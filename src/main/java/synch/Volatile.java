package main.java.synch;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
public class Volatile {
    public static void main(String[] args) {
        Metrics metrics = new Metrics();

        BusinessLogic businessLogicThread1 = new BusinessLogic(metrics);

        BusinessLogic businessLogicThread2 = new BusinessLogic(metrics);

        MetricsPrinter metricsPrinter = new MetricsPrinter(metrics);

        businessLogicThread1.start();
        businessLogicThread2.start();
        metricsPrinter.start();
    }

    public static class MetricsPrinter extends Thread {
        private Metrics metrics;

        public MetricsPrinter(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }

                double currentAverage = metrics.getAverage();

                System.out.println("Current Average is " + currentAverage);
            }
        }
    }

    public static class BusinessLogic extends Thread {
        private Metrics metrics;
        private Random random = new Random();

        public BusinessLogic(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                long start = System.currentTimeMillis();

                try {
                    Thread.sleep(random.nextInt(100));
                } catch (InterruptedException e) {
                }

                long end = System.currentTimeMillis();

                metrics.addSample(end - start);
            }
        }
    }

    /*



    By default all primitive variable assignations are atomic except for long and double due to 32 bits
    for that, we can use volatile keyword when declaring a long/double variable

    Assignments to a reference are also atomic

     */
    public static class Metrics {
        private long count = 0; //not need to add volatile since only use is already in a synchronized block
        private volatile double average = 0.0; // volatile assignation

        public synchronized void addSample(long sample) {
            double currentSum = average * count;
            count++;
            average = (currentSum + sample) / count;
        }

        //This is considered as atomic operation
        public double getAverage() {
            return average;
        }
    }
}