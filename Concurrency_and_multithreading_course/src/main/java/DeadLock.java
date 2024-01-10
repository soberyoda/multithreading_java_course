import java.util.Objects;
import java.util.Random;

public class DeadLock {
    public static void main(String... args){
        Intersection intersection = new Intersection();
        TrainA trainA = new TrainA(intersection);
        TrainB trainB = new TrainB(intersection);
        Thread thread = new Thread(trainA);
        Thread thread1 = new Thread(trainB);
        thread.start();
        thread1.start();
    }
    public static class TrainA implements Runnable{
        private Intersection intersection= new Intersection();
        private Random random = new Random();
        public TrainA(Intersection intersection){
            this.intersection = intersection;
        }
        @Override
        public void run() {
            while (true){
                long sleepingTime = random.nextInt(5);
                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                intersection.takeRoadA();
            }
        }
    }
    public static class TrainB implements Runnable{
        private Intersection intersection= new Intersection();
        private Random random = new Random();
        public TrainB(Intersection intersection){
            this.intersection = intersection;
        }
        @Override
        public void run() {
            while (true){
                long sleepingTime = random.nextInt(5);
                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                intersection.takeRoadB();
            }
        }
    }
    private static class Intersection{
        private Object roadA = new Object();
        private Object roadB = new Object();
        public void takeRoadA(){
            synchronized (roadA){
                System.out.println("roadA is locked by Thread: " + Thread.currentThread().getName());
            }
            synchronized (roadB){
                System.out.println("Train is passing through road A");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        public void takeRoadB(){
            synchronized (roadA){
                System.out.println("roadB is locked by thread: "+ Thread.currentThread().getName());
            }
            synchronized (roadB){
                System.out.println("Train is passing through roadB");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
