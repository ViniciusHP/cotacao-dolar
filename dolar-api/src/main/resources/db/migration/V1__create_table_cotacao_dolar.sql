create table cotacao_dolar (
    id_cotacao bigserial not null,
    data_cotacao timestamp,
    valor_cotacao float8,
    primary key (id_cotacao)
);
