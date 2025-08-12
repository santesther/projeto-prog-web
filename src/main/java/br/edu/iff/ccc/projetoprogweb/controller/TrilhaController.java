package br.edu.iff.ccc.projetoprogweb.controller;

import br.edu.iff.ccc.projetoprogweb.entities.Trilha;
import br.edu.iff.ccc.projetoprogweb.entities.NivelDificuldade;
import br.edu.iff.ccc.projetoprogweb.service.TrilhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/trilhas")
public class TrilhaController {

    @Autowired
    private TrilhaService trilhaService;

    @GetMapping
    public String listTrilhas(Model model) {
        model.addAttribute("trilhas", trilhaService.findAll());
        model.addAttribute("trilha", new Trilha());
        model.addAttribute("dificuldades", NivelDificuldade.values());
        return "trilhas/lista";
    }

    @PostMapping
    public String cadastrarTrilha(
            @RequestParam String nome,
            @RequestParam String descricao,
            @RequestParam String localizacao,
            @RequestParam NivelDificuldade dificuldade,
            Model model
    ) {
        Trilha novaTrilha = new Trilha();
        novaTrilha.setNome(nome);
        novaTrilha.setDescricao(descricao);
        novaTrilha.setLocalizacao(localizacao);
        novaTrilha.setDificuldade(dificuldade);
        
        trilhaService.save(novaTrilha);
        
        return "redirect:/trilhas";
    }
}
