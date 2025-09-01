package br.edu.iff.ccc.projetoprogweb.entities;

public enum NivelDificuldade {
    FACIL,
    MODERADA,
    DIFICIL;
    
    public String getNomeFormatado() {
        switch (this) {
            case FACIL: return "Fácil";
            case MODERADA: return "Moderada";
            case DIFICIL: return "Difícil";
            default: return this.name();
        }
    }
}