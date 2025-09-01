package br.edu.iff.ccc.projetoprogweb.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity 
@Table(name = "trilhas") 
public class Trilha implements Serializable {
        //Permite que instâncias das suas entidades (Trilha, Avaliacao) sejam transformadas em formato binário para:
        //Armazenamento em banco, cache em memoria, etc

    private static final long serialVersionUID = 1L;

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório") 
    @Column(nullable = false, length = 100) 
    private String nome;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Column(nullable = false, length = 1000)
    private String descricao;
    
    @NotBlank
    @Column(nullable = false, length = 50)
    private String localizacao;
    
    @Enumerated(EnumType.STRING) 
    @Column(nullable = false, length = 20)
    private NivelDificuldade dificuldade;
    
    private Long autorId; 
    
    @Column(length = 50)
    private String autorNome;
    
    @Column(length = 500)
    private String dicasSeguranca;
    
    @Column(length = 100)
    private String melhorEpoca;
    
    @Min(3)
    private Double distancia;
    
    @Min(30)
    private Integer duracaoEstimada;
    
    //uma trilha tem muitas avaliações 
    @OneToMany(mappedBy = "trilha", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // ← Aponta para o campo 'trilha' na Avaliacao
    private List<Avaliacao> avaliacoes = new ArrayList<>(); 
   // Já a List<Avaliacao> representa o lado inverso do relacionamento OneToMany, 
    //permitindo acesso às avaliações a partir da trilha.

    public Trilha() {}

    public Trilha(Long id, String nome, String descricao, String localizacao, 
                 NivelDificuldade dificuldade, Long autorId, String autorNome) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.dificuldade = dificuldade;
        this.autorId = autorId;
        this.autorNome = autorNome;
    }

    public void adicionarAvaliacao(Avaliacao avaliacao) {
    // 1. Adiciona a avaliação na lista desta trilha (lado "um")
    avaliacoes.add(avaliacao);
    
    // 2. Define esta trilha como dona da avaliação (lado "muitos")
    avaliacao.setTrilha(this);
}

/**
 * @implNote Importante para evitar referências órfãs e manter
 *           a integridade do relacionamento
 */
    public void removerAvaliacao(Avaliacao avaliacao) {
    // 1. Remove a avaliação da lista desta trilha (lado "um")
    avaliacoes.remove(avaliacao);
    
    // 2. Remove a referência desta trilha na avaliação (lado "muitos")
    avaliacao.setTrilha(null);
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

    public String getDicasSeguranca() { return dicasSeguranca; }
    public void setDicasSeguranca(String dicasSeguranca) { this.dicasSeguranca = dicasSeguranca; }

    public String getMelhorEpoca() { return melhorEpoca; }
    public void setMelhorEpoca(String melhorEpoca) { this.melhorEpoca = melhorEpoca; }

    public Double getDistancia() { return distancia; }
    public void setDistancia(Double distancia) { this.distancia = distancia; }

    public Integer getDuracaoEstimada() { return duracaoEstimada; }
    public void setDuracaoEstimada(Integer duracaoEstimada) { this.duracaoEstimada = duracaoEstimada; }

    public List<Avaliacao> getAvaliacoes() { return avaliacoes; }
    public void setAvaliacoes(List<Avaliacao> avaliacoes) { this.avaliacoes = avaliacoes; }
}