package synch;

public class DataRace {
    public static void main(String[] args) throws InterruptedException {
        SharedClass sharedClass = new SharedClass();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sharedClass.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sharedClass.checkForDataRace();
            }

        });

        thread1.start();
        thread2.start();

        thread2.join();
        thread1.join();
    }

    public static class SharedClass {
        /*
        Adding volatile to variables makes operations on them, atomic
        thus making it more secure for multithreading and avoiding data race
         */

        private volatile int x = 0;
        private volatile int y = 0;

        public void increment() {
            x++;
            y++;
        }

        //This should never happen in a single thread, using multithreading this isn't guaranteed
        public void checkForDataRace() {
            if (y > x) {
                System.out.println("y > x - Data Race is detected");
            }
        }
    }
}
