package PZ1;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class Mean {
    private static double[] array;
    static BlockingQueue<Double> results = new ArrayBlockingQueue<Double>(100);
    static void initArray(int size){
        array = new double[size];
        for(int i = 0; i < size; i++){
            array[i] = Math.random()*size/(i+1);
        }
    }
    public static void main(String... args) throws InterruptedException, ExecutionException {
        initArray(100000000);
        double start = System.currentTimeMillis();
        parallelMean(8);
        asyncMeanv1();
        asyncMeanv2();
        double end = System.currentTimeMillis();
        System.out.println(end-start);

    }
    private static class MeanCalc extends Thread{
        private final int start;
        private final int end;
        double mean = 0;
        MeanCalc(int start, int end){
            this.start = start;
            this.end = end;
        }
        private void calc() throws InterruptedException {
            double sum = 0;
            for(int i = this.start; i < this.end; i ++){
                sum += array[i];
            }
            this.mean = sum / (this.end - this.start + 1);
            results.put(mean);
        }
        @Override
        public void run(){
            try {
                calc();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            System.out.printf(Locale.US,"%d-%d mean=%f\n",start,end,mean);
        }

    }
    static void parallelMean(int cnt) throws InterruptedException {
//        MeanCalc[] threads = new MeanCalc[cnt];
        int numberOfArr = array.length / cnt;
        double meanOfMeans = 0;
        ExecutorService executorService = Executors.newFixedThreadPool(cnt);
        double t1 = System.currentTimeMillis();
        for(int i = 0; i < cnt ; i ++) {
            int start = i * numberOfArr;
            int end = (i + 1) * numberOfArr - 1;
//            threads[i] = new MeanCalc(start, end);
            executorService.execute(new MeanCalc(start, end));
        }
//        for(MeanCalc thread : threads){
//            thread.start();
//        }


//        for(MeanCalc thread : threads){
//            thread.join();
//        }
//        for(MeanCalc thread : threads){
//            meanOfMeans += thread.mean;
//        }
        for(int i = 0; i < cnt; i ++){
            meanOfMeans += results.take();
        }
        meanOfMeans /= cnt;
        executorService.shutdown();
        double t2 = System.currentTimeMillis();
        double t3 = System.currentTimeMillis();
        System.out.printf(Locale.US,"size = %d cnt=%d >  t2-t1=%f t3-t1=%f mean=%f\n",
                array.length,
                cnt,
                t2-t1,
                t3-t1,
                meanOfMeans);
    }
    static class AsyncMean{
        static double[] array;
        static void initArray(int size){
            array = new double[size];
            for(int i = 0; i < size; i ++){
                array[i] = Math.random()*size/ (i+1);
            }
        }
    }
    static class MeanCalcSupplier implements Supplier<Double>{
        private int start;
        private int end;
        MeanCalcSupplier(int start, int end){
            this.start = start;
            this.end = end;
        }
        @Override
        public Double get() {
            double mean;
            double sum = 0;
            for(int i = this.start; i < this.end; i ++){
                sum += array[i];
            }
            mean = sum / (this.end - this.start + 1);
            return mean;
        }
    }
    public static void asyncMeanv1() throws ExecutionException, InterruptedException {
        int size = 100_000_000;
        initArray(size);
        int n= 16;
        ExecutorService executor = Executors.newFixedThreadPool(n);
        int numberOfArr = array.length/n;
        // Utwórz listę future
        List<CompletableFuture<Double>> partialResults = new ArrayList<>();
        for(int i=0;i<n;i++){
            int start = i * numberOfArr;
            int end = (i+1) * numberOfArr -1;
            CompletableFuture<Double> partialMean = CompletableFuture.supplyAsync(
                    new MeanCalcSupplier(start,end),executor);
            partialResults.add(partialMean);
        }
        // zagreguj wyniki
        double mean=0;
        double t1 = System.currentTimeMillis();
        for(var pr:partialResults){
            mean += pr.join();
            // wywołaj pr.join() aby odczytać wartość future;
            // join() zawiesza wątek wołający
        }
        double t2 = System.currentTimeMillis();
        mean /= n;
        executor.shutdown();
        System.out.printf(Locale.US,"mean=%f\n",mean);
    }
    static void asyncMeanv2() throws InterruptedException {
        int size = 100_000_000;
        initArray(size);
        ExecutorService executor = Executors.newFixedThreadPool(16);
        int n=10;

        BlockingQueue<Double> queue = new ArrayBlockingQueue<>(n);
        int numberOfArr = array.length/n;

        for (int i = 0; i < n; i++) {
            int start = i * numberOfArr;
            int end = (i+1) * numberOfArr -1;
            CompletableFuture.supplyAsync(
                    new MeanCalcSupplier(start,end), executor)
            .thenApply(d -> queue.offer(d));
        }

        double mean=0;
        // w pętli obejmującej n iteracji wywołaj queue.take() i oblicz średnią
        for(int i = 0; i < n; i ++){
            mean += queue.take();
        }
        mean /= n;
        System.out.printf(Locale.US,"mean=%f\n", mean);

        executor.shutdown();
    }
}
