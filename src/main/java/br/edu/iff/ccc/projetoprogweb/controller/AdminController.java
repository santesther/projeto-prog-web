package br.edu.iff.ccc.projetoprogweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {


    @GetMapping
    public String adminDashboard(Model model) {
        return "admin/dashboard";
    }

    @GetMapping("/trilhas")
    public String adminTrilhas(Model model) {
        return "admin/trilhas";
    }

    @GetMapping("/usuarios")
    public String adminUsuarios(Model model) {
        return "admin/usuarios";
    }

    @GetMapping("/relatorios")
    public String adminRelatorios(Model model) {
        return "admin/relatorios";
    }

    @GetMapping("/relatorios/trilhas")
    public String relatorioTrilhas(Model model) {
        return "admin/relatorio-trilhas";
    }

    @GetMapping("/relatorios/usuarios")
    public String relatorioUsuarios(Model model) {
        return "admin/relatorio-usuarios";
    }

    @GetMapping("/configuracoes")
    public String adminConfiguracoes(Model model) {
        return "admin/configuracoes";
    }

    @PostMapping("/configuracoes")
    public String salvarConfiguracoes(Model model) {
        return "redirect:/admin/configuracoes";
    }
}
