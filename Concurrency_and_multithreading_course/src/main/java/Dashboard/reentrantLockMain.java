package Dashboard;

import lombok.Data;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class reentrantLockMain {
    public static void main(String... args){

    }
    @Data
    public static class PricesContainer{
        private Lock lockObject = new ReentrantLock();
        private double bitcoinPrice;
        private double etherPrice;
        private double litecoinPrice;
        private double bitcoinCashPrice;
        private double ripplePrice;

    }
    private static class PriceUpdater extends Thread{
        private PricesContainer pricesContainer;
        private Random random = new Random();
        public  PriceUpdater(PricesContainer pricesContainer){
            this.pricesContainer = pricesContainer;
        }
        @Override
        public void run(){
            while (true){
                pricesContainer.getLockObject().lock();
                try {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    pricesContainer.setBitcoinPrice(random.nextDouble(2000));
                    pricesContainer.setEtherPrice(random.nextDouble(2000));
                    pricesContainer.setLitecoinPrice(random.nextDouble(500));
                    pricesContainer.setBitcoinCashPrice(random.nextDouble(5000));
                    pricesContainer.setRipplePrice(random.nextDouble());
                }
                finally {
                    pricesContainer.getLockObject().unlock();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
