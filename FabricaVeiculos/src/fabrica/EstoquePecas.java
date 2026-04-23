package fabrica;

import java.util.concurrent.Semaphore;

public class EstoquePecas {

    public static final int CAPACIDADE_INICIAL = 500;

    private final Semaphore pecasDisponiveis;

    public EstoquePecas() {
        this.pecasDisponiveis = new Semaphore(CAPACIDADE_INICIAL, true);
    }

    public void consumirPeca() throws InterruptedException {
        pecasDisponiveis.acquire();
    }

    public void reporPeca() {
        pecasDisponiveis.release();
    }

    public void reporPecas(int quantidade) {
        pecasDisponiveis.release(quantidade);
    }

    public int getPecasDisponiveis() {
        return pecasDisponiveis.availablePermits();
    }
}
