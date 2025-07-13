package readwritelock;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockExample {
    public static final int HIGHEST_PRICE = 1000;
    static Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        InventoryDatabase inventoryDatabase = new InventoryDatabase();

        Thread writer = new Thread(() -> {
            while (true) {
                inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
                inventoryDatabase.removeItem(random.nextInt(HIGHEST_PRICE));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        });

        // to avoid writing forever
        writer.setDaemon(true);
        writer.start();

        // Multiple reader threads to confirm the difference of time execution
        // of using ReentrantReadWriteLock vs ReentrantLock
        int numberOfReaderThreads = 7;
        List<Thread> readers = new ArrayList<>();

        for (int readerIndex = 0; readerIndex < numberOfReaderThreads; readerIndex++) {
            Thread reader = new Thread(() -> {
                for (int i = 0; i < 100000; i++) {
                    int upperBoundPrice = random.nextInt(HIGHEST_PRICE);
                    int lowerBoundPrice = upperBoundPrice > 0 ? random.nextInt(upperBoundPrice) : 0;
                    inventoryDatabase.getTotalItemsInPriceRange(lowerBoundPrice, upperBoundPrice);
                }
            });

            readers.add(reader);
        }

        long startReadingTime = System.currentTimeMillis();
        for (Thread reader : readers) {
            reader.start();
        }

        for (Thread reader : readers) {
            reader.join();
        }

        long endReadingTime = System.currentTimeMillis();

        System.out.println(String.format("Reading took %d ms", endReadingTime - startReadingTime));
    }

    public static class InventoryDatabase {
        private TreeMap<Integer, Integer> priceToCountMap = new TreeMap<>();

        /*

        ReentrantReadWriteLock differentiates between read and write access, need to declare
        a reader and writer for each kind of lock.
        -Multiple concurrent readers (if no writer is active).
        -Only one writer (exclusive).

        ReentrantLock is a mutual exclusion lock, blocks resources in general for both read and write operations
        Best used for heavy writer, non-concurrent threads

         */

        private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        private Lock readLock = reentrantReadWriteLock.readLock();
        private Lock writeLock = reentrantReadWriteLock.writeLock();

        private Lock lock = new ReentrantLock();
        /*
         Uncommiting readLock and writeLock vs lock shows the time difference between locks:

        approx time for ReentrantLock: 1000 ms
        approx time for readLock/writeLock: 250 ms

        Almost 1/4 of time, super efficient
         */


        // Constructor for initializing db with some random items
        InventoryDatabase() {
            for (int i = 0; i < 100000; i++) {
                this.addItem(random.nextInt(HIGHEST_PRICE));
            }
        }

        public int getTotalItemsInPriceRange(int lowerBound, int upperBound) {
            //            lock.lock();
            readLock.lock();
            try {
                Integer fromKey = priceToCountMap.ceilingKey(lowerBound);
                Integer toKey = priceToCountMap.floorKey(upperBound);

                if (fromKey == null || toKey == null) {
                    return 0;
                }

                NavigableMap<Integer, Integer> rangeOfPrices = priceToCountMap.subMap(fromKey, true, toKey, true);

                int sum = 0;
                for (int itemsInRange : rangeOfPrices.values()) {
                    sum += itemsInRange;
                }

                return sum;
            } finally {
                readLock.unlock();
                //                lock.unlock();
            }
        }

        public void addItem(int price) {
            //            lock.lock();
            writeLock.lock();

            try {
                Integer numberOfItemsForPrice = priceToCountMap.get(price);
                if (numberOfItemsForPrice == null) {
                    priceToCountMap.put(price, 1);
                } else {
                    priceToCountMap.put(price, numberOfItemsForPrice + 1);
                }

            } finally {
                writeLock.unlock();
                //                lock.unlock();
            }
        }

        public void removeItem(int price) {
            //            lock.lock();
            writeLock.lock();
            try {
                Integer numberOfItemsForPrice = priceToCountMap.get(price);
                if (numberOfItemsForPrice == null || numberOfItemsForPrice == 1) {
                    priceToCountMap.remove(price);
                } else {
                    priceToCountMap.put(price, numberOfItemsForPrice - 1);
                }
            } finally {
                writeLock.unlock();
                //                lock.unlock();
            }
        }
    }
}
