package project.vhp.dolarapi.exceptionhandler;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import project.vhp.dolarapi.exceptions.CotacaoDolarNotFoundException;

@ControllerAdvice
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DolarApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String mensagem = obterMensagem("parametro.requisicao.obrigatorio", ex.getParameterName());
        Erro erro = Erro.builder()
                .mensagem(mensagem)
                .build();
        return handleExceptionInternal(ex, erro, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({CotacaoDolarNotFoundException.class})
    public ResponseEntity<Object> handleCotacaoDolarNotFound(CotacaoDolarNotFoundException ex, WebRequest request) {
        String mensagem = obterMensagem("cotacao.atual.nao-econtrada");
        Erro erro = Erro.builder()
                .mensagem(mensagem)
                .build();
        return handleExceptionInternal(ex, erro, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    private String obterMensagem(String codigo, Object... args) {
        return messageSource.getMessage(codigo, args, LocaleContextHolder.getLocale());
    }
}

