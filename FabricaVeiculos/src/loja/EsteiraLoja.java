package loja;

import common.Veiculo;

import java.util.concurrent.Semaphore;

public class EsteiraLoja {

    private final int capacidade;
    private final Veiculo[] buffer;
    private int indiceInsercao;
    private int indiceRemocao;

    private final Semaphore vagas;
    private final Semaphore itens;
    private final Semaphore mutex;

    public EsteiraLoja(int capacidade) {
        this.capacidade = capacidade;
        this.buffer = new Veiculo[capacidade];
        this.indiceInsercao = 0;
        this.indiceRemocao = 0;

        this.vagas = new Semaphore(capacidade, true);
        this.itens = new Semaphore(0, true);
        this.mutex = new Semaphore(1, true);
    }

    public int inserir(Veiculo v) throws InterruptedException {
        vagas.acquire();
        int posicaoInserida;
        mutex.acquire();
        try {
            posicaoInserida = indiceInsercao;
            buffer[indiceInsercao] = v;
            indiceInsercao = (indiceInsercao + 1) % capacidade;
        } finally {
            mutex.release();
        }
        itens.release();
        return posicaoInserida;
    }

    public Veiculo remover() throws InterruptedException {
        itens.acquire();
        Veiculo v;
        mutex.acquire();
        try {
            v = buffer[indiceRemocao];
            buffer[indiceRemocao] = null;
            indiceRemocao = (indiceRemocao + 1) % capacidade;
        } finally {
            mutex.release();
        }
        vagas.release();
        return v;
    }

    public int getCapacidade() { return capacidade; }
    public int getOcupacao()   { return itens.availablePermits(); }
}
