package br.edu.iff.ccc.projetoprogweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SessionAttributes userSession;

    @Autowired
    private TrilhaController trilhaController;

    @Autowired
    private UsuarioController usuarioController;

    @Autowired
    private AvaliacaoController avaliacaoController;

    @GetMapping
    public String adminDashboard(Model model) {
        if (!userSession.isAdmin()) {
            model.addAttribute("erro", "Acesso negado. Apenas administradores podem acessar esta área.");
            return "redirect:/home";
        }

        model.addAttribute("trilhasPendentes", trilhaController.getTrilhas().size());
        model.addAttribute("usuariosAtivos", usuarioController.getUsuarios().size());
        model.addAttribute("avaliacoesPendentes", avaliacaoController.getAvaliacoes().size());
        
        return "admin/dashboard";
    }

    @GetMapping("/trilhas")
    public String adminTrilhas(Model model) {
        if (!userSession.isAdmin()) {
            model.addAttribute("erro", "Acesso negado.");
            return "redirect:/home";
        }

        model.addAttribute("trilhas", trilhaController.getTrilhas());
        return "admin/trilhas";
    }

    @GetMapping("/usuarios")
    public String adminUsuarios(Model model) {
        if (!userSession.isAdmin()) {
            model.addAttribute("erro", "Acesso negado.");
            return "redirect:/home";
        }

        model.addAttribute("usuarios", usuarioController.getUsuarios());
        return "admin/usuarios";
    }

    @GetMapping("/relatorios")
    public String adminRelatorios(Model model) {
        if (!userSession.isAdmin()) {
            model.addAttribute("erro", "Acesso negado.");
            return "redirect:/home";
        }

        model.addAttribute("totalTrilhas", trilhaController.getTrilhas().size());
        model.addAttribute("totalUsuarios", usuarioController.getUsuarios().size());
        model.addAttribute("totalAvaliacoes", avaliacaoController.getAvaliacoes().size());
        
        return "admin/relatorios";
    }

    @GetMapping("/relatorios/trilhas")
    public String relatorioTrilhas(Model model) {
        if (!userSession.isAdmin()) {
            model.addAttribute("erro", "Acesso negado.");
            return "redirect:/home";
        }

        model.addAttribute("trilhas", trilhaController.getTrilhas());
        return "admin/relatorio-trilhas";
    }

    @GetMapping("/relatorios/usuarios")
    public String relatorioUsuarios(Model model) {
        if (!userSession.isAdmin()) {
            model.addAttribute("erro", "Acesso negado.");
            return "redirect:/home";
        }

        model.addAttribute("usuarios", usuarioController.getUsuarios());
        return "admin/relatorio-usuarios";
    }

    @GetMapping("/configuracoes")
    public String adminConfiguracoes(Model model) {
        if (!userSession.isAdmin()) {
            model.addAttribute("erro", "Acesso negado.");
            return "redirect:/home";
        }

        return "admin/configuracoes";
    }

    @PostMapping("/configuracoes")
    public String salvarConfiguracoes(Model model) {
        if (!userSession.isAdmin()) {
            model.addAttribute("erro", "Acesso negado.");
            return "redirect:/home";
        }

        model.addAttribute("mensagem", "Configurações salvas com sucesso!");
        return "redirect:/admin/configuracoes";
    }
}
