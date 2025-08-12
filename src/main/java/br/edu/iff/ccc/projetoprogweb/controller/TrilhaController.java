package br.edu.iff.ccc.projetoprogweb.controller;

import br.edu.iff.ccc.projetoprogweb.service.TrilhaService;
import br.edu.iff.ccc.projetoprogweb.entities.NivelDificuldade;
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
    public String listTrilhas(@RequestParam(value = "viewMode", defaultValue = "all") String viewMode, Model model) {
        return "trilhas/lista";
    }

    @GetMapping("/minhas")
    public String minhasTrilhas(Model model) {
        return "redirect:/trilhas?viewMode=mine";
    }

    @GetMapping("/nova")
    public String showNewTrilhaForm(Model model) {
        model.addAttribute("dificuldades", NivelDificuldade.values());
        return "trilhas/formulario";
    }

    @PostMapping("/nova")
    public String processNewTrilha(
            @RequestParam String nome,
            @RequestParam String descricao,
            @RequestParam String localizacao,
            @RequestParam String dificuldade,
            Model model
    ) {
        return "redirect:/trilhas";
    }

    @GetMapping("/{id}")
    public String showTrilhaDetails(@PathVariable Long id, Model model) {
        return "trilhas/detalhes";
    }

    @GetMapping("/{id}/editar")
    public String showEditTrilhaForm(@PathVariable Long id, Model model) {
        model.addAttribute("dificuldades", NivelDificuldade.values());
        return "trilhas/formulario";
    }

    @PostMapping("/{id}/editar")
    public String processEditTrilha(
            @PathVariable Long id,
            @RequestParam String nome,
            @RequestParam String descricao,
            @RequestParam String localizacao,
            @RequestParam String dificuldade,
            Model model
    ) {
        return "redirect:/trilhas/" + id;
    }
}
