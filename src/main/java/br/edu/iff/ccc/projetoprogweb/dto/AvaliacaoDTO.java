package br.edu.iff.ccc.projetoprogweb.dto;

import br.edu.iff.ccc.projetoprogweb.entities.Avaliacao;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * Avaliacao → Trilha → List<Avaliacao> → Trilha → ... (loop infinito)
 */
@Schema(description = "Dados de uma avaliação para transferência")
public class AvaliacaoDTO {
    
    @Schema(description = "ID único da avaliação", example = "1")
    private Long id;
    
    // CAMPOS DA TRILHA - Apenas referências simples 
    @Schema(description = "ID da trilha avaliada", example = "1")
    private Long trilhaId;
    
    @Schema(description = "Nome da trilha avaliada", example = "Trilha do Pico da Neblina")
    private String trilhaNome;
    
    @Schema(description = "ID do usuário que fez a avaliação", example = "1")
    private Long usuarioId;
    
    @Schema(description = "Nome do usuário que fez a avaliação", example = "Maria Santos")
    private String usuarioNome;
    
    @Schema(description = "Nota da avaliação (1-5)", example = "4")
    private int nota;
    
    @Schema(description = "Comentário da avaliação")
    private String comentario;
    
    @Schema(description = "Data e hora da avaliação")
    private LocalDateTime dataAvaliacao;

    public AvaliacaoDTO() {}

    /**
    Converte Entidade para DTO
     * 1. Copia campos simples da avaliação
     * 2. Para trilha: extrai apenas ID e nome (não objeto completo)
     * 3. Usa operador ternário para verificação null-safe:
     *    - Se trilha != null: pega ID/nome
     *    - Se trilha == null: define como null
     * 4. Evita NullPointerException e referência circular
     */
    public AvaliacaoDTO(Avaliacao avaliacao) {
        this.id = avaliacao.getId();
        this.usuarioId = avaliacao.getUsuarioId();
        this.usuarioNome = avaliacao.getUsuarioNome();
        this.nota = avaliacao.getNota();
        this.comentario = avaliacao.getComentario();
        this.dataAvaliacao = avaliacao.getDataAvaliacao();
        
        // Extrai apenas ID e nome da trilha
        this.trilhaId = avaliacao.getTrilha() != null ? avaliacao.getTrilha().getId() : null;
        this.trilhaNome = avaliacao.getTrilha() != null ? avaliacao.getTrilha().getNome() : null;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTrilhaId() { return trilhaId; }
    public void setTrilhaId(Long trilhaId) { this.trilhaId = trilhaId; }

    public String getTrilhaNome() { return trilhaNome; }
    public void setTrilhaNome(String trilhaNome) { this.trilhaNome = trilhaNome; }

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
