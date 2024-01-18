import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadBlock {
    public static final int HIGHEST_PRICE = 1000;
    public static void main(String... args) throws InterruptedException {
        InventoryDatabase inventoryDatabase = new InventoryDatabase();
        Random random = new Random();
        for (int i = 0; i < 100000; i ++){
            inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
        }

        Thread writer = new Thread(()-> {
            while (true){
                inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
                inventoryDatabase.removeItem(random.nextInt(HIGHEST_PRICE));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        writer.setDaemon(true);
        writer.start();

        int numb_of_threads = 7;
        List<Thread> threadList = new ArrayList<>();
        for(int i = 0; i < numb_of_threads; i ++){
            Thread reader = new Thread(()->{
                for(int j = 0; j < 100000; j ++){
                    int uppBprice = random.nextInt(HIGHEST_PRICE);
                    int lowerBprice = uppBprice > 0 ? random.nextInt(uppBprice): 0;
                    inventoryDatabase.getNumberOfItemsInPriceRange(lowerBprice, uppBprice);
                }
            });
            reader.setDaemon(true);
            threadList.add(reader);
        }

        long start = System.currentTimeMillis();
        for(Thread thread : threadList){
            thread.start();
        }
        for(Thread thread : threadList){
            thread.join();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
    public static class InventoryDatabase {
        private ReentrantLock lock = new ReentrantLock();

        private TreeMap<Integer, Integer> priceToCountMap = new TreeMap<>();
        public int getNumberOfItemsInPriceRange(int lowerBound, int upperBound){
            lock.lock();
            try {
                Integer fromKey = priceToCountMap.ceilingKey(lowerBound);
                Integer toKey = priceToCountMap.floorKey(upperBound);
                if (fromKey == null || toKey == null) {
                    return 0;
                }
                NavigableMap<Integer, Integer> rangeOfPrices = priceToCountMap.subMap(fromKey, true, toKey, true);
                int sum = 0;
                for (int numberOfItemsForPrice : rangeOfPrices.values()) {
                    sum += numberOfItemsForPrice;
                }
                return sum;
            }finally {
                lock.unlock();
            }
        }

        public void addItem(int price) {
            lock.lock();
            try {
                priceToCountMap.merge(price, 1, Integer::sum);

            } finally {
                lock.unlock();
            }
        }
        public void removeItem(int price) {
            lock.lock();
            try {
                Integer numberOfItemsForPrice = priceToCountMap.get(price);
                if (numberOfItemsForPrice == null || numberOfItemsForPrice == 1) {
                    priceToCountMap.remove(price);
                } else {
                    priceToCountMap.put(price, numberOfItemsForPrice - 1);

                }
            }finally {
                lock.unlock();
            }
        }

    }
}
