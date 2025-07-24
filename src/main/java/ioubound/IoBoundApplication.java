package ioubound;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Michael Pogrebinsky - Top Developer Academy
 * Thread Per Task Threading Model
 * https://www.udemy.com/java-multithreading-concurrency-performance-optimization
 */

/**
 * All of this cover IO blocking operations.
 * We can also use IO non-blocking operations that will make use of callbacks
 * for handling data/operations, but usually this is more complicated and require
 * some external APIs/libraries like:
 * -netty
 * -vert.x
 * -webflux
 *
 * Also using non-blocking operations could end in what's called callback hell
 * (basically a lot of nested callbacks depending on each other result)
 */

public class IoBoundApplication {

    // couldn't crash it with 1000
    private static final int NUMBER_OF_TASKS = 10_000;

    public static void main(String[] args) {
        System.out.printf("Running %d tasks\n", NUMBER_OF_TASKS);

        long start = System.currentTimeMillis();
        performTasks();
        System.out.printf("Tasks took %dms to complete\n", System.currentTimeMillis() - start);
    }

    private static void performTasks() {

        //        try (ExecutorService executorService = Executors.newCachedThreadPool(); ) { //dynamic number of
        // threads
        try (ExecutorService executorService = Executors.newFixedThreadPool(1000); ) { // fixed number

            for (int i = 0; i < NUMBER_OF_TASKS; i++) {
                executorService.submit(() -> blockingIoOperation());
            }

            /*
            uncommenting this and setting sleep to 10ms
            proves that context switches (blocking/unblocking) and scheduling/rescheduling
            slows overall processing time

            should take around 10s to process 10k tasks but since every thread is
            scheduled and rescheduled 100 times, it slows the whole process

            this is called Thrashing

            where a system spends more time swapping data in and out of memory
            (or other resources) than performing actual work.

            🔹 Common in:
               Virtual memory systems (e.g., OS constantly paging between RAM and disk)
               Multithreaded environments (excessive context switching)
               Caches (frequent cache invalidations)
            */

            //            for (int i = 0; i < NUMBER_OF_TASKS; i++) {
            //                executorService.submit(() -> {
            //                    for(int j = 0; j < 100; j++) {
            //                        blockingIoOperation();
            //                    }
            //                });
            //            }
        }
    }

    // Simulates a long blocking IO
    private static void blockingIoOperation() {
        System.out.println("Executing a blocking task from thread: " + Thread.currentThread());
        try {
            //            Thread.sleep(10);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}