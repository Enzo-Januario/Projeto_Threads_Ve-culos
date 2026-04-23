package common;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Semaphore;

public class Logger {

    private final String caminhoArquivo;
    private final Semaphore mutex;
    private final boolean imprimirConsole;

    public Logger(String caminhoArquivo, boolean imprimirConsole) {
        this.caminhoArquivo = caminhoArquivo;
        this.mutex = new Semaphore(1, true);
        this.imprimirConsole = imprimirConsole;
    }

    public void registrar(String linha) {
        try {
            mutex.acquire();
            try {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
                    bw.write(linha);
                    bw.newLine();
                }
                if (imprimirConsole) {
                    System.out.println(linha);
                }
            } catch (IOException e) {
                System.err.println("Erro ao gravar log: " + e.getMessage());
            } finally {
                mutex.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void registrar(Object objetoLog) {
        registrar(objetoLog.toString());
    }
}
