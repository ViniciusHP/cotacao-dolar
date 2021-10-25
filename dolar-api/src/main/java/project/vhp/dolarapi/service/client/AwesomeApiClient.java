package project.vhp.dolarapi.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import project.vhp.dolarapi.dto.awesome.api.CotacaoDolarWrapperAwesomeApi;
import project.vhp.dolarapi.dto.awesome.api.CotacaoInfoDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Cliente HTTP para consumo da API do AwesomeAPI
 *
 * @see <a href="https://docs.awesomeapi.com.br/api-de-moedas">AwesomeAPI</a>
 */
@FeignClient(name = "awesomeApi", url = "https://economia.awesomeapi.com.br/")
public interface AwesomeApiClient {

    @RequestMapping( method = RequestMethod.GET, value = "last/USD-BRL")
    CotacaoDolarWrapperAwesomeApi ultimaCotacaoDolar();

    @RequestMapping( method = RequestMethod.GET, value = "/USD-BRL/{quantidade}?start_date={dataInicial}&end_date=${dataFinal}")
    List<CotacaoInfoDTO> listagemCotacoesDolarNoPeriodo(
            @PathVariable( value = "quantidade") Integer quantidade,
            @RequestParam(value = "dataInicial") @DateTimeFormat( pattern = "yyyyMMdd" ) LocalDate dataInicial,
            @RequestParam(value = "dataFinal") @DateTimeFormat( pattern = "yyyyMMdd" ) LocalDate dataFinal);
}
