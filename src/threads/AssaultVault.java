package threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AssaultVault {
    /*
    This exercise is to show Threads inheritance having classes
    -HackerThread which inherits  from Thread
    -Child classes for HackerThread are AscendingHackerThread and DescendingHackerThread which will increase or decrease count while trying to guess the password for hacking the vault
    -PoliceThread which inherits  from Thread and counts down from 10 to 0 tyring to catch the hackers before these hack into the vault
    -Vault class which will have a generate random int password
     */

    static final int MAX_PASSWORD = 9999;

    public static void main(String[] args) {
        Random random=new Random();

        //Generates a random password between 0 and MAX_PASSWORD
        Vault vault=new Vault(random.nextInt(MAX_PASSWORD));

//        AscendingHackerThread ascendingHackerThread=new AscendingHackerThread(vault);
//        DescendingHackerThread descendingHackerThread = new DescendingHackerThread(vault);
//        PoliceThread policeThread=new PoliceThread();
//
//        policeThread.start();
//        ascendingHackerThread.start();
//        descendingHackerThread.start();

        List<Thread> threads=new ArrayList<>();
        threads.add(new AscendingHackerThread(vault));
        threads.add(new DescendingHackerThread(vault));
        threads.add(new PoliceThread());

        threads.forEach(Thread::start);
    }

    public static class Vault {
        int password;

        Vault(int password) {
            this.password = password;
        }

        public boolean hackedVault(int password) {
            // Make dome time delay in each attempt
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
            }

            return password == this.password;
        }
    }

    public static class PoliceThread extends Thread {

        PoliceThread() {
            this.setName(this.getClass().getSimpleName());
        }

        @Override
        public void run() {
            System.out.println("Starting thread " + this.getName());
            for (int i = 10; i >0; i--) {
                System.out.println("Counting down: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            System.out.println("Police caught the hackers!!");
            System.exit(0);
        }
    }

    public static class HackerThread extends Thread {
        protected Vault vault;

        HackerThread(Vault vault) {
            this.vault = vault;
            this.setName(this.getClass().getSimpleName());
            this.setPriority(MAX_PRIORITY);
        }

        //The exercise used this just to avoid adding the sout to every child
        //shouldn't really override start, safer to override run()
//        @Override
//        public void start() {
//            System.out.println("Starting thread " + this.getName());
//            super.start();
//        }
    }

    public static class AscendingHackerThread extends HackerThread {
        AscendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            System.out.println("Starting thread " + this.getName());

            for (int i = 0; i < MAX_PASSWORD; i++) {
                if (vault.hackedVault(i)) {
                    System.out.println(this.getName() + " hacked the vault with password: " + i);
                    System.exit(0);
                }
            }
        }
    }

    public static class DescendingHackerThread extends HackerThread {
        DescendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            System.out.println("Starting thread " + this.getName());

            for (int i = MAX_PASSWORD; i >0; i--) {
                if (vault.hackedVault(i)) {
                    System.out.println(this.getName() + " hacked the vault with password: " + i);
                    System.exit(0);
                }
            }
        }
    }
}
