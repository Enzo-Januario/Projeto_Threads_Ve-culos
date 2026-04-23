package fabrica;

import common.LogProducao;
import common.Veiculo;

public class Funcionario implements Runnable {

    private static final long TEMPO_MONTAGEM_MS = 150;
    private static final long TEMPO_DESCANSO_MS = 80;

    private final int id;
    private final Estacao estacao;
    private final Ferramenta ferramentaEsquerda;
    private final Ferramenta ferramentaDireita;
    private final Fabrica fabrica;
    private volatile boolean rodando;

    public Funcionario(int id, Estacao estacao, Ferramenta esquerda, Ferramenta direita, Fabrica fabrica) {
        this.id = id;
        this.estacao = estacao;
        this.ferramentaEsquerda = esquerda;
        this.ferramentaDireita = direita;
        this.fabrica = fabrica;
        this.rodando = true;
    }

    @Override
    public void run() {
        try {
            while (rodando && !Thread.currentThread().isInterrupted()) {
                fabrica.getEstoquePecas().consumirPeca();
                fabrica.getEsteiraDistribuicao().solicitar();

                try {
                    pegarFerramentasComAssimetria();
                    try {
                        Veiculo v = montarVeiculo();
                        LogProducao log = new LogProducao(v);
                        fabrica.getLoggerProducao().registrar(log);
                    } finally {
                        soltarFerramentas();
                    }
                } finally {
                    fabrica.getEsteiraDistribuicao().liberar();
                }

                Thread.sleep(TEMPO_DESCANSO_MS);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void pegarFerramentasComAssimetria() throws InterruptedException {
        boolean ultimoFuncionario = (id == Estacao.FUNCIONARIOS_POR_ESTACAO - 1);
        if (ultimoFuncionario) {
            ferramentaDireita.pegar();
            ferramentaEsquerda.pegar();
        } else {
            ferramentaEsquerda.pegar();
            ferramentaDireita.pegar();
        }
    }

    private void soltarFerramentas() {
        ferramentaEsquerda.soltar();
        ferramentaDireita.soltar();
    }

    private Veiculo montarVeiculo() throws InterruptedException {
        Thread.sleep(TEMPO_MONTAGEM_MS);

        int idVeiculo = fabrica.proximoIdVeiculo();
        Veiculo.Cor cor   = Veiculo.Cor.values()[idVeiculo % Veiculo.Cor.values().length];
        Veiculo.Tipo tipo = Veiculo.Tipo.values()[idVeiculo % Veiculo.Tipo.values().length];

        int posicao = fabrica.getEsteiraCircular().reservarPosicao();
        Veiculo v = new Veiculo(idVeiculo, cor, tipo, estacao.getId(), id, posicao);
        fabrica.getEsteiraCircular().inserirNaPosicao(v, posicao);

        return v;
    }

    public void parar() {
        this.rodando = false;
    }

    public int getId() {
        return id;
    }
}
