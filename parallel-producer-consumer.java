import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

class SharedQueue {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity;

    private final Semaphore producerSemaphore;
    private final Semaphore consumerSemaphore;
    private final Semaphore accessSemaphore = new Semaphore(1); 

    public SharedQueue(int capacity) {
        this.capacity = capacity;
        this.producerSemaphore = new Semaphore(capacity); 
        this.consumerSemaphore = new Semaphore(0); 
    }

    public void produce(int item) throws InterruptedException {
        producerSemaphore.acquire();

        accessSemaphore.acquire();
        queue.add(item);
        System.out.println("Produziu: " + item);
        accessSemaphore.release();

        consumerSemaphore.release();
    }

    public int consume() throws InterruptedException {
        consumerSemaphore.acquire();

        accessSemaphore.acquire();
        int item = queue.poll();
        System.out.println("Consumiu: " + item);
        accessSemaphore.release();
        producerSemaphore.release();
        return item;
    }
}

public class ParallelProducerConsumerSemaphore {
    public static void main(String[] args) {
        SharedQueue sharedQueue = new SharedQueue(5);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    sharedQueue.produce(i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    sharedQueue.consume();
                    Thread.sleep(150);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }

}
