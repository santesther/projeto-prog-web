package br.edu.iff.ccc.projetoprogweb.exception;

import java.util.Map;

public class DadosInvalidosException extends RuntimeException {
    
    private final Map<String, String> errosCampos;
    
    public DadosInvalidosException(String message) {
        super(message);
        this.errosCampos = null;
    }
    
    public DadosInvalidosException(String message, Map<String, String> errosCampos) {
        super(message);
        this.errosCampos = errosCampos;
    }
    
    public Map<String, String> getErrosCampos() {
        return errosCampos;
    }
}
