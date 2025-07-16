package notify;


public class WaitNotifyExample {

    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue(10);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= queue.TOTAL_MESSAGES; i++) {
                    queue.produce("Message " + i);
                    Thread.sleep(1000); // simulate work
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.setName("Producer ");


        Consumer consumer1=new Consumer(queue, "consumer 1");
        Consumer consumer2=new Consumer(queue, "consumer 2");

        producer.start();

        consumer1.start();
        consumer2.start();
    }

    static class Consumer extends Thread {
        private MessageQueue messageQueue;

        Consumer(MessageQueue messageQueue, String name) {
            this.messageQueue = messageQueue;
            this.setName(name);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String msg = messageQueue.consume();

                    if(msg==null) break;

                    System.out.println("Consumed: " + msg +" by " + Thread.currentThread().getName());
                    Thread.sleep(50); // simulate processing
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class MessageQueue {
        private String message;
        private boolean hasMessage = false;
        private volatile int TOTAL_MESSAGES;
        private volatile int consumedMessages;
        private boolean hasFinished = false;

        MessageQueue(int totalMessages) {
            this.TOTAL_MESSAGES = totalMessages;
        }

        public synchronized void produce(String newMessage) throws InterruptedException {
            while (hasMessage) {
                System.out.println(Thread.currentThread().getName() + " is waiting");
                wait(); // Wait until the message is consumed
            }
            this.message = newMessage;
            hasMessage = true;
            System.out.println("Produced: " + newMessage);
            notifyAll(); // Notify consumer
        }

        public synchronized String consume() throws InterruptedException {

            while (!hasMessage) {
                if (hasFinished) return null;

                System.out.println(Thread.currentThread().getName() + " is waiting for message");
                wait(); // Wait until a message is available
            }
            String result = message;
            hasMessage = false;

            consumedMessages++;

            if (consumedMessages == TOTAL_MESSAGES) {
                hasFinished = true;
                notifyAll();  // Wake up consumers  and producers waiting for more
            }

            return result;
        }
    }
}
