package threads;

public class BasicThread {

    public static void main(String[] args) throws InterruptedException {

        // Traditional way
        //        Thread thread=new Thread(new Runnable() {
        //            @Override
        //            public void run() {
        //                System.out.println("Max priority is: "+Thread.currentThread().getPriority());
        //                System.out.println("In thread: "+Thread.currentThread().getName());
        //
        //            }
        //        });

        // using lambdas
        Thread thread = new Thread(() -> {
            System.out.println("Max priority is: " + Thread.currentThread().getPriority());
            System.out.println("In thread: " + Thread.currentThread().getName());
        });

        thread.setName("MyThread1");
        thread.setPriority(Thread.MAX_PRIORITY);

        System.out.println("In thread:" + Thread.currentThread().getName() + " before starting a new thread");
        thread.start();
        System.out.println("In thread:" + Thread.currentThread().getName() + " after starting a new thread");

        Thread.sleep(1000);

        Thread customThread = new CustomThread();
        customThread.setName("customThread1");
        customThread.start();

        //creating new thread with a customRunnable class
        Thread customRunnable = new Thread(new CustomRunnable());
        customRunnable.start();
    }


}

/*
There are 2 ways of creating a thread in java:
-Implementing the Runnable interface when instantiating the thread
-Extend from Thread in another class and instantiating tha thread (Thread already implements runnable interface)

Both are correct and depend only on how you want to organize code
 */

class CustomThread extends Thread {
    @Override
    public void run() {
        System.out.println("I'm a custom thread with name:"+currentThread().getName());
    }
}

//This is also another way to create a custom class implementing runnable instead of extending thread
class CustomRunnable implements Runnable {
    @Override
    public void run(){
        System.out.println("Hello from new thread overriding runnable");
    }
}