import { Component } from '@angular/core';
import { NgForm, UntypedFormControl } from '@angular/forms';
import { TableLazyLoadEvent } from 'primeng/table';
import { ErrorHandlerService } from 'src/app/core/error-handler.service';
import { debounce } from 'src/app/core/helpers/debounce';
import { CotacaoDolar } from 'src/app/core/model/cotacao.model';
import { Page } from 'src/app/core/model/page.model';
import {
    CotacaoDolarFiltro,
    CotacaoDolarService,
} from '../cotacao-dolar.service';

@Component({
    selector: 'app-listagem-cotacao-dolar',
    templateUrl: './listagem-cotacao-dolar.component.html',
    styleUrls: ['./listagem-cotacao-dolar.component.scss'],
})
export class ListagemCotacaoDolarComponent {
    cotacaoDolarFiltro?: CotacaoDolarFiltro;
    listagemCotacaoDolar: Array<CotacaoDolar>;

    quantidadeTotalDeRegistros: number;

    numeroDeLinhasDeRegistros: number;
    carregandoRegistros: boolean;
    lazyLoadAtivado: boolean;

    constructor(
        private cotacaoDolarService: CotacaoDolarService,
        private errorHandlerService: ErrorHandlerService
    ) {
        this.listagemCotacaoDolar = [];

        this.quantidadeTotalDeRegistros = 0;
        this.numeroDeLinhasDeRegistros = 10;
        this.carregandoRegistros = false;
        this.lazyLoadAtivado = false;
    }

    /**
     * Executa a listagem das cotações a partir dos filtros.
     * @param formulario Formulário com os campos de filtro.
     */
    @debounce()
    pesquisar(formulario: NgForm): void {
        this.carregandoRegistros = true;
        this.cotacaoDolarFiltro = {
            dataInicial: formulario.value['data-inicial'],
            dataFinal: formulario.value['data-final'],
            page: 0,
            size: this.numeroDeLinhasDeRegistros,
        };

        this.lazyLoadAtivado = true;
        this.pesquisarCotacaoDoDolarNoPeriodo(this.cotacaoDolarFiltro);
    }

    /**
     * Executa a listagem das cotações de maneira tardia.
     * @param evento Evento de Lazy Load com informações de paginação.
     */
    @debounce()
    pesquisarComLazyLoad(evento: TableLazyLoadEvent): void {
        const numeroDaPagina =
            evento.first && evento.rows ? evento.first / evento.rows : 0;
        this.carregandoRegistros = true;

        if (this.cotacaoDolarFiltro == null)
            throw new Error(
                'Erro ao realizar lazy load da tabela de cotações por período. O filtro está nulo.'
            );

        this.cotacaoDolarFiltro.page = numeroDaPagina;
        this.pesquisarCotacaoDoDolarNoPeriodo(this.cotacaoDolarFiltro);
    }

    /**
     * Valida se um campo do formulário está inválido.
     * @param campo Campo que será verificado.
     * @returns true se o campo está inválido, false se o campos está válido.
     */
    isCampoInvalido(campo: UntypedFormControl): boolean {
        if (campo) {
            return campo.invalid && campo.dirty;
        }
        return false;
    }

    /**
     * Busca os valores das cotações em determinado período.
     * @param filtro Campos de filtro para executar a requisição.
     */
    private pesquisarCotacaoDoDolarNoPeriodo(filtro: CotacaoDolarFiltro) {
        this.cotacaoDolarService
            .listarCotacoesDolarNoPeriodo(filtro)
            .then((page: Page<CotacaoDolar>) => {
                this.quantidadeTotalDeRegistros = page.totalElements;
                this.listagemCotacaoDolar = page.content;
            })
            .catch((erro) => this.errorHandlerService.handleError(erro))
            .finally(() => (this.carregandoRegistros = false));
    }
}
