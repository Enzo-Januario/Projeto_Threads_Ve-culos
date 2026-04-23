package common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogVenda implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public enum Tipo { FABRICA_PARA_LOJA, LOJA_PARA_CLIENTE }

    private final LocalDateTime timestamp;
    private final Tipo tipoVenda;
    private final Veiculo veiculo;
    private final int idComprador;
    private final int posicaoEsteiraDestino;

    public LogVenda(Tipo tipoVenda, Veiculo veiculo, int idComprador, int posicaoEsteiraDestino) {
        this.timestamp = LocalDateTime.now();
        this.tipoVenda = tipoVenda;
        this.veiculo = veiculo;
        this.idComprador = idComprador;
        this.posicaoEsteiraDestino = posicaoEsteiraDestino;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public Tipo getTipoVenda() { return tipoVenda; }
    public Veiculo getVeiculo() { return veiculo; }
    public int getIdComprador() { return idComprador; }
    public int getPosicaoEsteiraDestino() { return posicaoEsteiraDestino; }

    @Override
    public String toString() {
        String rotulo = (tipoVenda == Tipo.FABRICA_PARA_LOJA) ? "VENDA_FABRICA_LOJA" : "VENDA_LOJA_CLIENTE";
        String tipoComprador = (tipoVenda == Tipo.FABRICA_PARA_LOJA) ? "lojaId" : "clienteId";
        String campoPosicao = (tipoVenda == Tipo.FABRICA_PARA_LOJA) ? "posEsteiraLoja" : "posGaragemCliente";

        return "[" + FMT.format(timestamp) + "] " + rotulo + " | "
             + "veiculoId=" + veiculo.getId()
             + " | cor=" + veiculo.getCor()
             + " | tipo=" + veiculo.getTipo()
             + " | estacaoId=" + veiculo.getIdEstacao()
             + " | funcionarioId=" + veiculo.getIdFuncionario()
             + " | posEsteiraFabrica=" + veiculo.getPosicaoEsteiraFabrica()
             + " | " + tipoComprador + "=" + idComprador
             + " | " + campoPosicao + "=" + posicaoEsteiraDestino;
    }
}
