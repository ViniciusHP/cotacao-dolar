import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { PrimeNGConfig } from 'primeng/api';
import { slideDownAndFadeIn } from './animations/animations';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
    animations: [slideDownAndFadeIn],
})
export class AppComponent {
    constructor(
        private config: PrimeNGConfig,
        private translateService: TranslateService
    ) {}

    ngOnInit() {
        const lingua = 'pt';
        this.translateService.setDefaultLang(lingua);
        this.traduzir(lingua);
    }

    /**
     * Faz a tradução do componente do primeng (Calendário) para o português
     * @param lang Nome da língua
     */
    traduzir(lang: string) {
        this.translateService.use(lang);
        this.translateService
            .get('primeng')
            .subscribe((res) => this.config.setTranslation(res));
    }
}
