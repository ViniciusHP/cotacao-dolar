package project.vhp.dolarapi.service;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.vhp.dolarapi.dto.awesome.api.CotacaoDolarWrapperAwesomeApi;
import project.vhp.dolarapi.dto.awesome.api.CotacaoInfoDTO;
import project.vhp.dolarapi.mapper.CotacaoDolarMapper;
import project.vhp.dolarapi.model.CotacaoDolar;
import project.vhp.dolarapi.repository.CotacaoDolarRepository;
import project.vhp.dolarapi.service.client.AwesomeApiClient;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Serviço para obtenção dos dados de cotação do AwesomeAPI
 */
@Service
public class AwesomeApiService {

    private final AwesomeApiClient awesomeApiClient;
    private final CotacaoDolarMapper mapper;
    private final ExecutorService executorService;

    private final Logger logger;

    @Autowired
    public AwesomeApiService(AwesomeApiClient awesomeApiClient, CotacaoDolarRepository repository, CotacaoDolarMapper mapper) {
        this.awesomeApiClient = awesomeApiClient;
        this.mapper = mapper;

        this.executorService = Executors.newFixedThreadPool(3);
        this.logger = LoggerFactory.getLogger(AwesomeApiService.class);
    }

    /**
     * Obtém a cotação atual.
     *
     * @return {@link CotacaoDolar} atual.
     */
    public CotacaoDolar obterCotacaoAtual() {
        logger.info("Executando chamada remota para AwesomeAPI para obter última cotação...");
        CotacaoDolarWrapperAwesomeApi cotacaoDolarWrapperAwesomeApi = awesomeApiClient.ultimaCotacaoDolar();
        return mapper.cotacaoInfoDTOToCotacaoDolar(cotacaoDolarWrapperAwesomeApi.getUsdbrl());
    }

    /**
     * Executa listagem de cotações nas datas especificadas.
     *
     * @param listaDeDatas datas das cotações.
     * @return {@link Map} contendo as datas como chaves e as listagens das cotações como valores.
     */
    public Map<LocalDate, List<CotacaoDolar>> listarCotacaoNasDatas(List<LocalDate> listaDeDatas) {

        logger.info(String.format("Executando %d chamada(s) remota(s) para AwesomeAPI para listagem de cotações em determinado período...", listaDeDatas.size()));

        // Mapeia as datas e as chamadas assíncronas para API externa
        Map<LocalDate, Future<List<CotacaoDolar>>> mapaDataEChamada = new HashMap<>();
        for (LocalDate data : listaDeDatas) {
            Callable<List<CotacaoDolar>> chamada = () -> listarCotacaoDolarNoPeriodo(data, data);
            mapaDataEChamada.put(data, executorService.submit(chamada));
        }

        // Mapeia as datas e as listagens vindas das chamadas assíncronas
        Map<LocalDate, List<CotacaoDolar>> mapaDataECotacoes = new HashMap<>();
        for (Map.Entry<LocalDate, Future<List<CotacaoDolar>>> dataEChamada : mapaDataEChamada.entrySet()) {
            try {
                final LocalDate data = dataEChamada.getKey();
                List<CotacaoDolar> cotacaoChamada = dataEChamada.getValue().get();

                mapaDataECotacoes.put(data, cotacaoChamada);
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Erro ao executar chamada remota para AwesomeAPI.", e);
            }
        }

        return mapaDataECotacoes;
    }

    /**
     * Executa listagem de cotação no período especificado.
     *
     * @param dataInicial data de inicio do período.
     * @param dataFinal   data de fim do período.
     * @return {@link List} contendo os dados de cotação do período especificado.
     */
    public List<CotacaoDolar> listarCotacaoDolarNoPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        final int QUANTIDADE_MAXIMA_DE_REGISTROS_POR_CHAMADA = 360; // Número máximo retornado na API
        List<CotacaoInfoDTO> cotacaoAwesomeApiList = awesomeApiClient.listagemCotacoesDolarNoPeriodo(QUANTIDADE_MAXIMA_DE_REGISTROS_POR_CHAMADA, dataInicial, dataFinal);
        return mapper.cotacaoInfoDTOListToCotacaoDolarList(cotacaoAwesomeApiList);
    }

    /**
     * Método que finaliza o {@link ExecutorService}.
     */
    @PreDestroy
    private void closeExecutor() {
        try {
            logger.info("Finalizando ExecutorService...");
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
