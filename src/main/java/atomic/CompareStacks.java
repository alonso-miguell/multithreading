package atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CompareStacks {
    /*
            Usually using atomic operations should be more efficient than using locks (synchronized blocks)
            but for some reason I'm getting it otherwise.

            Looks like this can be related to how CPU handles atomic operations and threads or the JDK version.

            Ran with:
               CPU: Ryzen 7, 7800X3D
               openjdk version "17.0.14" 2025-01-21 LTS

    //2 threads
    // atomic (free lock): 115,074,303    112,246,468     123,675,357
    // standard lock 465,714,951     439,231,065     464,210,262

    //4 threads
    // atomic (free lock): 119,867,424    117,017,079     122,214,332
    // standard lock: 447,580,143      441,690,653     446,401,714

    //8 threads
    // atomic (free lock): 132,159,344    120,883,832    129,464,871
    // standard lock 424,660,224      447,916,610     418,454,719

         */

    public static void main(String[] args) throws InterruptedException {
        //        StandardLockStack<Integer> stack = new StandardLockStack<>();
        AtomicStack<Integer> stack = new AtomicStack<>();
        Random random = new Random();

        for (int i = 0; i < 10000; i++) {
            stack.push(random.nextInt());
        }

        List<Thread> threadList = new ArrayList<>();
        int pushingThreads = 8;
        int poppingThreads = 8;

        for (int i = 0; i < pushingThreads; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    stack.push(random.nextInt());
                }
            });

            thread.setDaemon(true);
            threadList.add(thread);
        }

        for (int i = 0; i < poppingThreads; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    stack.pop();
                }
            });

            thread.setDaemon(true);
            threadList.add(thread);
        }

        for (Thread thread : threadList) {
            thread.start();
        }

        Thread.sleep(10000);
        System.out.println(String.format("%,d operations were performed in 10 secs ", stack.getCounter()));
    }
}
