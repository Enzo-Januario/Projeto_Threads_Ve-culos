package cliente;

import common.Veiculo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Garagem {

    private final int idDono;
    private final List<Veiculo> veiculos;
    private final Semaphore mutex;

    public Garagem(int idDono) {
        this.idDono = idDono;
        this.veiculos = new ArrayList<>();
        this.mutex = new Semaphore(1, true);
    }

    public int guardar(Veiculo v) throws InterruptedException {
        mutex.acquire();
        try {
            veiculos.add(v);
            return veiculos.size() - 1;
        } finally {
            mutex.release();
        }
    }

    public int getQuantidade() throws InterruptedException {
        mutex.acquire();
        try {
            return veiculos.size();
        } finally {
            mutex.release();
        }
    }

    public int getIdDono() {
        return idDono;
    }
}
