package project.vhp.dolarapi.repository.cotacao.dolar;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.vhp.dolarapi.model.CotacaoDolar;
import project.vhp.dolarapi.model.CotacaoDolar_;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CotacaoDolarRepositoryImpl implements CotacaoDolarRepositoryQuery {

    private final EntityManager entityManager;
    private final Logger logger;

    @Autowired
    public CotacaoDolarRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.logger = LoggerFactory.getLogger(CotacaoDolarRepositoryImpl.class);
    }

    @Override
    public List<CotacaoDolar> listarTodasCotacoesNoPeriodo(LocalDate dataInicial, LocalDate dataFinal) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CotacaoDolar> criteria = builder.createQuery(CotacaoDolar.class);

        // Seleciona a cotação atual
        Root<CotacaoDolar> root = criteria.from(CotacaoDolar.class);

        // Onde a data está entre as datas fornecidas
        Predicate predicate = adicionarRestricaoDePeriodo(root, builder, dataInicial, dataFinal);

        // Ordenado pela data de maneira decrescente
        Order order = adicionarRestricaoDeOrdenacaoDecrescentePorData(root, builder);

        /*
            SQL Correspondente:

            SELECT * FROM cotacao_dolar
            WHERE data_cotacao BETWEEN :dataInicial AND :dataFinal
            ORDER BY data_cotacao DESC;
        */
        criteria.select(root)
                .where(predicate)
                .orderBy(order);

        TypedQuery<CotacaoDolar> typedQuery = entityManager.createQuery(criteria);
        return typedQuery.getResultList();
    }

    @Override
    public Page<CotacaoDolar> listarTodasCotacoesNoPeriodoComPaginacao(LocalDate dataInicial, LocalDate dataFinal, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CotacaoDolar> criteria = builder.createQuery(CotacaoDolar.class);

        // Seleciona a cotação atual
        Root<CotacaoDolar> root = criteria.from(CotacaoDolar.class);

        // Onde a data está entre as datas fornecidas
        Predicate predicate = adicionarRestricaoDePeriodo(root, builder, dataInicial, dataFinal);

        // Ordenado pela data de maneira decrescente
        Order order = adicionarRestricaoDeOrdenacaoDecrescentePorData(root, builder);

        /*
            SQL Correspondente:

            SELECT * FROM cotacao_dolar
            WHERE data_cotacao BETWEEN :dataInicial AND :dataFinal
            ORDER BY data_cotacao DESC
            LIMIT :pageable.getPageSize() OFFSET :pageable.getPageNumber() * :pageable.getPageSize();
        */
        criteria.select(root)
                .where(predicate)
                .orderBy(order);

        TypedQuery<CotacaoDolar> typedQuery = entityManager.createQuery(criteria);
        adicionarRestricoesDePaginacao(typedQuery, pageable);

        return new PageImpl<>(
                typedQuery.getResultList(),
                pageable,
                contarTodasCotacoesNoPeriodo(dataInicial, dataFinal)
        );
    }

    @Override
    public List<LocalDate> listarDatasPresentesNoPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        /*
            SQL Correspondente:

            SELECT to_date(to_char(data_cotacao, 'YYYY-MM-DD'), 'YYYY-MM-DD') FROM cotacao_dolar
            WHERE data_cotacao BETWEEN :dataInicial AND :dataFinal
            GROUP BY to_char(data_cotacao, 'YYYY-MM-DD');
         */
        CriteriaQuery<Date> criteria = builder.createQuery(Date.class);
        Root<CotacaoDolar> root = criteria.from(CotacaoDolar.class);

        // Esta função vai selecionar a data e hora e converter para string no formato 'YYYY-MM-DD'
        Expression<String> funcaoParaConverterDataParaString = builder.function("to_char",
                String.class,
                root.get(CotacaoDolar_.dataCotacao),
                builder.literal("YYYY-MM-DD"));

        // A partir da data em string no formato 'YYYY-MM-DD', ela será convertida para data
        Expression<Date> funcaoParaConverterStringParaData = builder.function("to_date",
                Date.class,
                funcaoParaConverterDataParaString,
                builder.literal("YYYY-MM-DD"));

        Predicate predicate = adicionarRestricaoDePeriodo(root, builder, dataInicial, dataFinal);

        criteria.select(funcaoParaConverterStringParaData)
                .where(predicate)
                .groupBy(funcaoParaConverterDataParaString);

        TypedQuery<Date> typedQuery = entityManager.createQuery(criteria);
        List<Date> datas = typedQuery.getResultList();

        return datas.stream()
                .map(Date::toLocalDate)
                .collect(Collectors.toList());
    }

    @Override
    public Long contarTodasCotacoesNoPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

        // Seleciona a cotação atual
        Root<CotacaoDolar> root = criteria.from(CotacaoDolar.class);

        // Conta os resultados
        Expression<Long> selecao = builder.count(root);

        // Onde a data está entre as datas fornecidas
        Predicate predicate = adicionarRestricaoDePeriodo(root, builder, dataInicial, dataFinal);

        /*
            SQL Correspondente:

            SELECT COUNT(*) FROM cotacao_dolar
            WHERE data_cotacao BETWEEN :dataInicial AND :dataFinal;
        */
        criteria.select(selecao)
                .where(predicate);

        TypedQuery<Long> typedQuery = entityManager.createQuery(criteria);

        return typedQuery.getSingleResult();
    }

    @Override
    public Optional<CotacaoDolar> obterCotacaoAtual() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CotacaoDolar> criteria = builder.createQuery(CotacaoDolar.class);

        // Seleciona a cotação atual
        Root<CotacaoDolar> root = criteria.from(CotacaoDolar.class);

        // Ordenado pela data de maneira decrescente
        Order order = adicionarRestricaoDeOrdenacaoDecrescentePorData(root, builder);

        /*
            SQL Correspondente:

            SELECT * FROM cotacao_dolar
            ORDER BY data_cotacao DESC
            LIMIT 1;
        */
        criteria.select(root)
                .orderBy(order);

        TypedQuery<CotacaoDolar> typedQuery = entityManager.createQuery(criteria);
        typedQuery.setFirstResult(0);
        typedQuery.setMaxResults(1);

        return obterResultadoComoOptional(typedQuery);
    }

    /**
     * Método responsável por adicionar a restrição de período.
     *
     * @param root        referência do tipo de entidade.
     * @param builder     componente utilizado para construir a restrição.
     * @param dataInicial data de início do período.
     * @param dataFinal   data de fim do período.
     * @return {@link Predicate} contendo as restrições.
     */
    private Predicate adicionarRestricaoDePeriodo(Root<CotacaoDolar> root, CriteriaBuilder builder, LocalDate dataInicial, LocalDate dataFinal) {
        return builder.between(
                root.get(CotacaoDolar_.dataCotacao),
                dataInicial.atTime(0, 0, 0, 0), // Meia noite da data inicial
                dataFinal.atTime(23, 59, 59, 999) // Um segundo antes da meia noite da data final
        );
    }

    /**
     * Método responsável por adicionar paginação dos resultados.
     *
     * @param typedQuery consulta tipada.
     * @param pageable   objeto que contém as informações para paginação.
     */
    private void adicionarRestricoesDePaginacao(TypedQuery<CotacaoDolar> typedQuery, Pageable pageable) {
        int tamanhoDaPagina = pageable.getPageSize();
        int indiceDeInicioDaPagina = pageable.getPageNumber() * tamanhoDaPagina;

        typedQuery.setFirstResult(indiceDeInicioDaPagina);
        typedQuery.setMaxResults(tamanhoDaPagina);
    }

    /**
     * Método responsável por adicionar a restrição de ordenação decrescente a partir da data de cotação.
     *
     * @param root    referência do tipo de entidade.
     * @param builder componente utilizado para construir a restrição.
     * @return {@link Order} com as restrições de ordenação.
     */
    private Order adicionarRestricaoDeOrdenacaoDecrescentePorData(Root<CotacaoDolar> root, CriteriaBuilder builder) {
        return builder.desc(root.get(CotacaoDolar_.dataCotacao));
    }

    /**
     * Obtém objeto {@link Optional} com o resultado da query.
     *
     * @param typedQuery Query com a consulta que pode retornar uma exceção por não obter resultado.
     * @param <T>        Tipo do dado que o {@link Optional} carrega.
     * @return {@link Optional} com o resultado da query.
     */
    private <T> Optional<T> obterResultadoComoOptional(TypedQuery<T> typedQuery) {
        Optional<T> optional = Optional.empty();
        try {
            optional = Optional.ofNullable(typedQuery.getSingleResult());
        } catch (NoResultException exception) {
            logger.error("Exceção ao obter entidade na query.");
        }
        return optional;
    }
}
