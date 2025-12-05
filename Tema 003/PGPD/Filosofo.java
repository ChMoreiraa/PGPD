import java.util.Random;

public class Filosofo implements Runnable {

    private final Random random = new Random();
    private final String nome;

    private Filosofo proximo;
    private final Garfo garfoEsquerdo;
    private final Garfo garfoDireito;

    public Filosofo(String nome, Garfo garfoEsquerdo, Garfo garfoDireito) {
        this.nome = nome;
        this.garfoEsquerdo = garfoEsquerdo;
        this.garfoDireito = garfoDireito;
    }

    public void setProximoFilosofo(Filosofo next) {
        this.proximo = next;
    }

    public String getNome() {
        return nome;
    }

    public Garfo getGarfoEsquerdo() {
        return garfoEsquerdo;
    }

    public Garfo getGarfoDireito() {
        return garfoDireito;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1; i++) {
            if (this.proximo != null) {
                synchronized(this.proximo) {
                    this.proximo.notify();
                }
            }
        }
    }
}