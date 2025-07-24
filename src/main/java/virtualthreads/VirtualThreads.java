package virtualthreads;

import java.util.ArrayList;
import java.util.List;

/**
 * Threads are divided into:
 * -Platform threads: same old treads created by the JVM and that are managed and scheduled by the OS
 *  and follow the rule of 1 thread per core
 * -Virtual thread: which actually, the OS won't know nothing about it, since they are just jave objects in the heap. But:
 * --They are managed by
 *      A pool of carrie threads
 *      Carrier thread are nothing but plataform thread that handles virtual threads
 *      The maximun number of carrier threads that can be created is the same number of (logical?) cores
 */
public class VirtualThreads {
    static final int THREADS_TO_RUN = 1000;

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> System.out.println("Calling a thread: " + Thread.currentThread());

        List<Thread> threads = new ArrayList<>();
        List<Thread> virtualThreads = new ArrayList<>();
        for (int i = 0; i < THREADS_TO_RUN; i++) {
            //            Thread thread=new Thread(runnable);
            // this is the same than new Thread()
            //            Thread thread = Thread.ofPlatform().unstarted(new BlockingTask());
            //            threads.add(thread);

            // way to create a virtual thread
            Thread virtualThread = Thread.ofVirtual().unstarted(new BlockingTask());
            virtualThreads.add(virtualThread);
        }

        //        for(Thread thread: threads){
        //            thread.start();
        //            thread.join();
        //        }

        for (Thread virtualThread : virtualThreads) {
            virtualThread.start();
        }

        for (Thread virtualThread : virtualThreads) {
            virtualThread.join();
        }
    }

    private static class BlockingTask implements Runnable {

        @Override
        public void run() {
            System.out.println(" Inside thread: " + Thread.currentThread() + " before blocking call");
            /**
             * Output looks like this:
             * VirtualThread[#51]/runnable@ForkJoinPool-1-worker-11 before blocking call
             * Where:
             * Type= VirtualThread
             * Thread number= #51
             * pool name=ForkJoinPool-1
             * platform thread name where the virtual thread was mounted: worker-11
             */
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }

            /**
             * Take note that virtual threads can be mounte/unmounted to different carrier threads
             * depending on how they are handle by the OS, we can't control that as devs
             */
            System.out.println(" Inside thread: " + Thread.currentThread() + " after blocking call");
        }
    }
}
