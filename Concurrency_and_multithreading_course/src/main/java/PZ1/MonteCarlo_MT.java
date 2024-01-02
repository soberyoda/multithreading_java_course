package PZ1;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;

public class MonteCarlo_MT {
    static BlockingQueue<MCThread> threads = new LinkedBlockingQueue<>();
    public static void main(String... args) throws InterruptedException {
        long start = System.currentTimeMillis();
        double result1 = integrateParallel(5, 10);
        long end = System.currentTimeMillis();
        long duration = end-start;

        long start1 = System.currentTimeMillis();
        double result2 = integrateSequential(30);
        long end1 = System.currentTimeMillis();
        long duration2 = end1 - start1;
        System.out.println(result1);
        System.out.println("parallel duration : " + duration);
        System.out.println(result2);
        System.out.println("sequential duration: " + duration2);
    }
    public static double integrateSequential(int n) throws InterruptedException {
        double result = 0;
        MCThread thread = new MCThread((Double x)-> Math.sin(x), 0, Math.PI, n);
        thread.start();
        thread.join();
        result = thread.getResult();
        return result;
    }

    public static double integrateParallel(int k, int n) throws InterruptedException {
        double sum = 0;
        for(int i = 0; i < k ; i ++){
            MCThread thread = new MCThread(Math::sin, 0, Math.PI, n);
            threads.put(thread);
        }
        for(int i = 0; i < k ; i ++){
            MCThread thread = threads.take();
            thread.start();
            thread.join();
            sum += thread.getResult();
        }
        return sum/k;
    }
    private static class MCThread extends Thread{
        private Function<Double, Double> f;
        private double a;
        private double b;
        private int n;
        private double result = 0;
        public Random random = new Random();
        public MCThread(Function<Double, Double> f, double a, double b, int n){
            this.f = f;
            this.a = a;
            this.b = b;
            this.n = n;
        }
        public double getResult(){
            return this.result;
        }
        public double integrate(){
            double sum = 0;

            for(int i = 1; i < this.n; i ++){
                double x = random.nextDouble() * (this.b-this.a) + this.a;
                sum += this.f.apply(x) * (this.b - this.a) / this.n;
            }
            return sum;
        }
        @Override
        public void run() {
            this.result = integrate();
        }
    }
}
