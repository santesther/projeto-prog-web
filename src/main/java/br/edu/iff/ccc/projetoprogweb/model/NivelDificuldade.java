package br.edu.iff.ccc.projetoprogweb.model;

public enum NivelDificuldade {
    FACIL("Fácil"),
    MODERADA("Moderada"),
    DIFICIL("Difícil");

    private final String descricao;

    NivelDificuldade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
