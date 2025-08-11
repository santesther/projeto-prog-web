package br.edu.iff.ccc.projetoprogweb.controller;

import br.edu.iff.ccc.projetoprogweb.model.TipoUsuario;
import br.edu.iff.ccc.projetoprogweb.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private List<Usuario> usuarios = new ArrayList<>();
    private long nextUserId = 1;

    @Autowired
    private SessionAttributes userSession;

    public UsuarioController() {
        Usuario admin = new Usuario();
        admin.setId(nextUserId++);
        admin.setNome("Admin");
        admin.setEmail("admin@trilhas.com");
        admin.setSenha("admin123");
        admin.setTipoUsuario(TipoUsuario.ADMINISTRADOR);
        usuarios.add(admin);

        Usuario joao = new Usuario();
        joao.setId(nextUserId++);
        joao.setNome("João Trilheiro");
        joao.setEmail("joao@trilhas.com");
        joao.setSenha("123456");
        joao.setTipoUsuario(TipoUsuario.TRILHEIRO_EXPERIENTE);
        usuarios.add(joao);

        Usuario maria = new Usuario();
        maria.setId(nextUserId++);
        maria.setNome("Maria Usuária");
        maria.setEmail("maria@trilhas.com");
        maria.setSenha("senha123");
        maria.setTipoUsuario(TipoUsuario.USUARIO);
        usuarios.add(maria);
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public Usuario findByEmail(String email) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public Usuario findById(Long id) {
        return usuarios.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @GetMapping("/cadastro")
    public String showRegistrationForm(Model model) {
        if (userSession.isLogado()) {
            return "redirect:/home";
        }
        model.addAttribute("erro", null);
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
        if (!senha.equals(confirmarSenha)) {
            model.addAttribute("erro", "As senhas não coincidem.");
            return "usuarios/cadastro";
        }

        if (findByEmail(email) != null) {
            model.addAttribute("erro", "Este email já está cadastrado.");
            return "usuarios/cadastro";
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setId(nextUserId++);
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(senha);
        novoUsuario.setTipoUsuario(tipoUsuario);
        usuarios.add(novoUsuario);

        model.addAttribute("mensagem", "Cadastro realizado com sucesso! Faça login.");
        return "redirect:/usuarios/login";
    }

    @GetMapping("/perfil")
    public String showProfile(Model model) {
        if (!userSession.isLogado()) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("usuario", userSession.getUsuarioLogado());
        model.addAttribute("view", "view");
        return "usuarios/perfil";
    }

    @GetMapping("/editar-perfil")
    public String showEditProfileForm(Model model) {
        if (!userSession.isLogado()) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("usuario", userSession.getUsuarioLogado());
        model.addAttribute("view", "edit");
        return "usuarios/perfil";
    }

    @PostMapping("/editar-perfil")
    public String processEditProfile(
            @RequestParam String nome,
            @RequestParam String email,
            Model model
    ) {
        if (!userSession.isLogado()) {
            return "redirect:/usuarios/login";
        }

        Usuario usuarioLogado = userSession.getUsuarioLogado();

        Usuario usuarioExistente = findByEmail(email);
        if (usuarioExistente != null && !usuarioExistente.getId().equals(usuarioLogado.getId())) {
            model.addAttribute("erro", "Este email já está em uso por outro usuário.");
            model.addAttribute("usuario", usuarioLogado);
            model.addAttribute("view", "edit");
            return "usuarios/perfil";
        }

        usuarioLogado.setNome(nome);
        usuarioLogado.setEmail(email);
        userSession.setUsuarioLogado(usuarioLogado);

        model.addAttribute("mensagem", "Perfil atualizado com sucesso!");
        return "redirect:/usuarios/perfil";
    }

    @GetMapping("/alterar-senha")
    public String showChangePasswordForm(Model model) {
        if (!userSession.isLogado()) {
            return "redirect:/usuarios/login";
        }
        model.addAttribute("erro", null);
        model.addAttribute("view", "change-password");
        return "usuarios/perfil";
    }

    @PostMapping("/alterar-senha")
    public String processChangePassword(
            @RequestParam String senhaAtual,
            @RequestParam String novaSenha,
            @RequestParam String confirmarNovaSenha,
            Model model
    ) {
        if (!userSession.isLogado()) {
            return "redirect:/usuarios/login";
        }

        Usuario usuarioLogado = userSession.getUsuarioLogado();

        if (!usuarioLogado.getSenha().equals(senhaAtual)) {
            model.addAttribute("erro", "Senha atual incorreta.");
            model.addAttribute("view", "change-password");
            return "usuarios/perfil";
        }

        if (!novaSenha.equals(confirmarNovaSenha)) {
            model.addAttribute("erro", "A nova senha e a confirmação não coincidem.");
            model.addAttribute("view", "change-password");
            return "usuarios/perfil";
        }

        usuarioLogado.setSenha(novaSenha);
        userSession.setUsuarioLogado(usuarioLogado);

        model.addAttribute("mensagem", "Senha alterada com sucesso!");
        return "redirect:/usuarios/perfil";
    }
}
