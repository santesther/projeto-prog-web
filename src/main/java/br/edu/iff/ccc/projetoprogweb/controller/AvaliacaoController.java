package br.edu.iff.ccc.projetoprogweb.controller;

import br.edu.iff.ccc.projetoprogweb.entities.Trilha;
import br.edu.iff.ccc.projetoprogweb.service.AvaliacaoService;
import br.edu.iff.ccc.projetoprogweb.service.AvaliacaoService.AvaliacaoInvalidaException;
import br.edu.iff.ccc.projetoprogweb.service.TrilhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;
    
    /**
     * Service de trilhas necessário para:
     * - Validar se trilha existe antes de criar avaliação
     * - Buscar dados da trilha para exibir no formulário
     * - Navegação entre trilhas e avaliações
     */
    @Autowired
    private TrilhaService trilhaService;

    @GetMapping("/trilha/{trilhaId}")
    public String listAvaliacoesByTrilha(@PathVariable Long trilhaId, Model model) {
        // Adiciona a trilha ao modelo para exibir informações no template
        // Permite mostrar nome da trilha, descrição, etc. na página de avaliações
        model.addAttribute("trilha", trilhaService.findById(trilhaId));
        
        // Adiciona a lista de avaliações específicas desta trilha
        model.addAttribute("avaliacoes", avaliacaoService.findByTrilhaId(trilhaId));
        
        // Adiciona a média das avaliações para exibição
        model.addAttribute("mediaAvaliacoes", avaliacaoService.getMediaByTrilhaId(trilhaId));
        
        return "avaliacoes/lista";
    }

    @GetMapping("/trilha/{trilhaId}/nova")
    public String showNewAvaliacaoForm(@PathVariable Long trilhaId, Model model) {
        
        // Adiciona a trilha ao modelo para referência no formulário
        // Permite mostrar "Avaliando: Nome da Trilha" no cabeçalho do form
        model.addAttribute("trilha", trilhaService.findById(trilhaId));
        
        return "avaliacoes/formulario";
    }

    @PostMapping("/trilha/{trilhaId}/nova")
    public String processNewAvaliacao(
            @PathVariable Long trilhaId,
            @RequestParam int nota,
            @RequestParam(required = false) String comentario,
            Model model) {
        
        try {
            Trilha trilha = trilhaService.findById(trilhaId);
            Long usuarioId = 1L;
            
            avaliacaoService.criarAvaliacao(trilha, usuarioId, nota, comentario);
            
            return "redirect:/trilhas/" + trilhaId + "?avaliacaoSalva=true";
            
        } catch (AvaliacaoInvalidaException e) {
            // Tratamento específico para erro de validação
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("trilha", trilhaService.findById(trilhaId));
            return "avaliacoes/formulario";
        }
    }

    @GetMapping("/{id}/editar")
    public String showEditAvaliacaoForm(@PathVariable Long id, Model model) {
        
        // Busca a avaliação pelo ID e adiciona ao modelo
        // Template usa th:object="${avaliacao}" para pré-preencher campos
        model.addAttribute("avaliacao", avaliacaoService.findById(id));
        
        return "avaliacoes/editar";
    }

    @PostMapping("/{id}/editar")
    public String processEditAvaliacao(
            @PathVariable Long id,
            @RequestParam int nota,
            @RequestParam(required = false) String comentario,
            Model model
    ) {
        
        if (nota < 1 || nota > 5) {
            model.addAttribute("erro", "A nota deve estar entre 1 e 5");
            
            // Adiciona a avaliação atual
            model.addAttribute("avaliacao", avaliacaoService.findById(id));
            
            return "avaliacoes/editar";
        }
        
        avaliacaoService.atualizarAvaliacao(id, nota, comentario);
        
        // Necessário porque a URL de destino precisa do ID da trilha
        Long trilhaId = avaliacaoService.findById(id).getTrilha().getId();
        
        return "redirect:/trilhas/" + trilhaId + "?avaliacaoAtualizada=true";
    }

    @PostMapping("/{id}/excluir")
    public String excluirAvaliacao(@PathVariable Long id) {
        // IMPORTANTE: Fazer isso ANTES da exclusão, senão perde a referência
        Long trilhaId = avaliacaoService.findById(id).getTrilha().getId();
    
        // Service cuida da remoção e atualização de relacionamentos
        avaliacaoService.excluir(id);
        
        // Usuário volta para ver a trilha sem a avaliação excluída
        return "redirect:/trilhas/" + trilhaId + "?avaliacaoExcluida=true";
    }
}