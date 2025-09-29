package br.edu.iff.ccc.projetoprogweb.exception;

import br.edu.iff.ccc.projetoprogweb.service.AvaliacaoService.AvaliacaoInvalidaException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(annotations = RestController.class) 
public class ApiGlobalAdviceException extends ResponseEntityExceptionHandler {

    /*
     * GET /api/v1/trilhas/999999
     * RETORNA: ProblemDetail com status 404
     */
    @ExceptionHandler(TrilhaNaoEncontrada.class)
    public ResponseEntity<ProblemDetail> handleTrilhaNaoEncontrada(
            TrilhaNaoEncontrada ex, WebRequest request) {
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, 
            ex.getMessage()
        );
        
        problemDetail.setTitle("Trilha não encontrada");
        problemDetail.setType(URI.create("https://api.guia-trilhas.com/problems/trilha-nao-encontrada"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getDescription(false).replace("uri=", ""));
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    /**
     * GET /api/v1/avaliacoes/999999
     * RETORNA: ProblemDetail com status 404
     */
    @ExceptionHandler(AvaliacaoNaoEncontradaException.class)
    public ResponseEntity<ProblemDetail> handleAvaliacaoNaoEncontrada(
            AvaliacaoNaoEncontradaException ex, WebRequest request) {
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, 
            ex.getMessage()
        );
        
        problemDetail.setTitle("Avaliação não encontrada");
        problemDetail.setType(URI.create("https://api.guia-trilhas.com/problems/avaliacao-nao-encontrada"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getDescription(false).replace("uri=", ""));
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    /**
     * POST /api/v1/trilhas com campos inválidos
     * RETORNA: ProblemDetail com status 400 e detalhes dos campos inválidos
     */
    @ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handleDadosInvalidos(
            DadosInvalidosException ex, WebRequest request) {
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, 
            ex.getMessage()
        );
        
        problemDetail.setTitle("Dados inválidos");
        problemDetail.setType(URI.create("https://api.guia-trilhas.com/problems/dados-invalidos"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getDescription(false).replace("uri=", ""));
        
        if (ex.getErrosCampos() != null) {
            problemDetail.setProperty("fieldErrors", ex.getErrosCampos());
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    /**
      Tentar excluir trilha com avaliações dependentes
     * RETORNA: ProblemDetail com status 409
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, WebRequest request) {
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT, 
            "Violação de integridade de dados. Verifique se não há dependências ou dados duplicados."
        );
        
        problemDetail.setTitle("Violação de integridade de dados");
        problemDetail.setType(URI.create("https://api.guia-trilhas.com/problems/data-integrity-violation"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getDescription(false).replace("uri=", ""));
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    /**
     POST /api/v1/avaliacoes com nota fora do range 1-5
     * RETORNA: ProblemDetail com status 400
     */
    @ExceptionHandler(AvaliacaoInvalidaException.class)
    public ResponseEntity<ProblemDetail> handleAvaliacaoInvalida(
            AvaliacaoInvalidaException ex, WebRequest request) {
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, 
            ex.getMessage()
        );
        
        problemDetail.setTitle("Dados de avaliação inválidos");
        problemDetail.setType(URI.create("https://api.guia-trilhas.com/problems/avaliacao-invalida"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getDescription(false).replace("uri=", ""));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    /**
     *Tentar operação que viola regra de negócio
     * RETORNA: ProblemDetail com status 422
     */
    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ProblemDetail> handleRegraDeNegocio(
            RegraDeNegocioException ex, WebRequest request) {
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNPROCESSABLE_ENTITY, 
            ex.getMessage()
        );
        
        problemDetail.setTitle("Regra de negócio violada");
        problemDetail.setType(URI.create("https://api.guia-trilhas.com/problems/regra-de-negocio"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getDescription(false).replace("uri=", ""));
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(problemDetail);
    }

    /**
     POST /api/v1/trilhas com {"nome": "", "descricao": "abc"}
     * RETORNA: ProblemDetail com status 400 e detalhes dos campos inválidos
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, 
            HttpHeaders headers, 
            HttpStatusCode status, 
            WebRequest request) {
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, 
            "Dados fornecidos são inválidos"
        );
        
        problemDetail.setTitle("Erro de validação");
        problemDetail.setType(URI.create("https://api.guia-trilhas.com/problems/validation-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getDescription(false).replace("uri=", ""));
        
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        problemDetail.setProperty("fieldErrors", fieldErrors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    /**
     Passar parâmetro inválido para endpoint
     * RETORNA: ProblemDetail com status 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, 
            ex.getMessage()
        );
        
        problemDetail.setTitle("Argumento inválido");
        problemDetail.setType(URI.create("https://api.guia-trilhas.com/problems/illegal-argument"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getDescription(false).replace("uri=", ""));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    /**
     *Forçar erro interno no servidor
     * RETORNA: ProblemDetail com status 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(
            Exception ex, WebRequest request) {
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, 
            "Ocorreu um erro interno no servidor"
        );
        
        problemDetail.setTitle("Erro interno do servidor");
        problemDetail.setType(URI.create("https://api.guia-trilhas.com/problems/internal-server-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getDescription(false).replace("uri=", ""));
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}
