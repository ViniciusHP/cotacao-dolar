package project.vhp.dolarapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.vhp.dolarapi.model.CotacaoDolar;
import project.vhp.dolarapi.repository.cotacao.dolar.CotacaoDolarRepositoryQuery;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CotacaoDolarRepository extends JpaRepository<CotacaoDolar, Long>, CotacaoDolarRepositoryQuery {

    /**
     * Obtém a cotação a partir de uma data e tempo.
     * @param dataCotacao data e tempo.
     * @return Cotação na data epecificada.
     */
    Optional<CotacaoDolar> findByDataCotacao(LocalDateTime dataCotacao);
}
