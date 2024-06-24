import { Component, OnInit } from '@angular/core';
import { ErrorHandlerService } from 'src/app/core/error-handler.service';
import { CotacaoDolar } from 'src/app/core/model/cotacao.model';
import { CotacaoDolarService } from './../cotacao-dolar.service';

@Component({
    selector: 'app-cotacao-atual-dolar',
    templateUrl: './cotacao-atual-dolar.component.html',
    styleUrls: ['./cotacao-atual-dolar.component.scss'],
})
export class CotacaoAtualDolarComponent implements OnInit {
    cotacaoDolarAtual?: CotacaoDolar;

    constructor(
        private cotacaoDolarService: CotacaoDolarService,
        private errorHandlerService: ErrorHandlerService
    ) {}

    ngOnInit(): void {
        this.atualizarCotacaoAtual();
        this.iniciarTemporizador();
    }

    /**
     * Atualiza a cotação do dólar para o valor atual.
     */
    atualizarCotacaoAtual(): void {
        this.cotacaoDolarService
            .obterCotacaoDolarAtual()
            .then((cotacaoAtual) => (this.cotacaoDolarAtual = cotacaoAtual))
            .catch((erro) => this.errorHandlerService.handleError(erro));
    }

    /**
     * Inicia temporizador para buscar a cotação atual a cada 1 minuto.
     */
    iniciarTemporizador() {
        setInterval(() => this.atualizarCotacaoAtual(), 1000 * 60);
    }
}
