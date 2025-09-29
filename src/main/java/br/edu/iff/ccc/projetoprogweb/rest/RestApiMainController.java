package br.edu.iff.ccc.projetoprogweb.rest;

import br.edu.iff.ccc.projetoprogweb.entities.Trilha;
import br.edu.iff.ccc.projetoprogweb.entities.Avaliacao;
import br.edu.iff.ccc.projetoprogweb.dto.TrilhaDTO;
import br.edu.iff.ccc.projetoprogweb.dto.AvaliacaoDTO;
import br.edu.iff.ccc.projetoprogweb.dto.TrilhaComAvaliacoesDTO;
import br.edu.iff.ccc.projetoprogweb.service.TrilhaService;
import br.edu.iff.ccc.projetoprogweb.service.AvaliacaoService;
import br.edu.iff.ccc.projetoprogweb.config.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/api/v1") //versionamento 
@Tag(name = "API Principal", description = "API REST completa para gerenciamento de trilhas e avaliações")
public class RestApiMainController {

    @Autowired
    private TrilhaService trilhaService;

    @Autowired
    private AvaliacaoService avaliacaoService;

   
    @Operation(summary = "Endpoint de verificação da API",
               description = "Retorna informações básicas sobre a API para verificar se está funcionando")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "API funcionando corretamente")
    })
    @GetMapping("/home")
    public ResponseEntity<Map<String, Object>> getApiHome() {
        Map<String, Object> info = Map.of(
            "nome", "API de Guia de Trilhas",
            "versao", "v1.0",
            "status", "ativo",
            "documentacao", "/swagger-ui/index.html"
        );
        return ResponseEntity.ok(info); 
    }

    @Operation(summary = "Retorna todas as trilhas", 
               description = "Lista todas as trilhas cadastradas no sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de trilhas retornada com sucesso")
    })
    @GetMapping("/trilhas") 
    public ResponseEntity<List<TrilhaDTO>> getAllTrilhas() {
        List<Trilha> trilhas = trilhaService.findAll();
        // Usa DTOs para evitar referências circulares 
        List<TrilhaDTO> trilhasDTO = trilhas.stream()
            .map(TrilhaDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(trilhasDTO); 
    }

    @Operation(summary = "Retorna uma trilha específica pelo ID",
               description = "Busca uma trilha específica utilizando seu identificador único")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Trilha encontrada"),
        @ApiResponse(responseCode = "404", description = "Trilha não encontrada") 
    })
    @GetMapping("/trilhas/{id}") 
    public ResponseEntity<TrilhaComAvaliacoesDTO> getTrilhaById(
            @Parameter(description = "ID da trilha", required = true) 
            @PathVariable Long id) {
        Trilha trilha = trilhaService.findById(id); // Lança exceção pelo globalAdvice se não encontrar 
        TrilhaComAvaliacoesDTO trilhaDTO = new TrilhaComAvaliacoesDTO(trilha);
        return ResponseEntity.ok(trilhaDTO); 
    }

    @Operation(summary = "Cria uma nova trilha",
               description = "Cadastra uma nova trilha no sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Trilha criada com sucesso"), 
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping("/trilhas") 
    public ResponseEntity<TrilhaDTO> createTrilha(
            @Parameter(description = "Dados da trilha a ser criada", required = true)
            @Valid @RequestBody Trilha trilha) { // @Valid ativa validação Bean Validation
        
        trilha.setId(null); // Garante que seja uma criação, não atualização
        Trilha novaTrilha = trilhaService.save(trilha);
        TrilhaDTO trilhaDTO = new TrilhaDTO(novaTrilha);
        
        // Cabeçalho Location com URI do recurso criado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaTrilha.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(trilhaDTO); 
    }

    @Operation(summary = "Atualiza uma trilha existente",
               description = "Atualiza completamente os dados de uma trilha existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Trilha atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Trilha não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/trilhas/{id}") 
    public ResponseEntity<TrilhaDTO> updateTrilha(
            @Parameter(description = "ID da trilha", required = true)
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados da trilha", required = true)
            @Valid @RequestBody Trilha trilha) {
        
        // Verifica se a trilha existe (lança exceção se não encontrar)
        trilhaService.findById(id);
        
        // Garante que o ID seja mantido
        trilha.setId(id);
        Trilha trilhaAtualizada = trilhaService.save(trilha);
        TrilhaDTO trilhaDTO = new TrilhaDTO(trilhaAtualizada);
        
        return ResponseEntity.ok(trilhaDTO); 
    }

    @Operation(summary = "Atualiza parcialmente uma trilha",
               description = "Atualiza apenas os campos fornecidos de uma trilha existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Trilha atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Trilha não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PatchMapping("/trilhas/{id}") 
    public ResponseEntity<TrilhaDTO> patchTrilha(
            @Parameter(description = "ID da trilha", required = true)
            @PathVariable Long id,
            @Parameter(description = "Dados parciais para atualização", required = true)
            @RequestBody Trilha trilhaParcial) {
        
        Trilha trilhaExistente = trilhaService.findById(id);
        
        // Atualiza apenas os campos não nulos 
        if (trilhaParcial.getNome() != null) {
            trilhaExistente.setNome(trilhaParcial.getNome());
        }
        if (trilhaParcial.getDescricao() != null) {
            trilhaExistente.setDescricao(trilhaParcial.getDescricao());
        }
        if (trilhaParcial.getLocalizacao() != null) {
            trilhaExistente.setLocalizacao(trilhaParcial.getLocalizacao());
        }
        if (trilhaParcial.getDificuldade() != null) {
            trilhaExistente.setDificuldade(trilhaParcial.getDificuldade());
        }
        if (trilhaParcial.getDistancia() != null) {
            trilhaExistente.setDistancia(trilhaParcial.getDistancia());
        }
        if (trilhaParcial.getDuracaoEstimada() != null) {
            trilhaExistente.setDuracaoEstimada(trilhaParcial.getDuracaoEstimada());
        }
        
        Trilha trilhaAtualizada = trilhaService.save(trilhaExistente);
        TrilhaDTO trilhaDTO = new TrilhaDTO(trilhaAtualizada);
        return ResponseEntity.ok(trilhaDTO);
    }

    @Operation(summary = "Exclui uma trilha",
               description = "Remove uma trilha do sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Trilha excluída com sucesso"), 
        @ApiResponse(responseCode = "404", description = "Trilha não encontrada")
    })
    @DeleteMapping("/trilhas/{id}") 
    public ResponseEntity<Void> deleteTrilha(
            @Parameter(description = "ID da trilha", required = true)
            @PathVariable Long id) {
        
        // Verifica se a trilha existe antes de excluir
        trilhaService.findById(id);
        trilhaService.deleteById(id);
        
        return ResponseEntity.noContent().build(); 
    }

    //consultas costumizadas

    @Operation(summary = "Busca trilhas por nome",
               description = "Retorna trilhas que contenham o termo de busca no nome")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Trilhas encontradas")
    })
    @GetMapping("/trilhas/buscar") 
    public ResponseEntity<List<TrilhaDTO>> buscarTrilhasPorNome(
            @Parameter(description = "Termo de busca para o nome da trilha", required = true)
            @RequestParam String nome) {
        // Usa consulta customizada do Repository
        List<Trilha> trilhas = trilhaService.findByNomeContaining(nome);
        List<TrilhaDTO> trilhasDTO = trilhas.stream()
            .map(TrilhaDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(trilhasDTO);
    }

   //consultas das avaliações
    @Operation(summary = "Retorna todas as avaliações",
               description = "Lista todas as avaliações cadastradas no sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de avaliações retornada com sucesso")
    })
    @GetMapping("/avaliacoes") 
    public ResponseEntity<List<AvaliacaoDTO>> getAllAvaliacoes() {
        List<Avaliacao> avaliacoes = avaliacaoService.findAll();
        List<AvaliacaoDTO> avaliacoesDTO = avaliacoes.stream()
            .map(AvaliacaoDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(avaliacoesDTO); 
    }

    @Operation(summary = "Retorna uma avaliação específica pelo ID",
               description = "Busca uma avaliação específica utilizando seu identificador único")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Avaliação encontrada"),
        @ApiResponse(responseCode = "404", description = "Avaliação não encontrada") 
    })
    @GetMapping("/avaliacoes/{id}") 
    public ResponseEntity<AvaliacaoDTO> getAvaliacaoById(
            @Parameter(description = "ID da avaliação", required = true) 
            @PathVariable Long id) {
        Avaliacao avaliacao = avaliacaoService.findById(id); // Lança exceção se não encontrar (tratada pelo GlobalAdvice)
        AvaliacaoDTO avaliacaoDTO = new AvaliacaoDTO(avaliacao);
        return ResponseEntity.ok(avaliacaoDTO); 
    }

    @Operation(summary = "Retorna avaliações de uma trilha específica",
               description = "Lista todas as avaliações de uma trilha específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Avaliações da trilha encontradas"),
        @ApiResponse(responseCode = "404", description = "Trilha não encontrada")
    })
    @GetMapping("/avaliacoes/trilha/{trilhaId}") 
    public ResponseEntity<List<AvaliacaoDTO>> getAvaliacoesByTrilha(
            @Parameter(description = "ID da trilha", required = true) 
            @PathVariable Long trilhaId) {
        
        // Verifica se a trilha existe
        trilhaService.findById(trilhaId);
        
        List<Avaliacao> avaliacoes = avaliacaoService.findByTrilhaId(trilhaId);
        List<AvaliacaoDTO> avaliacoesDTO = avaliacoes.stream()
            .map(AvaliacaoDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(avaliacoesDTO); 
    }

    @Operation(summary = "Cria uma nova avaliação",
               description = "Cadastra uma nova avaliação para uma trilha")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "404", description = "Trilha não encontrada") 
    })
    @PostMapping("/avaliacoes") 
    public ResponseEntity<AvaliacaoDTO> createAvaliacao(
            @Parameter(description = "Dados da avaliação a ser criada", required = true)
            @Valid @RequestBody AvaliacaoRequest request) {
        
        // Verifica se a trilha existe
        Trilha trilha = trilhaService.findById(request.getTrilhaId());
        
        // Cria a avaliação usando o usuário demo
        Avaliacao novaAvaliacao = avaliacaoService.criarAvaliacao(
            trilha, 
            AppConstants.USUARIO_DEMO_ID, 
            request.getNota(), 
            request.getComentario()
        );
        
        AvaliacaoDTO avaliacaoDTO = new AvaliacaoDTO(novaAvaliacao);
        
        // Cabeçalho Location com URI do recurso criado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaAvaliacao.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(avaliacaoDTO);
    }

    @Operation(summary = "Atualiza uma avaliação existente",
               description = "Atualiza os dados de uma avaliação existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Avaliação atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Avaliação não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/avaliacoes/{id}") 
    public ResponseEntity<AvaliacaoDTO> updateAvaliacao(
            @Parameter(description = "ID da avaliação", required = true)
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados da avaliação", required = true)
            @Valid @RequestBody AvaliacaoUpdateRequest request) {
        
        // Verifica se a avaliação existe
        avaliacaoService.findById(id);
        
        Avaliacao avaliacaoAtualizada = avaliacaoService.atualizarAvaliacao(
            id, 
            request.getNota(), 
            request.getComentario()
        );
        
        AvaliacaoDTO avaliacaoDTO = new AvaliacaoDTO(avaliacaoAtualizada);
        return ResponseEntity.ok(avaliacaoDTO); 
    }

    @Operation(summary = "Exclui uma avaliação",
               description = "Remove uma avaliação do sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Avaliação excluída com sucesso"), 
        @ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
    })
    @DeleteMapping("/avaliacoes/{id}") 
    public ResponseEntity<Void> deleteAvaliacao(
            @Parameter(description = "ID da avaliação", required = true)
            @PathVariable Long id) {
        
        // Verifica se a avaliação existe antes de excluir
        avaliacaoService.findById(id);
        avaliacaoService.excluir(id);
        
        return ResponseEntity.noContent().build(); 
    }

    /**
     * DTOs internos para requests específicos
     * Evitam exposição desnecessária das entidades JPA
     */
    public static class AvaliacaoRequest {
        private Long trilhaId;
        private int nota;
        private String comentario;

        public Long getTrilhaId() { return trilhaId; }
        public void setTrilhaId(Long trilhaId) { this.trilhaId = trilhaId; }
        public int getNota() { return nota; }
        public void setNota(int nota) { this.nota = nota; }
        public String getComentario() { return comentario; }
        public void setComentario(String comentario) { this.comentario = comentario; }
    }

    public static class AvaliacaoUpdateRequest {
        private int nota;
        private String comentario;

        public int getNota() { return nota; }
        public void setNota(int nota) { this.nota = nota; }
        public String getComentario() { return comentario; }
        public void setComentario(String comentario) { this.comentario = comentario; }
    }
}
