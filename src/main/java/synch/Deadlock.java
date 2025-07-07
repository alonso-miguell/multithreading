package main.java.synch;

import java.util.Random;

public class Deadlock {

    /*

    This exercise shows a circular deadlock.

    2 trains are trying to cross an intersection and wait for the resource to be unlocked by another thread
    which is at the same time waiting for the other resource to be released thus both resources end up locked.

    This is due to having lockA and lockB waiting on different order

     */

    public static void main(String[] args) {
        Intersection intersection = new Intersection();

        Thread trainA = new TrainA(intersection);
        Thread trainB = new TrainB(intersection);

        trainA.start();
        trainB.start();
    }

    public static class TrainA extends Thread {
        Intersection intersection;
        private Random random = new Random();

        TrainA(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while (true) {
                long sleepingTime = random.nextInt(5);
                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException ie) {
                }
                intersection.takeRoadA();
            }
        }
    }

    public static class TrainB extends Thread {
        Intersection intersection;
        private Random random = new Random();

        TrainB(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while (true) {
                long sleepingTime = random.nextInt(5);
                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException ie) {
                }
                intersection.takeRoadB();
            }
        }
    }

    public static class Intersection {
        private Object lockA = new Object();
        private Object lockB = new Object();

        public void takeRoadA() {
            synchronized (lockA) {
                System.out.println(
                        "Road A is locked by thread = " + Thread.currentThread().getName());

                synchronized (lockB) {
                    System.out.println("Train is passing by road A");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        public void takeRoadB() {

            // Using the same order lockA and then lockB, solves the deadlock issue
            synchronized (lockA) { // change to lockA
                System.out.println(
                        "Road B is locked by thread = " + Thread.currentThread().getName());

                synchronized (lockB ) { //change to lockB
                    System.out.println("Train is passing by road B");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
}
