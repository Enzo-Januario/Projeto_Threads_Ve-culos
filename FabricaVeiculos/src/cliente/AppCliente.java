package cliente;

import common.ILoja;
import common.Logger;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class AppCliente {

    public static final int QUANTIDADE_CLIENTES = 20;
    public static final int QUANTIDADE_LOJAS = 3;
    public static final int PORTA_RMI_PADRAO = 1099;

    public static void main(String[] args) {
        String hostFabrica = (args.length >= 1) ? args[0] : "localhost";
        int portaRmi      = (args.length >= 2) ? Integer.parseInt(args[1]) : PORTA_RMI_PADRAO;

        try {
            Registry registry = LocateRegistry.getRegistry(hostFabrica, portaRmi);

            ILoja[] lojas = new ILoja[QUANTIDADE_LOJAS];
            for (int i = 0; i < QUANTIDADE_LOJAS; i++) {
                String nomeServico = ILoja.PREFIXO_SERVICO_RMI + (i + 1);
                lojas[i] = (ILoja) registry.lookup(nomeServico);
                System.out.println("[APP] Conectado à " + nomeServico);
            }

            Logger loggerCompras = new Logger("logs/clientes_compras.log", true);

            List<Thread> threadsClientes = new ArrayList<>();
            List<Cliente> clientes = new ArrayList<>();

            for (int i = 1; i <= QUANTIDADE_CLIENTES; i++) {
                Cliente c = new Cliente(i, lojas, loggerCompras);
                Thread t = new Thread(c, "Cliente-" + i);
                clientes.add(c);
                threadsClientes.add(t);
                t.start();
            }

            System.out.println("[APP] " + QUANTIDADE_CLIENTES + " clientes iniciados.");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\n[APP] Encerrando clientes...");
                for (Cliente c : clientes) c.parar();
                for (Thread t : threadsClientes) t.interrupt();
            }));

            for (Thread t : threadsClientes) {
                t.join();
            }

            System.out.println("[APP] Todos os clientes finalizaram.");

        } catch (Exception e) {
            System.err.println("[APP] Erro fatal: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
