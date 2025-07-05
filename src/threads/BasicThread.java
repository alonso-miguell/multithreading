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

        //        Thread.sleep(1000);

    }
}
