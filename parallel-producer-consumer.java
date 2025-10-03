import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

// Classe que gerencia a fila compartilhada
class SharedQueue {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity;

    // Semáforos para controle de acesso
    private final Semaphore producerSemaphore;
    private final Semaphore consumerSemaphore;
    private final Semaphore accessSemaphore = new Semaphore(1); // Semáforo para acesso mútuo à fila

    public SharedQueue(int capacity) {
        this.capacity = capacity;
        this.producerSemaphore = new Semaphore(capacity); // Permite até 'capacity' produtores inserirem
        this.consumerSemaphore = new Semaphore(0); // Começa com 0, só permite consumidores quando há itens
    }

    // Método para o produtor inserir itens
    public void produce(int item) throws InterruptedException {
        // Pede permissão para inserir (aguarda se a fila estiver cheia)
        producerSemaphore.acquire();

        // Início da região crítica: acesso exclusivo à fila
        accessSemaphore.acquire();
        queue.add(item);
        System.out.println("Produziu: " + item);
        accessSemaphore.release();
        // Fim da região crítica

        // Libera permissão para o consumidor (sinaliza que um item está disponível)
        consumerSemaphore.release();
    }

    // Método para o consumidor remover itens
    public int consume() throws InterruptedException {
        // Pede permissão para consumir (aguarda se a fila estiver vazia)
        consumerSemaphore.acquire();

        // Início da região crítica: acesso exclusivo à fila
        accessSemaphore.acquire();
        int item = queue.poll();
        System.out.println("Consumiu: " + item);
        accessSemaphore.release();
        // Fim da região crítica

        // Libera permissão para o produtor (sinaliza que um espaço foi liberado)
        producerSemaphore.release();
        return item;
    }
}

// Classe principal
public class ParallelProducerConsumerSemaphore {
    public static void main(String[] args) {
        SharedQueue sharedQueue = new SharedQueue(5);

        // Thread produtora
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

        // Thread consumidora
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