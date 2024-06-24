import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { DateTime } from 'luxon';

import { environment } from 'src/environments/environment';
import { CotacaoDolar, CotacaoDolarDTO } from '../core/model/cotacao.model';
import { Page } from '../core/model/page.model';

/**
 * Filtro utilizado na listagem das cotações de um período.
 */
export interface CotacaoDolarFiltro {
    dataInicial: Date;
    dataFinal: Date;
    page: number;
    size: number;
}

@Injectable({
    providedIn: 'root',
})
export class CotacaoDolarService {
    private apiUrl: string;

    constructor(private httpClient: HttpClient) {
        this.apiUrl = environment.apiUrl;
    }

    /**
     * Obtém a cotação atual do dólar.
     * @returns Promise contendo o valor da cotação atual do dólar.
     */
    obterCotacaoDolarAtual(): Promise<CotacaoDolar> {
        return this.httpClient
            .get<CotacaoDolarDTO>(`${this.apiUrl}/dolar/atual`)
            .toPromise()
            .then((cotacaoDTO: CotacaoDolarDTO) =>
                this.converterCotacaoDolarDTOParaCotacaoDolar(cotacaoDTO)
            );
    }

    /**
     * Obtém lista de cotações do dólar no período especificado no filtro.
     * @param filtro Restrições contendo o período e informações de paginação
     * @returns Paginação com lista de cotações do dólar.
     */
    listarCotacoesDolarNoPeriodo(
        filtro: CotacaoDolarFiltro
    ): Promise<Page<CotacaoDolar>> {
        const params = this.construirParametrosDeRequisicao(filtro);
        return this.httpClient
            .get<Page<CotacaoDolarDTO>>(`${this.apiUrl}/dolar`, { params })
            .toPromise()
            .then((pageDTO: Page<CotacaoDolarDTO>) =>
                this.converterPageCotacaoDolarDTOParaPageCotacaoDolar(pageDTO)
            );
    }

    /**
     * Converte cotação do dólar do tipo DTO para cotação do dólar
     * com os tipos apropriados para suas propriedades.
     * @param cotacaoDTO Objeto de cotação do dólar do tipo DTO.
     * @returns
     */
    private converterCotacaoDolarDTOParaCotacaoDolar(
        cotacaoDTO: CotacaoDolarDTO
    ): CotacaoDolar {
        return {
            valor: Number(cotacaoDTO.valor),
            data: DateTime.fromFormat(
                cotacaoDTO.data,
                'dd/MM/yyyy - hh:mm:ss'
            ).toJSDate(),
            diferenca: Number(cotacaoDTO.diferenca),
        };
    }

    /**
     * Converte página com cotações do dólar do tipo DTO para página com cotações do
     * dólar com os tipos apropriados para suas propriedades.
     * @param pageDTO Página com cotações do dólar do tipo DTO.
     * @returns Página com cotações do dólar com os tipos apropriados para suas propriedades.
     */
    private converterPageCotacaoDolarDTOParaPageCotacaoDolar(
        pageDTO: Page<CotacaoDolarDTO>
    ): Page<CotacaoDolar> {
        const conteudo = pageDTO.content.map(
            this.converterCotacaoDolarDTOParaCotacaoDolar
        );
        return {
            content: conteudo,
            pageable: pageDTO.pageable,
            last: pageDTO.last,
            totalPages: pageDTO.totalPages,
            totalElements: pageDTO.totalElements,
            number: pageDTO.number,
            size: pageDTO.size,
            sort: pageDTO.sort,
            first: pageDTO.first,
            numberOfElements: pageDTO.numberOfElements,
            empty: pageDTO.empty,
        };
    }

    /**
     * Constrói os parâmetros de requisição a partir do filtro.
     * @param filtro Restrições contendo o período e informações de paginação.
     * @returns Parâmetros de requisição.
     */
    private construirParametrosDeRequisicao(
        filtro: CotacaoDolarFiltro
    ): HttpParams {
        return new HttpParams()
            .append(
                'data-inicial',
                DateTime.fromJSDate(filtro.dataInicial).toFormat('yyyy-MM-dd')
            )
            .append(
                'data-final',
                DateTime.fromJSDate(filtro.dataFinal).toFormat('yyyy-MM-dd')
            )
            .append('size', filtro.size)
            .append('page', filtro.page);
    }
}
