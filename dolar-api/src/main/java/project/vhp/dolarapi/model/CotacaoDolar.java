package project.vhp.dolarapi.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cotacao_dolar")
public class CotacaoDolar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cotacao")
    private Long id;

    @Column(name = "valor_cotacao")
    private Double valorCotacao;

    @EqualsAndHashCode.Include
    @Column(name = "data_cotacao")
    private LocalDateTime dataCotacao;
}

