import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {CalendarModule} from 'primeng/calendar';
import {TableModule} from 'primeng/table';

import { CotacaoAtualDolarComponent } from './cotacao-atual-dolar/cotacao-atual-dolar.component';
import { ListagemCotacaoDolarComponent } from './listagem-cotacao-dolar/listagem-cotacao-dolar.component';
import { ButtonModule } from 'primeng/button';



@NgModule({
  declarations: [
    CotacaoAtualDolarComponent,
    ListagemCotacaoDolarComponent
  ],
  imports: [
    CommonModule,
    FormsModule,

    CalendarModule,
    ButtonModule,
    TableModule
  ],
  exports: [
    CotacaoAtualDolarComponent,
    ListagemCotacaoDolarComponent
  ]
})
export class DolarModule { }
