package br.edu.iff.ccc.projetoprogweb.controller;

import br.edu.iff.ccc.projetoprogweb.entities.TipoUsuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {


    @GetMapping("/cadastro")
    public String showRegistrationForm(Model model) {
        return "usuarios/cadastro";
    }

    @PostMapping("/cadastro")
    public String processRegistration(
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam TipoUsuario tipoUsuario,
            @RequestParam String senha,
            @RequestParam String confirmarSenha,
            Model model
    ) {
        return "redirect:/usuarios/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "usuarios/login";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam String email,
            @RequestParam String senha,
            Model model
    ) {
        return "redirect:/home";
    }

    @GetMapping("/perfil")
    public String showProfile(Model model) {
        return "usuarios/perfil";
    }

    @GetMapping("/editar-perfil")
    public String showEditProfileForm(Model model) {
        return "usuarios/perfil";
    }

    @PostMapping("/editar-perfil")
    public String processEditProfile(
            @RequestParam String nome,
            @RequestParam String email,
            Model model
    ) {
        return "redirect:/usuarios/perfil";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        return "redirect:/usuarios/login";
    }
}
