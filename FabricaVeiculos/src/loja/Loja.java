package loja;

import common.IFabrica;
import common.ILoja;
import common.LogVenda;
import common.Logger;
import common.Veiculo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Loja extends UnicastRemoteObject implements ILoja {

    public static final int CAPACIDADE_ESTEIRA_LOJA = 15;
    private static final long INTERVALO_REPOSICAO_MS = 50;

    private final int id;
    private final IFabrica fabrica;
    private final EsteiraLoja esteira;

    private final Logger loggerRecebimento;
    private final Logger loggerVendaCliente;

    private Thread threadReposicao;
    private volatile boolean ativa;

    public Loja(int id, IFabrica fabrica) throws RemoteException {
        super();
        this.id = id;
        this.fabrica = fabrica;
        this.esteira = new EsteiraLoja(CAPACIDADE_ESTEIRA_LOJA);

        this.loggerRecebimento   = new Logger("logs/loja_" + id + "_recebimento.log", true);
        this.loggerVendaCliente  = new Logger("logs/loja_" + id + "_venda_cliente.log", true);

        this.ativa = true;
    }

    public void iniciar() {
        threadReposicao = new Thread(this::loopReposicao, "Loja-" + id + "-Reposicao");
        threadReposicao.start();
        System.out.println("[LOJA " + id + "] Iniciada com esteira de " + CAPACIDADE_ESTEIRA_LOJA + " vagas.");
    }

    public void parar() throws InterruptedException {
        ativa = false;
        if (threadReposicao != null) {
            threadReposicao.interrupt();
            threadReposicao.join();
        }
        System.out.println("[LOJA " + id + "] Encerrada.");
    }

    private void loopReposicao() {
        try {
            while (ativa && !Thread.currentThread().isInterrupted()) {
                try {
                    Veiculo v = fabrica.comprarVeiculo(id);
                    int posicao = esteira.inserir(v);

                    LogVenda log = new LogVenda(
                            LogVenda.Tipo.FABRICA_PARA_LOJA,
                            v,
                            id,
                            posicao
                    );
                    loggerRecebimento.registrar(log);

                    Thread.sleep(INTERVALO_REPOSICAO_MS);
                } catch (RemoteException e) {
                    System.err.println("[LOJA " + id + "] Erro RMI na compra: " + e.getMessage());
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Veiculo comprarVeiculo(int idCliente) throws RemoteException {
        try {
            Veiculo v = esteira.remover();
            LogVenda log = new LogVenda(
                    LogVenda.Tipo.LOJA_PARA_CLIENTE,
                    v,
                    idCliente,
                    -1
            );
            loggerVendaCliente.registrar(log);
            return v;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RemoteException("Compra pelo cliente " + idCliente + " foi interrompida", e);
        }
    }

    @Override
    public int getId() throws RemoteException {
        return id;
    }

    @Override
    public boolean estaAtiva() throws RemoteException {
        return ativa;
    }
}
