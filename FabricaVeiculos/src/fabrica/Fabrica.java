package fabrica;

import common.IFabrica;
import common.Logger;
import common.Veiculo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.atomic.AtomicInteger;

public class Fabrica extends UnicastRemoteObject implements IFabrica {

    public static final int QUANTIDADE_ESTACOES = 4;
    public static final int CAPACIDADE_ESTEIRA_CIRCULAR = 40;

    private final EstoquePecas estoquePecas;
    private final EsteiraDistribuicao esteiraDistribuicao;
    private final EsteiraCircular esteiraCircular;
    private final Estacao[] estacoes;

    private final Logger loggerProducao;

    private final AtomicInteger contadorVeiculos;
    private volatile boolean ativa;

    public Fabrica() throws RemoteException {
        super();
        this.estoquePecas        = new EstoquePecas();
        this.esteiraDistribuicao = new EsteiraDistribuicao();
        this.esteiraCircular     = new EsteiraCircular(CAPACIDADE_ESTEIRA_CIRCULAR);

        this.loggerProducao      = new Logger("logs/producao.log", true);

        this.contadorVeiculos = new AtomicInteger(0);
        this.ativa = true;

        this.estacoes = new Estacao[QUANTIDADE_ESTACOES];
        for (int i = 0; i < QUANTIDADE_ESTACOES; i++) {
            estacoes[i] = new Estacao(i, this);
        }
    }

    public void iniciarProducao() {
        for (Estacao e : estacoes) {
            e.iniciar();
        }
        System.out.println("[FABRICA] Produção iniciada com " + QUANTIDADE_ESTACOES
                + " estações (" + (QUANTIDADE_ESTACOES * Estacao.FUNCIONARIOS_POR_ESTACAO) + " funcionários).");
    }

    public void pararProducao() throws InterruptedException {
        ativa = false;
        for (Estacao e : estacoes) {
            e.parar();
        }
        for (Estacao e : estacoes) {
            e.aguardarTermino();
        }
        System.out.println("[FABRICA] Produção encerrada.");
    }

    @Override
    public Veiculo comprarVeiculo(int idLoja) throws RemoteException {
        try {
            return esteiraCircular.remover();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RemoteException("Compra interrompida", e);
        }
    }

    @Override
    public boolean estaAtiva() throws RemoteException {
        return ativa;
    }

    public int proximoIdVeiculo() {
        return contadorVeiculos.incrementAndGet();
    }

    public EstoquePecas getEstoquePecas() { return estoquePecas; }
    public EsteiraDistribuicao getEsteiraDistribuicao() { return esteiraDistribuicao; }
    public EsteiraCircular getEsteiraCircular() { return esteiraCircular; }
    public Logger getLoggerProducao() { return loggerProducao; }
}
