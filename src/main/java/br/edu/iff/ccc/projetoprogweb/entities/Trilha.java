package br.edu.iff.ccc.projetoprogweb.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "trilhas")
public class Trilha {

    @Id
    @NotNull
    private Long id;
    
    @NotEmpty
    private String nome;
    
    @NotEmpty
    private String descricao;
    
    @NotEmpty
    private String localizacao;
    
    @Enumerated(EnumType.STRING)
    private NivelDificuldade dificuldade;
    
    private Long autorId;
    private String autorNome;

    public Trilha() {}

    public Trilha(Long id, String nome, String descricao, String localizacao, NivelDificuldade dificuldade, Long autorId, String autorNome) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.dificuldade = dificuldade;
        this.autorId = autorId;
        this.autorNome = autorNome;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public NivelDificuldade getDificuldade() { return dificuldade; }
    public void setDificuldade(NivelDificuldade dificuldade) { this.dificuldade = dificuldade; }

    public Long getAutorId() { return autorId; }
    public void setAutorId(Long autorId) { this.autorId = autorId; }

    public String getAutorNome() { return autorNome; }
    public void setAutorNome(String autorNome) { this.autorNome = autorNome; }
}
