package br.edu.iff.ccc.projetoprogweb.dto;

import br.edu.iff.ccc.projetoprogweb.entities.NivelDificuldade;
import br.edu.iff.ccc.projetoprogweb.entities.Trilha;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;

/**
 * - Retorna dados de uma trilha incluindo todas as avaliações
 * - ex: GET /api/v1/trilhas/{id}
 * - Diferente do TrilhaDTO simples que não inclui avaliações

 * 1. Referência Circular JSON:
 *    - Trilha tem List<Avaliacao>
 *    - Avaliacao tem Trilha
 *    - JSON tentaria serializar infinitamente: Trilha -> Avaliacao -> Trilha -> Avaliacao...
 *    - Resultado: JSON infinito
 */
@Schema(description = "Dados completos de uma trilha incluindo suas avaliações")
public class TrilhaComAvaliacoesDTO {

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
    
    /**
     * Lista de avaliações convertidas para AvaliacaoDTO
     * - AvaliacaoDTO NÃO tem referência de volta para Trilha completa
     * - Apenas tem trilhaId e trilhaNome (dados simples)
     * - Isso quebra o ciclo de referência circular
     */
    @Schema(description = "Lista de avaliações da trilha")
    private List<AvaliacaoDTO> avaliacoes;
    
    @Schema(description = "Nota média das avaliações", example = "4.2")
    private Double notaMedia;

    public TrilhaComAvaliacoesDTO() {}

    public TrilhaComAvaliacoesDTO(Trilha trilha) {
        // Copia todos os campos básicos da entidade Trilha
        // Estes são dados simples (primitivos, Strings, Enums) - sem problemas de serialização
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
        
        // Converte List<Avaliacao> para List<AvaliacaoDTO>
        // Verificação null-safe: se trilha não tem avaliações, evita NullPointerException
        if (trilha.getAvaliacoes() != null) {
            
            /**
             * Stream API para converter cada Avaliacao em AvaliacaoDTO:
             * 
             * trilha.getAvaliacoes()           → Retorna List<Avaliacao> do banco
             *   .stream()                      → Converte List em Stream para processamento funcional
             *   .map(AvaliacaoDTO::new)        → Para cada Avaliacao, cria novo AvaliacaoDTO
             *                                     (chama construtor AvaliacaoDTO(Avaliacao))
             *   .collect(Collectors.toList())  → Coleta resultados em nova List<AvaliacaoDTO>
             * 
             *AvaliacaoDTO NÃO tem objeto Trilha completo dentro
             * - Tem apenas trilhaId (Long) e trilhaNome (String)
             * - Isso quebra o ciclo: Trilha -> Avaliacao -> Trilha -> ...
             */
            this.avaliacoes = trilha.getAvaliacoes().stream()
                .map(AvaliacaoDTO::new)  // Equivalente a: .map(av -> new AvaliacaoDTO(av))
                .collect(Collectors.toList());
            
            // Calcula nota média das avaliações
            this.notaMedia = trilha.getAvaliacoes().stream()
                .mapToInt(av -> av.getNota())
                .average()
                .orElse(0.0);
                
        } else {
            // Se trilha não tem avaliações, inicializa com lista vazia e nota 0
            this.avaliacoes = List.of();  // List.of() cria lista imutável vazia
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

    public List<AvaliacaoDTO> getAvaliacoes() { return avaliacoes; }
    public void setAvaliacoes(List<AvaliacaoDTO> avaliacoes) { this.avaliacoes = avaliacoes; }

    public Double getNotaMedia() { return notaMedia; }
    public void setNotaMedia(Double notaMedia) { this.notaMedia = notaMedia; }
}
