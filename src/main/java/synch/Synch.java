package synch;

public class Synch {

    public static void main(String[] args) throws InterruptedException {
        Inventory inventory = new Inventory(0);
        IncreasingThread increasingThread = new IncreasingThread(inventory);
        DecreasingThread decreasingThread = new DecreasingThread(inventory);

        increasingThread.start();
        decreasingThread.start();

        increasingThread.join();
        decreasingThread.join();

        System.out.println("final Inventory = " + inventory.counter);
    }
}

class Inventory {
    public int counter;

    /*
    Declaring and using multiple lock objects for different blocks of code provides a lot more of flexibility
    than just using synchronized keyword on a method signature
     */
    Object lock = new Object();
    Object lock2 = new Object();

    Inventory(int counter) {
        this.counter = counter;
    }

    /*
       There's 2 ways of synchronizing code:
       -Adding synchronized to a whole method
       -Declaring an object for lock purposes and use it in a block within a method,
           this is useful to avoid locking a whole method and only the contained block  will be locked

       In case of having multiple synchronized methods and one method gets locked, another thread will
       be unable to access any synchronized method and not only the one being used
    */

    public synchronized void increaseInventory() {
        //        synchronized (lock) {
        this.counter++;
        //        }
    }

    public synchronized void decreaseInventory() {
        //        synchronized (lock2) {
        this.counter--;
        //        }
    }
}

class DecreasingThread extends Thread {
    Inventory inventory;

    DecreasingThread(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            this.inventory.decreaseInventory();
        }
        System.out.println("Finished decreasing");
    }
}

class IncreasingThread extends Thread {
    Inventory inventory;

    IncreasingThread(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            this.inventory.increaseInventory();
        }

        System.out.println("Finished increasing");
    }
}