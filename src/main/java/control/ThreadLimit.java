package control;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/*
This class prints the limit of Threads that can be created by the CPU
before throwing a memory exception
 */


/* Current result for 7800X3D, took around 10 mins

161576
#
# There is insufficient memory for the Java Runtime Environment to continue.
# Native memory allocation (malloc) failed to allocate 1292720 bytes. Error detail: AllocateHeap
# An error report file with more information is saved as:
# F:\Home\Cursos\JAVA spring\main.java.multithreading\hs_err_pid17612.log
[280.758s][warning][os] Loading hsdis library failed

Process finished with exit code 1
 */

public class ThreadLimit {
    public static void main(String [] args) {
        AtomicInteger counter = new AtomicInteger(0);
        while(true){
            new Thread(() -> {
                System.out.println(counter.incrementAndGet());
                try {
                    Thread.sleep(Duration.ofHours(1).toMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
} 