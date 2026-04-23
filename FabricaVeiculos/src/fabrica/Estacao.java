package fabrica;

public class Estacao {

    public static final int FUNCIONARIOS_POR_ESTACAO = 5;

    private final int id;
    private final Ferramenta[] ferramentas;
    private final Funcionario[] funcionarios;
    private final Thread[] threadsFuncionarios;

    public Estacao(int id, Fabrica fabrica) {
        this.id = id;

        this.ferramentas = new Ferramenta[FUNCIONARIOS_POR_ESTACAO];
        for (int i = 0; i < FUNCIONARIOS_POR_ESTACAO; i++) {
            ferramentas[i] = new Ferramenta(i);
        }

        this.funcionarios = new Funcionario[FUNCIONARIOS_POR_ESTACAO];
        this.threadsFuncionarios = new Thread[FUNCIONARIOS_POR_ESTACAO];

        for (int i = 0; i < FUNCIONARIOS_POR_ESTACAO; i++) {
            Ferramenta esquerda = ferramentas[i];
            Ferramenta direita  = ferramentas[(i + 1) % FUNCIONARIOS_POR_ESTACAO];
            funcionarios[i] = new Funcionario(i, this, esquerda, direita, fabrica);
            threadsFuncionarios[i] = new Thread(funcionarios[i], "Estacao-" + id + "-Func-" + i);
        }
    }

    public void iniciar() {
        for (Thread t : threadsFuncionarios) {
            t.start();
        }
    }

    public void parar() {
        for (Funcionario f : funcionarios) {
            f.parar();
        }
        for (Thread t : threadsFuncionarios) {
            t.interrupt();
        }
    }

    public void aguardarTermino() throws InterruptedException {
        for (Thread t : threadsFuncionarios) {
            t.join();
        }
    }

    public int getId() {
        return id;
    }

    public int getQuantidadeFuncionarios() {
        return FUNCIONARIOS_POR_ESTACAO;
    }
}
