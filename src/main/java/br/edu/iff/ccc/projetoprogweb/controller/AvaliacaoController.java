package br.edu.iff.ccc.projetoprogweb.controller;

import br.edu.iff.ccc.projetoprogweb.model.Avaliacao;
import br.edu.iff.ccc.projetoprogweb.model.Trilha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    private List<Avaliacao> avaliacoes = new ArrayList<>();
    private long nextAvaliacaoId = 1;

    @Autowired
    private SessionAttributes userSession;

    @Autowired
    private TrilhaController trilhaController;

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    @GetMapping("/trilha/{trilhaId}")
    public String listAvaliacoesByTrilha(@PathVariable Long trilhaId, Model model) {
        Trilha trilha = trilhaController.findById(trilhaId);
        if (trilha == null) {
            model.addAttribute("erro", "Trilha não encontrada.");
            return "redirect:/trilhas";
        }

        List<Avaliacao> avaliacoesDaTrilha = avaliacoes.stream()
                .filter(a -> a.getTrilhaId().equals(trilhaId))
                .collect(Collectors.toList());

        model.addAttribute("trilhaId", trilhaId);
        model.addAttribute("trilhaNome", trilha.getNome());
        model.addAttribute("avaliacoes", avaliacoesDaTrilha);
        model.addAttribute("viewMode", "trilha");
        return "avaliacoes/lista";
    }

    @GetMapping("/minhas")
    public String minhasAvaliacoes(Model model) {
        if (!userSession.isLogado()) {
            return "redirect:/usuarios/login";
        }

        List<Avaliacao> minhasAvaliacoes = avaliacoes.stream()
                .filter(a -> a.getUsuarioId().equals(userSession.getUsuarioLogado().getId()))
                .collect(Collectors.toList());

        Map<Long, String> trilhaNomes = new HashMap<>();
        for (Avaliacao avaliacao : minhasAvaliacoes) {
            Trilha trilha = trilhaController.findById(avaliacao.getTrilhaId());
            if (trilha != null) {
                trilhaNomes.put(avaliacao.getTrilhaId(), trilha.getNome());
            } else {
                trilhaNomes.put(avaliacao.getTrilhaId(), "Trilha Desconhecida");
            }
        }

        model.addAttribute("avaliacoes", minhasAvaliacoes);
        model.addAttribute("trilhaNomes", trilhaNomes);
        model.addAttribute("viewMode", "mine");
        return "avaliacoes/lista";
    }

    @GetMapping("/trilha/{trilhaId}/nova")
    public String showNewAvaliacaoForm(@PathVariable Long trilhaId, Model model) {
        if (!userSession.isLogado()) {
            return "redirect:/usuarios/login";
        }
        Trilha trilha = trilhaController.findById(trilhaId);
        if (trilha == null) {
            model.addAttribute("erro", "Trilha não encontrada para avaliação.");
            return "redirect:/trilhas";
        }
        model.addAttribute("trilhaId", trilhaId);
        model.addAttribute("erro", null);
        return "avaliacoes/formulario";
    }

    @PostMapping("/trilha/{trilhaId}/nova")
    public String processNewAvaliacao(
            @PathVariable Long trilhaId,
            @RequestParam int nota,
            @RequestParam(required = false) String comentario,
            Model model
    ) {
        if (!userSession.isLogado()) {
            return "redirect:/usuarios/login";
        }

        Trilha trilha = trilhaController.findById(trilhaId);
        if (trilha == null) {
            model.addAttribute("erro", "Trilha não encontrada para avaliação.");
            return "avaliacoes/formulario";
        }

        boolean jaAvaliou = avaliacoes.stream()
                .anyMatch(a -> a.getTrilhaId().equals(trilhaId) && a.getUsuarioId().equals(userSession.getUsuarioLogado().getId()));

        if (jaAvaliou) {
            model.addAttribute("erro", "Você já avaliou esta trilha.");
            model.addAttribute("trilhaId", trilhaId);
            return "avaliacoes/formulario";
        }

        Avaliacao novaAvaliacao = new Avaliacao();
        novaAvaliacao.setId(nextAvaliacaoId++);
        novaAvaliacao.setTrilhaId(trilhaId);
        novaAvaliacao.setUsuarioId(userSession.getUsuarioLogado().getId());
        novaAvaliacao.setUsuarioNome(userSession.getUsuarioLogado().getNome());
        novaAvaliacao.setNota(nota);
        novaAvaliacao.setComentario(comentario);
        novaAvaliacao.setDataAvaliacao(LocalDateTime.now());

        avaliacoes.add(novaAvaliacao);

        model.addAttribute("mensagem", "Avaliação salva com sucesso!");
        return "redirect:/trilhas/" + trilhaId;
    }
}
