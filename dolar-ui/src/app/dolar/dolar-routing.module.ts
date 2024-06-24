import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PaginaDolarComponent } from './pagina-dolar/pagina-dolar.component';

const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        component: PaginaDolarComponent,
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class DolarRoutingModule {}
