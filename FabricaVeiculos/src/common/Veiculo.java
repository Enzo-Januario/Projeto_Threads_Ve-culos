package common;

import java.io.Serializable;

public class Veiculo implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Cor { R, G, B }
    public enum Tipo { SUV, SEDAN }

    private final int id;
    private final Cor cor;
    private final Tipo tipo;
    private final int idEstacao;
    private final int idFuncionario;
    private final int posicaoEsteiraFabrica;

    public Veiculo(int id, Cor cor, Tipo tipo, int idEstacao, int idFuncionario, int posicaoEsteiraFabrica) {
        this.id = id;
        this.cor = cor;
        this.tipo = tipo;
        this.idEstacao = idEstacao;
        this.idFuncionario = idFuncionario;
        this.posicaoEsteiraFabrica = posicaoEsteiraFabrica;
    }

    public int getId() { return id; }
    public Cor getCor() { return cor; }
    public Tipo getTipo() { return tipo; }
    public int getIdEstacao() { return idEstacao; }
    public int getIdFuncionario() { return idFuncionario; }
    public int getPosicaoEsteiraFabrica() { return posicaoEsteiraFabrica; }

    @Override
    public String toString() {
        return "Veiculo[id=" + id + ", cor=" + cor + ", tipo=" + tipo
             + ", estacao=" + idEstacao + ", funcionario=" + idFuncionario
             + ", posFabrica=" + posicaoEsteiraFabrica + "]";
    }
}
