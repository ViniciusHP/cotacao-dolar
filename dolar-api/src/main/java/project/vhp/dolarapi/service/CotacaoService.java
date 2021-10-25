package project.vhp.dolarapi.service;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.vhp.dolarapi.dto.response.CotacaoDolarDTO;
import project.vhp.dolarapi.exceptions.CotacaoDolarNotFoundException;
import project.vhp.dolarapi.mapper.CotacaoDolarMapper;
import project.vhp.dolarapi.model.CotacaoDolar;
import project.vhp.dolarapi.repository.CotacaoDolarRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CotacaoService {

    private final AwesomeApiService awesomeApiService;
    private final CotacaoDolarRepository repository;
    private final CotacaoDolarMapper mapper;

    private final List<LocalDate> datasASeremIgnoradas;
    private CotacaoDolarDTO cotacaoAtualDTO;

    @Autowired
    public CotacaoService(AwesomeApiService awesomeApiService, CotacaoDolarRepository repository, CotacaoDolarMapper mapper) {
        this.awesomeApiService = awesomeApiService;
        this.repository = repository;
        this.mapper = mapper;

        this.datasASeremIgnoradas = new ArrayList<>();
    }

    /**
     * Obtém a última cotação do dólar.
     * @return cotação do dólar.
     */
    public CotacaoDolarDTO obterCotacaoAtual() {
        if(cotacaoAtualDTO == null || cotacaoAtualDTO.getValor() == null){
            CotacaoDolar cotacaoDolar = repository.obterCotacaoAtual()
                    .orElseThrow(() -> new CotacaoDolarNotFoundException("A cotação do dólar não foi encontrada."));

            cotacaoAtualDTO = mapper.cotacaoDolarToCotacaoDolarDTO(cotacaoDolar);
        }
        return cotacaoAtualDTO;
    }

    /**
     * Listagem de cotações realizadas durante um período específico.
     * @param dataInicial data de início do período.
     * @param dataFinal data de término do período.
     * @param pageable dados para paginação.
     * @return {@link List} com as cotações do período.
     */
    public Page<CotacaoDolarDTO> listarCotacaoDoDolarNoPeriodo(LocalDate dataInicial, LocalDate dataFinal, Pageable pageable) {

        List<LocalDate> datasPresentesNoBancoDeDados = repository.listarDatasPresentesNoPeriodo(dataInicial, dataFinal);
        List<LocalDate> datasQueNaoEstaoNoBancoDeDados = listarDatasQueNaoEstaoNoBancoDeDados(dataInicial, dataFinal, datasPresentesNoBancoDeDados);
        datasQueNaoEstaoNoBancoDeDados = removerDatasASeremIgnoradas(datasQueNaoEstaoNoBancoDeDados);

        if(datasQueNaoEstaoNoBancoDeDados.size() > 0) {
            // Recebe os dados de cotações faltantes, remove as cotações que possuem as datas que não estão no período
            List<CotacaoDolar> listaCotacoesAwesomeApi = listarCotacaoDasDatasFaltantes(datasQueNaoEstaoNoBancoDeDados);
            listaCotacoesAwesomeApi = removerCotacoesComDatasQueNaoEstaoNoPeriodo(dataInicial, dataFinal, listaCotacoesAwesomeApi);

            // Remove as cotações que já estão no banco de dados e salva as cotações que não estão no banco de dados;
            List<CotacaoDolar> listaCotacoesBancoDeDados = repository.listarTodasCotacoesNoPeriodo(dataInicial, dataFinal);
            List<CotacaoDolar> listaCotacoesFaltantesNoBancoDeDados = new ArrayList<>(CollectionUtils.removeAll(listaCotacoesAwesomeApi, listaCotacoesBancoDeDados));
            salvarListaDeCotacoes(listaCotacoesFaltantesNoBancoDeDados);
        }

        Page<CotacaoDolar> page = repository.listarTodasCotacoesNoPeriodoComPaginacao(dataInicial, dataFinal, pageable);
        Page<CotacaoDolarDTO> pageDTO = mapper.pageCotacaoDolarToPageCotacaoDolarDTO(page);

        // Calcula a diferença entre o valor de Cotação atual e o valor do registro
        final String valorCotacaoAtual = obterCotacaoAtual().getValor();
        pageDTO.getContent().forEach((dto) -> dto.calcularDiferenca(valorCotacaoAtual));
        return pageDTO;
    }

    /**
     * Método utilizado para salvar uma única cotação vinda do {@link project.vhp.dolarapi.schedule.ScheduledCotacao}
     * @param cotacaoDolar cotação a ser salva.
     */
    public void salvarCotacaoAtual(CotacaoDolar cotacaoDolar) {
        Optional<CotacaoDolar> optionalCotacaoDolarEntity = repository.findByDataCotacao(cotacaoDolar.getDataCotacao());

        // Se essa cotação não estiver no banco de dados, ela será salva
        if(optionalCotacaoDolarEntity.isEmpty()){
            cotacaoDolar = repository.save(cotacaoDolar);
            cotacaoAtualDTO = mapper.cotacaoDolarToCotacaoDolarDTO(cotacaoDolar);
        }
    }

    /**
     * Salva uma lista de cotações no banco de dados
     * @param lista {@link List} a ser salva
     */
    private void salvarListaDeCotacoes(List<CotacaoDolar> lista) {
        if(lista.size() > 0) {
            repository.saveAllAndFlush(lista);
        }
    }

    /**
     * A partir de um periodo de datas, será realizada uma diferença entre este período e
     * as datas fornecidas através da lista.
     * @param dataInicial data de inicio do período.
     * @param dataFinal data de término do período.
     * @param listaDatasQueEstaoNoBancoDeDados lista contendo as datas que estão no banco de dados.
     * @return {@link List} com as datas faltantes do banco de dados.
     */
    private List<LocalDate> listarDatasQueNaoEstaoNoBancoDeDados(LocalDate dataInicial, LocalDate dataFinal, List<LocalDate> listaDatasQueEstaoNoBancoDeDados){
        // Listando datas entre o período
        List<LocalDate> listaDatasEntreInicioEFim = dataInicial.datesUntil(dataFinal.plusDays(1))
                .collect(Collectors.toList());
        Collection<LocalDate> datasQueNaoEstaoNoBancoDeDados = CollectionUtils.removeAll(listaDatasEntreInicioEFim, listaDatasQueEstaoNoBancoDeDados);
        return new ArrayList<>(datasQueNaoEstaoNoBancoDeDados);
    }

    /**
     * Obtém todas as cotações das datas fornecidas, utilizando a AwesomeAPI.
     * @param listaDeDatasQueNaoEstaoNoBancoDeDados {@link List} com as datas das cotações que são necessárias.
     * @return {@link List} com as cotações das datas fornecidas.
     */
    private List<CotacaoDolar> listarCotacaoDasDatasFaltantes(List<LocalDate> listaDeDatasQueNaoEstaoNoBancoDeDados) {
        List<CotacaoDolar> listaCotacoes = new ArrayList<>();
        Map<LocalDate, List<CotacaoDolar>> mapaDataECotacao = awesomeApiService.listarCotacaoNasDatas(listaDeDatasQueNaoEstaoNoBancoDeDados);

        for(Map.Entry<LocalDate, List<CotacaoDolar>> dataECotacao: mapaDataECotacao.entrySet()) {
            final LocalDate data = dataECotacao.getKey();

            List<CotacaoDolar> cotacaoChamada = dataECotacao.getValue();

            boolean isDataNaoPresente = cotacaoChamada.stream()
                    .noneMatch(cotacao -> cotacao.getDataCotacao().toLocalDate().isEqual(data));

            if(isDataNaoPresente) ignorarData(data);

            listaCotacoes.addAll(CollectionUtils.removeAll(cotacaoChamada, listaCotacoes));
        }

        return listaCotacoes;
    }

    /**
     * Remove as datas que estão fora do período especificado.
     * @param dataInicio data de inicio do período.
     * @param dataFim data de fim do período.
     * @param listaInicial lista contendo as datas.
     * @return {@link List} contendo apenas as datas presentes no período especificado.
     */
    private List<CotacaoDolar> removerCotacoesComDatasQueNaoEstaoNoPeriodo(final LocalDate dataInicio, final LocalDate dataFim, List<CotacaoDolar> listaInicial) {
        return listaInicial.stream()
                .filter(cotacao -> {
                    LocalDate data = cotacao.getDataCotacao().toLocalDate();

                    boolean isDataEntreOPeriodo = data.isAfter(dataInicio) && data.isBefore(dataFim);
                    boolean isDataNoLimiteSupeiorOuInferiorDoPeriodo = data.isEqual(dataInicio) || data.isEqual(dataFim);

                    return isDataEntreOPeriodo || isDataNoLimiteSupeiorOuInferiorDoPeriodo;
                })
                .collect(Collectors.toList());
    }

    /*
    *  Por conta da API ter lacunas entre os períodos de dias, as seguintes funções possuem a utilidade
    *  de ignorá-las após fazer uma primeira tentativa de buscá-las sem sucesso
    */

    /**
     * Adiciona data na lista de ignorar.
     * @param data data a ser ignorada.
     */
    private void ignorarData(LocalDate data) {
        // Apenas datas que estão antes do dia atual serão ignoradas
        if(data.isBefore(LocalDate.now())){
            datasASeremIgnoradas.add(data);
        }
    }

    /**
     * Remove as datas que devem ser ignoradas de uma lista.
     * @param datas lista que sofrerá remoção das datas que devem ser ignoradas.
     * @return {@link List} sem as datas que devem ser ignoradas.
     */
    private List<LocalDate> removerDatasASeremIgnoradas(List<LocalDate> datas) {
        return new ArrayList<>(CollectionUtils.removeAll(datas, datasASeremIgnoradas));
    }
}
