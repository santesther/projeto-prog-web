package br.edu.iff.ccc.projetoprogweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/avaliacoes")
public class AvaliacaoController {


    @GetMapping("/trilha/{trilhaId}")
    public String listAvaliacoesByTrilha(@PathVariable Long trilhaId, Model model) {
        return "avaliacoes/lista";
    }

    @GetMapping("/minhas")
    public String minhasAvaliacoes(Model model) {
        return "avaliacoes/lista";
    }

    @GetMapping("/trilha/{trilhaId}/nova")
    public String showNewAvaliacaoForm(@PathVariable Long trilhaId, Model model) {
        return "avaliacoes/formulario";
    }

    @PostMapping("/trilha/{trilhaId}/nova")
    public String processNewAvaliacao(
            @PathVariable Long trilhaId,
            @RequestParam int nota,
            @RequestParam(required = false) String comentario,
            Model model
    ) {
        return "redirect:/trilhas/" + trilhaId;
    }
}
