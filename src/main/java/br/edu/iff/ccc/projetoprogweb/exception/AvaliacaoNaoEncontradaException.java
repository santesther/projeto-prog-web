package br.edu.iff.ccc.projetoprogweb.exception;

public class AvaliacaoNaoEncontradaException extends RuntimeException {
    
    public AvaliacaoNaoEncontradaException(String message) {
        super(message);
    }
    
    public AvaliacaoNaoEncontradaException(Long id) {
        super("Avaliação com ID " + id + " não foi encontrada");
    }
}
