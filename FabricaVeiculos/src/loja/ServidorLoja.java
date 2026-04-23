package loja;

import common.IFabrica;
import common.ILoja;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorLoja {

    public static final int PORTA_RMI_PADRAO = 1099;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Uso: java loja.ServidorLoja <idLoja> [hostFabrica] [portaRmi]");
            System.err.println("Exemplo: java loja.ServidorLoja 1 localhost 1099");
            System.exit(1);
        }

        int idLoja;
        try {
            idLoja = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("idLoja deve ser um inteiro. Recebido: " + args[0]);
            System.exit(1);
            return;
        }

        String hostFabrica = (args.length >= 2) ? args[1] : "localhost";
        int portaRmi = (args.length >= 3) ? Integer.parseInt(args[2]) : PORTA_RMI_PADRAO;

        try {
            Registry registry = LocateRegistry.getRegistry(hostFabrica, portaRmi);
            IFabrica fabrica = (IFabrica) registry.lookup(IFabrica.NOME_SERVICO_RMI);
            System.out.println("[LOJA " + idLoja + "] Conectada à fábrica em " + hostFabrica + ":" + portaRmi);

            Loja loja = new Loja(idLoja, fabrica);

            String nomeServico = ILoja.PREFIXO_SERVICO_RMI + idLoja;
            registry.rebind(nomeServico, loja);
            System.out.println("[LOJA " + idLoja + "] Registrada no RMI como '" + nomeServico + "'");

            loja.iniciar();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("\n[LOJA " + idLoja + "] Encerrando...");
                    loja.parar();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }));

            System.out.println("[LOJA " + idLoja + "] Pressione Ctrl+C para encerrar.");

        } catch (Exception e) {
            System.err.println("[LOJA " + idLoja + "] Erro fatal: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
