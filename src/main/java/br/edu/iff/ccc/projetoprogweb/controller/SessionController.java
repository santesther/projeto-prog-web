package br.edu.iff.ccc.projetoprogweb.controller;
import br.edu.iff.ccc.projetoprogweb.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class SessionController {

    @Autowired
    private SessionAttributes userSession;

    @Autowired
    private UsuarioController usuarioController;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if (userSession.isLogado()) {
            return "redirect:/home";
        }
        model.addAttribute("erro", null);
        model.addAttribute("mensagem", null);
        return "usuarios/login";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam String email,
            @RequestParam String senha,
            Model model
    ) {
        Usuario usuario = usuarioController.findByEmail(email);
        
        if (usuario != null && usuario.getSenha().equals(senha)) {
            userSession.setUsuarioLogado(usuario);
            userSession.setLogado(true);
            model.addAttribute("mensagem", "Login realizado com sucesso!");
            return "redirect:/home";
        } else {
            model.addAttribute("erro", "Email ou senha inválidos.");
            return "usuarios/login";
        }
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        userSession.clearSession();
        model.addAttribute("mensagem", "Logout realizado com sucesso!");
        return "redirect:/usuarios/login";
    }

    public static boolean isLogado() {
        return false;
    }

    public static boolean isAdmin() {
        return false;
    }

    public static boolean isTrilheiroExperiente() {
        return false;
    }

    public static boolean podeGerenciarTrilhas() {
        return false;
    }

    public static Usuario getUsuarioLogado() {
        return null;
    }
}
