package br.edu.iff.ccc.projetoprogweb.controller;

import br.edu.iff.ccc.projetoprogweb.model.TipoUsuario;
import br.edu.iff.ccc.projetoprogweb.model.Usuario;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;

@Component
@SessionScope
public class SessionAttributes implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuarioLogado;
    private boolean logado = false;

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public boolean isLogado() {
        return logado;
    }

    public void setLogado(boolean logado) {
        this.logado = logado;
    }

    public boolean isAdmin() {
        return logado && usuarioLogado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR;
    }

    public boolean isTrilheiro() {
        return logado && usuarioLogado.getTipoUsuario() == TipoUsuario.TRILHEIRO_EXPERIENTE;
    }

    public boolean isPodeGerenciar() {
        return isAdmin() || isTrilheiro();
    }

    public void clearSession() {
        this.usuarioLogado = null;
        this.logado = false;
    }
}
