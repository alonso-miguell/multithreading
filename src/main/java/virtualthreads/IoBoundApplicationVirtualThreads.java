package virtualthreads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * This class just changes how the pool is created with virtual Threads
 * Things to note:
 * -they have a default priority, Setting priority for virtual threads does nothing, is ignored
 * -Virtual Threads are daemon for default, trying to set them to false will throw an exception
 * -Doesn't make sense to create a fixed-size pool of virtual threads sin the JVM handles this
 *  in the most optimal way
 */

public class IoBoundApplicationVirtualThreads {

    // couldn't crash it with 1000
    private static final int NUMBER_OF_TASKS = 10_000;

    public static void main(String[] args) {
        System.out.printf("Running %d tasks\n", NUMBER_OF_TASKS);

        long start = System.currentTimeMillis();
        performTasks();
        System.out.printf("Tasks took %dms to complete\n", System.currentTimeMillis() - start);
    }

    private static void performTasks() {

        /** threads
         *  We just changed newCachedThreadPool for newVirtualThreadPerTaskExecutor
         *
         */
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor(); ) { // fixed number

            for (int i = 0; i < NUMBER_OF_TASKS; i++) {
                executorService.submit(() -> blockingIoOperation());
            }

            /*
            uncommenting this and setting sleep to 10ms
            show a huge improvement comparing to platform threads.
            It reduces the total time from 16s to 3s
            */

//                        for (int i = 0; i < NUMBER_OF_TASKS; i++) {
//                            executorService.submit(() -> {
//                                for(int j = 0; j < 100; j++) {
//                                    blockingIoOperation();
//                                }
//                            });
//                        }
        }
    }

    // Simulates a long blocking IO
    private static void blockingIoOperation() {
        System.out.println("Executing a blocking task from thread: " + Thread.currentThread());
        try {
//                        Thread.sleep(10);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}