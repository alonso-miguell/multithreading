package interruptions;

public class DaemonThread {

    /*
    Daemon threads will end when the main threat ends.
    We need to setDaemon(true) and we don't need to throw InterruptedException or set a condition to handle exit point
     */

    public static void main(String[] args) {
        Thread blockingThread = new Thread(new BlockingThread());

        // commenting this line will keep blockingThread alive indefinitely
        blockingThread.setDaemon(true);
        blockingThread.start();

        try {
            System.out.println("waiting on main thread");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("ending main thread");
    }

    static class BlockingThread implements Runnable {

        @Override
        public void run() {

            while (true) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("BlockingThread is running");
            }
        }
    }
}
