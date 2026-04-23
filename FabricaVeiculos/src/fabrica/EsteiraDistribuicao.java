package fabrica;

import java.util.concurrent.Semaphore;

public class EsteiraDistribuicao {

    public static final int SOLICITACOES_SIMULTANEAS = 5;

    private final Semaphore slots;

    public EsteiraDistribuicao() {
        this.slots = new Semaphore(SOLICITACOES_SIMULTANEAS, true);
    }

    public void solicitar() throws InterruptedException {
        slots.acquire();
    }

    public void liberar() {
        slots.release();
    }

    public int getSlotsDisponiveis() {
        return slots.availablePermits();
    }
}
