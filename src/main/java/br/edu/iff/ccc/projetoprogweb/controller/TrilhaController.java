package br.edu.iff.ccc.projetoprogweb.controller;

import br.edu.iff.ccc.projetoprogweb.entities.Trilha;
import br.edu.iff.ccc.projetoprogweb.exception.TrilhaNaoEncontrada;
import br.edu.iff.ccc.projetoprogweb.entities.NivelDificuldade;
import br.edu.iff.ccc.projetoprogweb.service.TrilhaService;
import br.edu.iff.ccc.projetoprogweb.config.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/trilhas") 
public class TrilhaController {

    @Autowired
    private TrilhaService trilhaService;

    @GetMapping
    public String listTrilhas(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) NivelDificuldade dificuldade,
            @RequestParam(required = false) String localizacao,
            Model model) {
        
        // aplicação dos filtros em cascata
        
        List<Trilha> trilhasFiltradas = trilhaService.findAll(); // chama o service para encontrar as trilhas
        
        
        if (busca != null && !busca.isEmpty()) {
            trilhasFiltradas = trilhaService.findByNomeContaining(busca);
        }
        
        if (dificuldade != null) {
            trilhasFiltradas = trilhasFiltradas.stream()
                    .filter(trilha -> trilha.getDificuldade().equals(dificuldade))
                    .collect(Collectors.toList());
        }
        
        if (localizacao != null && !localizacao.isEmpty()) {
            trilhasFiltradas = trilhasFiltradas.stream()
                    .filter(trilha -> trilha.getLocalizacao().toLowerCase().contains(localizacao.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        // Adiciona dados ao Model 
        model.addAttribute("trilhas", trilhasFiltradas); 
        model.addAttribute("dificuldades", NivelDificuldade.values()); 
        model.addAttribute("busca", busca); 
        model.addAttribute("dificuldadeSelecionada", dificuldade); 
        model.addAttribute("localizacaoSelecionada", localizacao); 
        
        return "trilhas/lista"; 
    }

    @GetMapping("/{id}")
    public String viewTrilha(@PathVariable Long id, Model model) {
        try {
            Trilha trilha = trilhaService.findById(id);
            model.addAttribute("trilha", trilha);
            return "trilhas/detalhes";
        } catch (TrilhaNaoEncontrada e) {
            model.addAttribute("errorMessage", "Trilha com ID " + id + " não foi encontrada.");
            return "trilhas/nao-encontrada";
        }
    }

    @GetMapping("/cadastro")
    public String showCadastroForm(Model model) {
        // Thymeleaf usa th:object="${trilha}" para vincular campos do form
        model.addAttribute("trilha", new Trilha());
        
        // Usado em <select th:field="*{dificuldade}">
        model.addAttribute("dificuldades", NivelDificuldade.values());
        
        return "trilhas/cadastro";
    }

    /**
     * ROTA: POST /trilhas
     * FUNÇÃO: Processa o envio do formulário de cadastro de nova trilha.
     * Valida os dados, define autor demo e salva a trilha.

     * 1. Spring converte dados do form em objeto Trilha
     * 2. @Valid executa validações (@NotEmpty, @NotNull, etc.)
     * 3. Erros são colocados em BindingResult
     */
    @PostMapping
    public String cadastrarTrilha(
            @Valid @ModelAttribute("trilha") Trilha trilha,
            BindingResult result,
            Model model) {
        
        
        // Verifica se houve erros de validação (@NotEmpty, @NotNull, etc.)
        if (result.hasErrors()) {
            model.addAttribute("trilha", trilha);
            
           //lista de dificuldades
            model.addAttribute("dificuldades", NivelDificuldade.values());
            
            // Thymeleaf usa th:errors="*{campo}" para mostrar mensagens de erro
            return "trilhas/cadastro";
        }
        
        // Define informações do autor (simulação de usuário logado)
        trilha.setAutorId(AppConstants.USUARIO_DEMO_ID);
        trilha.setAutorNome(AppConstants.AUTOR_DEMO_NOME);
        
        // Salva a trilha no sistema 
        trilhaService.save(trilha);
       
        return "redirect:/trilhas?cadastro_sucesso=true";
    }


    @GetMapping("/{id}/editar")
    public String showEditForm(@PathVariable Long id, Model model) {
        
        Trilha trilha = trilhaService.findById(id);
        
        // Verifica se a trilha existe
        if (trilha == null) {
            return "redirect:/trilhas?error=trilha_nao_encontrada";
        }
        
        
        model.addAttribute("trilha", trilha); // Trilha com dados atuais
        model.addAttribute("dificuldades", NivelDificuldade.values()); 
        
        return "trilhas/editar"; 
    }

    @PostMapping("/{id}/editar")
    public String editarTrilha(
            @PathVariable Long id,
            @Valid @ModelAttribute("trilha") Trilha trilhaAtualizada,
            BindingResult result,
            Model model) {
        
        
        // Verifica erros de validação
        if (result.hasErrors()) {
            model.addAttribute("dificuldades", NivelDificuldade.values());
            return "trilhas/editar"; // Volta ao formulário com erros
        }
        
        // Busca a trilha original para preservar dados imutáveis
        Trilha trilhaExistente = trilhaService.findById(id);
        if (trilhaExistente == null) {
            return "redirect:/trilhas?error=trilha_nao_encontrada";
        }
        
        // Preserva dados que não devem ser alterados pelo formulário
        trilhaAtualizada.setId(id); 
        trilhaAtualizada.setAutorId(trilhaExistente.getAutorId()); 
        trilhaAtualizada.setAutorNome(trilhaExistente.getAutorNome()); 
        
        // Salva as alterações
        trilhaService.save(trilhaAtualizada);
        
        return "redirect:/trilhas/" + id + "?edicao_sucesso=true";
    }

    @PostMapping("/{id}/excluir")
    public String excluirTrilha(@PathVariable Long id) {
        
        // Verifica se a trilha existe antes de tentar excluir
        Trilha trilha = trilhaService.findById(id);
        if (trilha == null) {
            return "redirect:/trilhas?error=trilha_nao_encontrada";
        }
        
        // Executa a exclusão via service
        trilhaService.deleteById(id);
        
        return "redirect:/trilhas?exclusao_sucesso=true";
    }

    @PostMapping("/{id}/favoritar")
    @ResponseBody
    public String favoritarTrilha(@PathVariable Long id) {
        
        // Verifica se a trilha existe
        Trilha trilha = trilhaService.findById(id);
        if (trilha == null) {
            return "error"; // Retorna erro para o AJAX
        }
        
        // Alterna o status de favorito (adiciona ou remove)
        trilhaService.toggleFavorito(AppConstants.USUARIO_DEMO_ID, id);
        
        return "success"; 
    }

  
    @GetMapping("/favoritas")
    public String trilhasFavoritas(Model model) {
        // Obtém as trilhas favoritas do usuário demo
        model.addAttribute("trilhas", trilhaService.findFavoritasByUsuarioId(AppConstants.USUARIO_DEMO_ID));
        
        return "trilhas/favoritas"; 
    }
}