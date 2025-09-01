package br.edu.iff.ccc.projetoprogweb.config;

/**
 * Classe de constantes da aplicação.
 * Centraliza valores fixos usados em múltiplos lugares.
 * 
 * Por ser final e com construtor privado, não pode ser instanciada.
 * Todas as constantes são public static final.
 */
public final class AppConstants {
    
    // Construtor privado para prevenir instanciação
    private AppConstants() {
        throw new UnsupportedOperationException("Classe de utilitários não pode ser instanciada");
    }
    
    /**
     * ID do usuário demo utilizado durante desenvolvimento
     * Antes da implementação completa de autenticação
     */
    public static final Long USUARIO_DEMO_ID = 1L;
    
    /**
     * Nome do usuário demo para exibição
     */
    public static final String USUARIO_DEMO_NOME = "Usuário Demo";
    
    /**
     * Nome do autor demo para trilhas cadastradas
     */
    public static final String AUTOR_DEMO_NOME = "Trilheiro Demo";
}