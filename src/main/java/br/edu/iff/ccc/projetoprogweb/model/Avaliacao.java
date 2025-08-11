package br.edu.iff.ccc.projetoprogweb.model;

import java.time.LocalDateTime;

public class Avaliacao {
    private Long id;
    private Long trilhaId;
    private Long usuarioId;
    private String usuarioNome;
    private int nota;
    private String comentario;
    private LocalDateTime dataAvaliacao;

    public Avaliacao() {}

    public Avaliacao(Long id, Long trilhaId, Long usuarioId, String usuarioNome, int nota, String comentario, LocalDateTime dataAvaliacao) {
        this.id = id;
        this.trilhaId = trilhaId;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.nota = nota;
        this.comentario = comentario;
        this.dataAvaliacao = dataAvaliacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTrilhaId() { return trilhaId; }
    public void setTrilhaId(Long trilhaId) { this.trilhaId = trilhaId; }

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
