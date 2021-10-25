package project.vhp.dolarapi.schedule;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.vhp.dolarapi.service.AwesomeApiService;
import project.vhp.dolarapi.service.CotacaoService;

/**
 * Classe responsável por obter o valor atual da cotação do dólar a cada 1 minuto.
 */
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ScheduledCotacao {

    private final CotacaoService cotacaoService;
    private final AwesomeApiService awesomeApiService;

    /**
     * Método que executa a cotação a cada 1 minuto.
     */
    @Scheduled(fixedDelay = 1000 * 60)
    public void obterCotacaoAtual() {
        cotacaoService.salvarCotacaoAtual(awesomeApiService.obterCotacaoAtual());
    }
}

