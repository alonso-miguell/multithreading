package atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class SynchAtomic {

    public static void main(String[] args) throws InterruptedException {
        AtomicInventory inventory = new AtomicInventory(new AtomicInteger(0));
        AtomicIncreasingThread increasingThread = new AtomicIncreasingThread(inventory);
        AtomicDecreasingThread decreasingThread = new AtomicDecreasingThread(inventory);

        increasingThread.start();
        decreasingThread.start();

        increasingThread.join();
        decreasingThread.join();

        System.out.println("final Inventory = " + inventory.counter);
    }
}

/*
    This example shows how to use AtomicInteger for the same code used in Synch.java

 */

class AtomicInventory {
    public AtomicInteger counter; // AtomicInteger declaration

    AtomicInventory(AtomicInteger counter) {
        this.counter = counter;
    }

    /*
        We don't need any synchronized methods anymore since the integer
        is already atomic
     */

    //incrementAndGet is a method for atomic integers, equals to ++counter
    //similarly getAndIncrement equals to counter++
    public void increaseInventory() {
        counter.incrementAndGet();
    }

    public void decreaseInventory() {
        counter.decrementAndGet();
    }
}

class AtomicDecreasingThread extends Thread {
    AtomicInventory inventory;

    AtomicDecreasingThread(AtomicInventory inventory) {
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

class AtomicIncreasingThread extends Thread {
    AtomicInventory inventory;

    AtomicIncreasingThread(AtomicInventory inventory) {
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