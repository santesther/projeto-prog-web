package br.edu.iff.ccc.projetoprogweb.model;

public enum TipoUsuario {
    USUARIO("Usuário"),
    TRILHEIRO_EXPERIENTE("Trilheiro Experiente"),
    ADMINISTRADOR("Administrador");

    private final String descricao;

    TipoUsuario(String descricao) {
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
