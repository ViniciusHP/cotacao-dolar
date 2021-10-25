package project.vhp.dolarapi.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de responta da nossa API.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CotacaoDolarDTO {

    private String valor;
    private String data;
    private String diferenca;

    @JsonIgnore
    public void calcularDiferenca(String valorCotacaoAtual) {
        double diferencaDouble = Double.parseDouble(valor) - Double.parseDouble(valorCotacaoAtual);
        diferenca = String.format("%.2f", diferencaDouble);
    }
}
