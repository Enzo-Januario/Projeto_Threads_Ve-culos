package common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogProducao implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private final LocalDateTime timestamp;
    private final Veiculo veiculo;

    public LogProducao(Veiculo veiculo) {
        this.timestamp = LocalDateTime.now();
        this.veiculo = veiculo;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public Veiculo getVeiculo() { return veiculo; }

    @Override
    public String toString() {
        return "[" + FMT.format(timestamp) + "] PRODUCAO | "
             + "veiculoId=" + veiculo.getId()
             + " | cor=" + veiculo.getCor()
             + " | tipo=" + veiculo.getTipo()
             + " | estacaoId=" + veiculo.getIdEstacao()
             + " | funcionarioId=" + veiculo.getIdFuncionario()
             + " | posEsteiraFabrica=" + veiculo.getPosicaoEsteiraFabrica();
    }
}
