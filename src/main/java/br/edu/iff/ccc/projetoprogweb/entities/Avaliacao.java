package br.edu.iff.ccc.projetoprogweb.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "avaliacoes")
public class Avaliacao implements Serializable {

    private static final long serialVersionUID = 2L; 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    //Uma Trilha pode ter muitas Avaliacoes
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trilha_id", nullable = false) //cria a FK no banco
    private Trilha trilha; // Este campo é o "dono" do relacionamento //no caso as avaliações estao ligadas a uma trilha
    
    @Column(nullable = false)
    private Long usuarioId;
    
    private String usuarioNome;
    
    @Column(nullable = false)
    private int nota;
    
    @Column(length = 500) 
    private String comentario;
    
    @Column(nullable = false)
    private LocalDateTime dataAvaliacao;

    public Avaliacao() {}

    public Avaliacao(Trilha trilha, Long usuarioId, String usuarioNome, 
                    int nota, String comentario, LocalDateTime dataAvaliacao) {
        this.trilha = trilha;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.nota = nota;
        this.comentario = comentario;
        this.dataAvaliacao = dataAvaliacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Trilha getTrilha() { return trilha; }
    public void setTrilha(Trilha trilha) { this.trilha = trilha; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNome() { return usuarioNome; }
    public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }

    public int getNota() { return nota; }
    public void setNota(int nota) { this.nota = nota; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDateTime getDataAvaliacao() { return dataAvaliacao; }
    public void setDataAvaliacao(LocalDateTime dataAvaliacao) { this.dataAvaliacao = dataAvaliacao; }
}