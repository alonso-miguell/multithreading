package reentrantlock;

import java.util.Random;

public class PriceUpdater extends Thread {
    private PricesContainer pricesContainer;
    private Random random = new Random();

    public PriceUpdater(PricesContainer pricesContainer) {
        this.pricesContainer = pricesContainer;
    }

    @Override
    public void run() {
        while (true) {
            pricesContainer.getLockObject().lock();
            System.out.println("locked by priceUpdater");

            try {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                pricesContainer.setBitcoinPrice(random.nextInt(20000));
                pricesContainer.setEtherPrice(random.nextInt(2000));
                pricesContainer.setLitecoinPrice(random.nextInt(500));
                pricesContainer.setBitcoinCashPrice(random.nextInt(5000));
                pricesContainer.setRipplePrice(random.nextDouble());
            }

            /*
                Note: we use finally to unlock whatever happens/we do after locking the thread
                using lock(). Without using finally and in case the thread throws an exception
                the thread will be stuck/asleep
             */
            finally {
                System.out.println("unlocked by priceUpdater");
                pricesContainer.getLockObject().unlock();
            }

            //this gives a chance to UI to lock the object
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }
    }
}