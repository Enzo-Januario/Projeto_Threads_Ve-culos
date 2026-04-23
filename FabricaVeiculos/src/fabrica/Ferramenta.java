package fabrica;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Ferramenta {

    private final int id;
    private final Semaphore semaforo;

    public Ferramenta(int id) {
        this.id = id;
        this.semaforo = new Semaphore(1, true);
    }

    public void pegar() throws InterruptedException {
        semaforo.acquire();
    }

    public boolean tentarPegar(long timeoutMs) throws InterruptedException {
        return semaforo.tryAcquire(timeoutMs, TimeUnit.MILLISECONDS);
    }

    public void soltar() {
        semaforo.release();
    }

    public int getId() {
        return id;
    }

    public boolean estaDisponivel() {
        return semaforo.availablePermits() > 0;
    }
}
