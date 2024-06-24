import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { CalendarModule } from 'primeng/calendar';
import { TableModule } from 'primeng/table';

import { ButtonModule } from 'primeng/button';
import { CotacaoAtualDolarComponent } from './cotacao-atual-dolar/cotacao-atual-dolar.component';
import { DolarRoutingModule } from './dolar-routing.module';
import { ListagemCotacaoDolarComponent } from './listagem-cotacao-dolar/listagem-cotacao-dolar.component';
import { PaginaDolarComponent } from './pagina-dolar/pagina-dolar.component';

@NgModule({
    declarations: [
        CotacaoAtualDolarComponent,
        ListagemCotacaoDolarComponent,
        PaginaDolarComponent,
    ],
    imports: [
        CommonModule,
        FormsModule,

        CalendarModule,
        ButtonModule,
        TableModule,
        DolarRoutingModule,
    ],
    exports: [CotacaoAtualDolarComponent, ListagemCotacaoDolarComponent],
})
export class DolarModule {}
