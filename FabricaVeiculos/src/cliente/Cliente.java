package cliente;

import common.ILoja;
import common.LogVenda;
import common.Logger;
import common.Veiculo;

import java.rmi.RemoteException;
import java.util.Random;

public class Cliente implements Runnable {

    private static final long INTERVALO_MIN_MS = 200;
    private static final long INTERVALO_MAX_MS = 800;
    private static final int  COMPRAS_POR_CLIENTE = 5;

    private final int id;
    private final ILoja[] lojas;
    private final Garagem garagem;
    private final Logger loggerCompras;
    private final Random random;
    private volatile boolean rodando;

    public Cliente(int id, ILoja[] lojas, Logger loggerCompras) {
        this.id = id;
        this.lojas = lojas;
        this.garagem = new Garagem(id);
        this.loggerCompras = loggerCompras;
        this.random = new Random(System.nanoTime() + id);
        this.rodando = true;
    }

    @Override
    public void run() {
        try {
            int compras = 0;
            while (rodando && !Thread.currentThread().isInterrupted() && compras < COMPRAS_POR_CLIENTE) {
                long espera = INTERVALO_MIN_MS + random.nextInt((int)(INTERVALO_MAX_MS - INTERVALO_MIN_MS));
                Thread.sleep(espera);

                int indiceLoja = random.nextInt(lojas.length);
                ILoja lojaEscolhida = lojas[indiceLoja];

                try {
                    int idLoja = lojaEscolhida.getId();
                    Veiculo v = lojaEscolhida.comprarVeiculo(id);
                    int posicaoGaragem = garagem.guardar(v);

                    LogVenda log = new LogVenda(
                            LogVenda.Tipo.LOJA_PARA_CLIENTE,
                            v,
                            id,
                            posicaoGaragem
                    );
                    loggerCompras.registrar(log);

                    compras++;
                } catch (RemoteException e) {
                    System.err.println("[CLIENTE " + id + "] Erro RMI: " + e.getMessage());
                    Thread.sleep(500);
                }
            }
            System.out.println("[CLIENTE " + id + "] Concluiu " + compras
                    + " compras. Veículos na garagem: " + garagem.getQuantidade());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void parar() {
        this.rodando = false;
    }

    public int getId() { return id; }
    public Garagem getGaragem() { return garagem; }
}
