package main.java.control;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoinThread {
    public static void main(String[] args) {
        List<Long> inputNumbers = Arrays.asList(0L, 30L, 500L, 784265L);
        List<FactorialThread> threads = new ArrayList<>();

        inputNumbers.forEach(input -> {
            FactorialThread factorialThread = new FactorialThread(input);

            // Commenting this wait until last calculation is finished
            factorialThread.setDaemon(true);
            threads.add(factorialThread);
        });

        threads.forEach(Thread::start);

        threads.forEach(thread -> {

            // Needs to use join after starting the thread
            // Join will wait for all thread during that time before returning to mainThread
            // This is useful for waiting at most X time for other threads before continuing in main
            try {
                thread.join(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (!thread.isAlive()) {
                System.out.println(thread.getName() + " has already finished after joining");
            }
        });

        threads.forEach(thread -> {
            if (thread.isFinished) {
                System.out.println("Calculation for " + thread.inputNumber + " has been completed: " + thread.result);
            } else {
                System.out.println("Calculation for " + thread.inputNumber + " IN PROGRESS");
            }
        });
    }

    static class FactorialThread extends Thread {
        long inputNumber;
        public boolean isFinished = false;
        public BigInteger result = BigInteger.ZERO;

        FactorialThread(long inputNumber) {
            this.inputNumber = inputNumber;
        }

        @Override
        public void run() {
            this.result = calculateFactorial(this.inputNumber);

            this.isFinished = true;
        }

        BigInteger calculateFactorial(long inputNumber) {
            BigInteger tempResult = BigInteger.ONE;

            for (long i = 1; i <= inputNumber; i++) {
                tempResult = tempResult.multiply(new BigInteger(String.valueOf(i)));

            }

            return tempResult;
        }
    }
}
