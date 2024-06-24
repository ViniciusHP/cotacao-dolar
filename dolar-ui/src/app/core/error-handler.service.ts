import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';

/**
 * Classe que recebe os erros de rede.
 */
@Injectable({
    providedIn: 'root',
})
export class ErrorHandlerService {
    constructor(private messageService: MessageService) {}

    /**
     * Exibe toast para o usuário com informações do erro.
     * @param error - Erro de rede que ocorreu.
     */
    handleError(error: HttpErrorResponse) {
        if (error.error.mensagem) {
            const mensagem = error.error.mensagem;
            this.messageService.add({ severity: 'error', detail: mensagem });
        } else {
            this.messageService.add({
                severity: 'error',
                detail: 'Erro ao se conectar com o servidor remoto.',
            });
        }
    }
}
