package interruptions;


public class InterruptableThread {
    public static void main(String[] args){
        Thread blockingThread= new Thread( new BlockingThread());

        blockingThread.start();
        blockingThread.setName("blockingThread");

        //it only works with JDK methods that declare that  throw an InterruptedException
        blockingThread.interrupt();

        Thread anotherBlockingThread = new Thread(new AnotherBlockingThread());

        anotherBlockingThread.start();
        blockingThread.setName("anotherBlockingThread");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        anotherBlockingThread.interrupt();

    }

    /*
    Method 1 to interrupt a thread
    By throwing an exception using interrupt method and handling it
 */

    static class BlockingThread implements Runnable{

        @Override
        public void run() {


            try{
                Thread.sleep(10000000);
            }
            catch (InterruptedException e){
                System.out.println("Exiting blocking thread");
            }

        }
    }


    /*
Method 2 to interrupt a thread
By calling interrupt, checking if the thread was interrupted and handling it with proper exit condition
*/
    static class AnotherBlockingThread implements Runnable{

        @Override
        public void run() {

            while(true){
                System.out.println("AnotherBlockingThread is running");

                if(Thread.interrupted()){
                    System.out.println("AnotherBlockingThread was interrupted");
                    return;
                }
            }

        }
    }
}
