package br.edu.iff.ccc.projetoprogweb.dto;

import br.edu.iff.ccc.projetoprogweb.entities.NivelDificuldade;
import br.edu.iff.ccc.projetoprogweb.entities.Trilha;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * PROBLEMA ORIGINAL:
 * Trilha → List<Avaliacao> → Trilha → List<Avaliacao> → ... (loop infinito)
 * 
 * SOLUÇÃO COM DTO:
 * 1. TrilhaDTO não inclui List<Avaliacao> completa
 * 2. Inclui apenas estatísticas (totalAvaliacoes, notaMedia)
 * 3. Quebra a cadeia de referências circulares
 * 4. JSON fica limpo e eficiente
 */
@Schema(description = "Dados de uma trilha para transferência")
public class TrilhaDTO {
    
    @Schema(description = "ID único da trilha", example = "1")
    private Long id;
    
    @Schema(description = "Nome da trilha", example = "Trilha do Pico da Neblina")
    private String nome;
    
    @Schema(description = "Descrição detalhada da trilha")
    private String descricao;
    
    @Schema(description = "Localização da trilha", example = "Parque Nacional do Pico da Neblina")
    private String localizacao;
    
    @Schema(description = "Nível de dificuldade", example = "DIFICIL")
    private NivelDificuldade dificuldade;
    
    @Schema(description = "ID do autor da trilha", example = "1")
    private Long autorId;
    
    @Schema(description = "Nome do autor da trilha", example = "João Silva")
    private String autorNome;
    
    @Schema(description = "Dicas de segurança para a trilha")
    private String dicasSeguranca;
    
    @Schema(description = "Melhor época para fazer a trilha", example = "Maio a Setembro")
    private String melhorEpoca;
    
    @Schema(description = "Distância total da trilha em km", example = "15.5")
    private Double distancia;
    
    @Schema(description = "Duração estimada em minutos", example = "480")
    private Integer duracaoEstimada;
    
    // CAMPOS CALCULADOS - Substituem a lista de avaliações
    @Schema(description = "Número total de avaliações", example = "25")
    private Long totalAvaliacoes;
    
    @Schema(description = "Nota média das avaliações", example = "4.2")
    private Double notaMedia;

    public TrilhaDTO() {}

    /**
     Converte Entidade para DTO
     * 1. Copia todos os campos simples da entidade
     * 2. Para avaliações: NÃO copia a lista 
     * 3. Calcula estatísticas usando Stream API:
     *    - size(): conta número de avaliações
     *    - mapToInt(): extrai notas como IntStream
     *    - average(): calcula média das notas
     *    - orElse(0.0): valor padrão se não há avaliações
     * 4. Resultado: DTO "flat" com estatísticas, sem referências circulares
     */
    public TrilhaDTO(Trilha trilha) {
        this.id = trilha.getId();
        this.nome = trilha.getNome();
        this.descricao = trilha.getDescricao();
        this.localizacao = trilha.getLocalizacao();
        this.dificuldade = trilha.getDificuldade();
        this.autorId = trilha.getAutorId();
        this.autorNome = trilha.getAutorNome();
        this.dicasSeguranca = trilha.getDicasSeguranca();
        this.melhorEpoca = trilha.getMelhorEpoca();
        this.distancia = trilha.getDistancia();
        this.duracaoEstimada = trilha.getDuracaoEstimada();
        
        // Calcula estatísticas das avaliações sem incluir a lista completa
        if (trilha.getAvaliacoes() != null && !trilha.getAvaliacoes().isEmpty()) {
            // Conta total de avaliações
            this.totalAvaliacoes = (long) trilha.getAvaliacoes().size();
            
            // Calcula média usando Stream API
            this.notaMedia = trilha.getAvaliacoes().stream()
                .mapToInt(av -> av.getNota()) // Extrai nota de cada avaliação
                .average() // Calcula média
                .orElse(0.0); // Valor padrão se stream vazio
        } else {
            // Trilha sem avaliações
            this.totalAvaliacoes = 0L;
            this.notaMedia = 0.0;
        }
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

    public Long getTotalAvaliacoes() { return totalAvaliacoes; }
    public void setTotalAvaliacoes(Long totalAvaliacoes) { this.totalAvaliacoes = totalAvaliacoes; }

    public Double getNotaMedia() { return notaMedia; }
    public void setNotaMedia(Double notaMedia) { this.notaMedia = notaMedia; }
}
