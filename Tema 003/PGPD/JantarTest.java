import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JantarTest {

    private Jantar jantar;
    private final int NUM_FILOSOFOS = 5;
    @BeforeEach
    public void setup() {
        jantar = new Jantar(NUM_FILOSOFOS);
        jantar.iniciar();
    }

    //Testes de Verificação de Artefatos

    @Test
    void deveCriarONumeroCorretoDeFilosofos() {
        assertEquals(NUM_FILOSOFOS, jantar.getFilosofos().size(), 
            "Deve haver o número exato de Filósofos (N).");
    }

    @Test
    void deveCriarONumeroCorretoDeGarfos() {
        assertEquals(NUM_FILOSOFOS, jantar.getGarfos().size(), 
            "Deve haver o número exato de Garfos (N).");
    }
    
    //Testes de Configuração de Garfos

    @Test
    void cadaFilosofoDeveTerDoisGarfosDistintos() {
        for (Filosofo f : jantar.getFilosofos()) {
            assertNotNull(f.getGarfoEsquerdo(), "Garfo Esquerdo não deve ser nulo.");
            assertNotNull(f.getGarfoDireito(), "Garfo Direito não deve ser nulo.");
            assertNotEquals(f.getGarfoEsquerdo(), f.getGarfoDireito(), 
                "Garfo Esquerdo e Direito devem ser objetos diferentes.");
        }
    }

    @Test
    void garfosDevemSerCompartilhadosApenasEntreAdjacentes() {
        
        List<Garfo> todosGarfosReferenciados = jantar.getFilosofos().stream()
            .flatMap(f -> List.of(f.getGarfoEsquerdo(), f.getGarfoDireito()).stream())
            .collect(Collectors.toList());

        Map<Garfo, Long> contagemPorGarfo = todosGarfosReferenciados.stream()
            .collect(Collectors.groupingBy(g -> g, Collectors.counting()));

        for (Garfo g : jantar.getGarfos()) {
            Long contagem = contagemPorGarfo.getOrDefault(g, 0L);
            assertEquals(2L, contagem, 
                "O Garfo " + g.getId() + " deve ser referenciado por exatamente 2 filósofos.");
        }
    }
    
    // --- Testes de Configuração do Ciclo de Notify ---

    @Test
    void cicloDeNotificacaoDeveSerFechadoCorretamente() {
        
        List<Filosofo> filosofos = jantar.getFilosofos();
        
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            Filosofo atual = filosofos.get(i);
            Filosofo proximoEsperado = filosofos.get((i + 1) % NUM_FILOSOFOS);
            assertEquals(proximoEsperado.getNome(), atual.proximo.getNome(),
                "O Filósofo " + atual.getNome() + " deve notificar o Filósofo " + proximoEsperado.getNome() + ".");
        }
    }
}