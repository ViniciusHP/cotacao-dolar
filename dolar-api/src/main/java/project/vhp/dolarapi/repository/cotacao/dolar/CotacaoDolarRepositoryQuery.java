package project.vhp.dolarapi.repository.cotacao.dolar;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.vhp.dolarapi.model.CotacaoDolar;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CotacaoDolarRepositoryQuery {

    /**
     * Obtém uma lista contendo todas as cotações durante o período especificado.
     * @param dataInicial data inicial do período.
     * @param dataFinal data final do período.
     * @return {@link List} contendo todas as cotações.
     */
    List<CotacaoDolar> listarTodasCotacoesNoPeriodo(LocalDate dataInicial, LocalDate dataFinal);

    /**
     * Obtém uma lista contendo as cotações durante o período especificado, com paginação.
     * @param dataInicial data inicial do período.
     * @param dataFinal data final do período.
     * @param pageable objeto com as informações de paginação.
     * @return {@link Page} contendo as cotações com paginação.
     */
    Page<CotacaoDolar> listarTodasCotacoesNoPeriodoComPaginacao(LocalDate dataInicial, LocalDate dataFinal, Pageable pageable);

    /**
     * Obtém uma lista de datas presentes no banco de dados em determinado período.
     * @param dataInicial data inicial do período.
     * @param dataFinal data final do período.
     * @return {@link List} contendo as datas presentes no banco de dados.
     */
    List<LocalDate> listarDatasPresentesNoPeriodo(LocalDate dataInicial, LocalDate dataFinal);

    /**
     * Obtém a cotação atual.
     * @return {@link Optional} com o valor da cotação atual.
     */
    Optional<CotacaoDolar> obterCotacaoAtual();

    /**
     * Obtém o número de registros de cotações em um determinado período.
     * @param dataInicial data inicial do período.
     * @param dataFinal data final do período.
     * @return número de registros.
     */
    Long contarTodasCotacoesNoPeriodo(LocalDate dataInicial, LocalDate dataFinal);
}
