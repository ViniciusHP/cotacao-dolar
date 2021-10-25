package project.vhp.dolarapi.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.vhp.dolarapi.dto.response.CotacaoDolarDTO;
import project.vhp.dolarapi.service.CotacaoService;

import java.time.LocalDate;

@RestController
@RequestMapping("/dolar")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DolarController {

    private final CotacaoService cotacaoService;

    @GetMapping("/atual")
    public ResponseEntity<CotacaoDolarDTO> cotacaoAtual() {
        return ResponseEntity.ok(cotacaoService.obterCotacaoAtual());
    }

    @GetMapping()
    public ResponseEntity<Page<CotacaoDolarDTO>> listagemPorPeriodo(
        @RequestParam(name = "data-inicial") @DateTimeFormat( pattern = "yyyy-MM-dd" ) LocalDate dataInicial,
        @RequestParam(name = "data-final") @DateTimeFormat( pattern = "yyyy-MM-dd" ) LocalDate dataFinal,
        Pageable pageable) {

        LocalDate temp = dataInicial;
        dataInicial = dataInicial.isBefore(dataFinal) ? dataInicial : dataFinal;
        dataFinal = dataFinal.isAfter(temp) ? dataFinal : temp;

        return ResponseEntity.ok(cotacaoService.listarCotacaoDoDolarNoPeriodo(dataInicial, dataFinal, pageable));
    }
}
