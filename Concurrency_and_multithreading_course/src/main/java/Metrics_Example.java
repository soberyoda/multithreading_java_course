import java.awt.image.BufferedImage;
import java.util.Random;

public class Metrics_Example {
    public static void main(String... args){
        Metrics metrics = new Metrics();
        BusinessLogic businessLogicThread1 = new BusinessLogic(metrics);
        BusinessLogic businessLogicThread2 = new BusinessLogic(metrics);
        MetricsPrinter metricsPrinter = new MetricsPrinter(metrics);
        businessLogicThread1.start();
        businessLogicThread2.start();
        metricsPrinter.start();
    }
    public static class MetricsPrinter extends Thread{
        private Metrics metrics;
        public MetricsPrinter(Metrics metrics){
            this.metrics = metrics;
        }
        @Override
        public void run(){
            while (true){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                double currentAvg = metrics.getAverage();
                System.out.println("current avg: " + currentAvg);
            }
        }

    }
    public static class BusinessLogic extends Thread{
        private Random random = new Random();
        private Metrics metrics;
        public BusinessLogic(Metrics metrics){
            this.metrics = metrics;
        }

        @Override
        public void run(){
            while (true){
                long start = System.currentTimeMillis();
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                long end = System.currentTimeMillis();
                metrics.addSample(end-start);
            }
        }
    }
    public static class Metrics{
        private long count = 0;
        private volatile double average = 0.0;
        public synchronized void addSample(long sample){
            double currentSum = average * count;
            count ++;
            average = (currentSum + sample) / count;
        }
        public double getAverage(){
            return average;
        }
    }
}
