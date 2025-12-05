import java.util.ArrayList;
import java.util.List;

public class Jantar {
    private final int numFilosofos;
    private List<Filosofo> filosofos;
    private List<Garfo> garfos;

    public Jantar(int numFilosofos) {
        this.numFilosofos = numFilosofos;
        this.filosofos = new ArrayList<>();
        this.garfos = new ArrayList<>();
    }

    public List<Filosofo> getFilosofos() {
        return filosofos;
    }

    public List<Garfo> getGarfos() {
        return garfos;
    }

    public void iniciar() {
        for (int i = 0; i < numFilosofos; i++) {
            garfos.add(new Garfo(i + 1));
        }
        for (int i = 0; i < numFilosofos; i++) {
            Garfo garfoEsquerdo = garfos.get(i);
            Garfo garfoDireito = garfos.get((i + 1) % numFilosofos);
            
            Filosofo f = new Filosofo("F" + (i + 1), garfoEsquerdo, garfoDireito);
            filosofos.add(f);
        }
        for (int i = 0; i < numFilosofos; i++) {
            Filosofo atual = filosofos.get(i);
            Filosofo proximo = filosofos.get((i + 1) % numFilosofos);
            atual.setProximoFilosofo(proximo);
        }
    }
}