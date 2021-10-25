package project.vhp.dolarapi.dto.awesome.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de invólucro para objeto 'USDBRL' vindo da AwesomeAPI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotacaoDolarWrapperAwesomeApi {

    @JsonProperty("USDBRL")
    private CotacaoInfoDTO usdbrl;
}
