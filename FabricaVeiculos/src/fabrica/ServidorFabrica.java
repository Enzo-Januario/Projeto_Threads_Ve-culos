package fabrica;

import common.IFabrica;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorFabrica {

    public static final int PORTA_RMI = 1099;

    public static void main(String[] args) {
        try {
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(PORTA_RMI);
                System.out.println("[FABRICA] RMI Registry criado na porta " + PORTA_RMI);
            } catch (Exception e) {
                registry = LocateRegistry.getRegistry(PORTA_RMI);
                System.out.println("[FABRICA] RMI Registry já existente na porta " + PORTA_RMI);
            }

            Fabrica fabrica = new Fabrica();
            registry.rebind(IFabrica.NOME_SERVICO_RMI, fabrica);
            System.out.println("[FABRICA] Registrada no RMI como '" + IFabrica.NOME_SERVICO_RMI + "'");

            fabrica.iniciarProducao();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("\n[FABRICA] Encerrando...");
                    fabrica.pararProducao();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }));

            System.out.println("[FABRICA] Pressione Ctrl+C para encerrar.");

        } catch (Exception e) {
            System.err.println("[FABRICA] Erro fatal: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
