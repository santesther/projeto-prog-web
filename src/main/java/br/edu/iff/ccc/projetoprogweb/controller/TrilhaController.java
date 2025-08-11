package br.edu.iff.ccc.projetoprogweb.controller;

import br.edu.iff.ccc.projetoprogweb.model.NivelDificuldade;
import br.edu.iff.ccc.projetoprogweb.model.Trilha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/trilhas")
public class TrilhaController {

    private List<Trilha> trilhas = new ArrayList<>();
    private long nextTrilhaId = 1;

    @Autowired
    private SessionAttributes userSession;

    public TrilhaController() {
        Trilha trilha1 = new Trilha();
        trilha1.setId(nextTrilhaId++);
        trilha1.setNome("Trilha da Pedra Bonita");
        trilha1.setDescricao("Uma trilha desafiadora com vistas panorâmicas da cidade.");
        trilha1.setLocalizacao("Rio de Janeiro, RJ");
        trilha1.setDificuldade(NivelDificuldade.DIFICIL);
        trilha1.setAutorId(2L);
        trilha1.setAutorNome("João Trilheiro");
        trilhas.add(trilha1);

        Trilha trilha2 = new Trilha();
        trilha2.setId(nextTrilhaId++);
        trilha2.setNome("Caminho da Floresta");
        trilha2.setDescricao("Percurso tranquilo em meio à mata atlântica, ideal para iniciantes.");
        trilha2.setLocalizacao("Parque Nacional da Tijuca, RJ");
        trilha2.setDificuldade(NivelDificuldade.FACIL);
        trilha2.setAutorId(1L);
        trilha2.setAutorNome("Admin");
        trilhas.add(trilha2);

        Trilha trilha3 = new Trilha();
        trilha3.setId(nextTrilhaId++);
        trilha3.setNome("Travessia Petrópolis-Teresópolis");
        trilha3.setDescricao("Uma das mais clássicas travessias de montanha do Brasil, exige bom preparo físico.");
        trilha3.setLocalizacao("Serra dos Órgãos, RJ");
        trilha3.setDificuldade(NivelDificuldade.DIFICIL);
        trilha3.setAutorId(2L);
        trilha3.setAutorNome("João Trilheiro");
        trilhas.add(trilha3);
    }

    public List<Trilha> getTrilhas() {
        return trilhas;
    }

    public Trilha findById(Long id) {
        return trilhas.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @GetMapping
    public String listTrilhas(@RequestParam(value = "viewMode", defaultValue = "all") String viewMode, Model model) {
        List<Trilha> trilhasExibir;
        if ("mine".equals(viewMode) && userSession.isLogado()) {
            trilhasExibir = trilhas.stream()
                    .filter(t -> t.getAutorId().equals(userSession.getUsuarioLogado().getId()))
                    .collect(Collectors.toList());
        } else {
            trilhasExibir = trilhas;
        }
        model.addAttribute("trilhas", trilhasExibir);
        model.addAttribute("viewMode", viewMode);
        return "trilhas/lista";
    }

    @GetMapping("/minhas")
    public String minhasTrilhas(Model model) {
        if (!userSession.isLogado()) {
            return "redirect:/usuarios/login";
        }
        return "redirect:/trilhas?viewMode=mine";
    }

    @GetMapping("/nova")
    public String showNewTrilhaForm(Model model) {
        if (!userSession.isPodeGerenciar()) {
            return "redirect:/home";
        }
        model.addAttribute("acao", "nova");
        model.addAttribute("trilha", new Trilha());
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
        if (!userSession.isPodeGerenciar()) {
            return "redirect:/home";
        }

        try {
            NivelDificuldade nivelDificuldade = NivelDificuldade.valueOf(dificuldade);
        
            Trilha novaTrilha = new Trilha();
            novaTrilha.setId(nextTrilhaId++);
            novaTrilha.setNome(nome);
            novaTrilha.setDescricao(descricao);
            novaTrilha.setLocalizacao(localizacao);
            novaTrilha.setDificuldade(nivelDificuldade);
            novaTrilha.setAutorId(userSession.getUsuarioLogado().getId());
            novaTrilha.setAutorNome(userSession.getUsuarioLogado().getNome());

            trilhas.add(novaTrilha);

            model.addAttribute("mensagem", "Trilha cadastrada com sucesso!");
            return "redirect:/trilhas";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", "Nível de dificuldade inválido.");
            model.addAttribute("acao", "nova");
            model.addAttribute("trilha", new Trilha());
            model.addAttribute("dificuldades", NivelDificuldade.values());
            return "trilhas/formulario";
        }
    }

    @GetMapping("/{id}")
    public String showTrilhaDetails(@PathVariable Long id, Model model) {
        Trilha trilha = findById(id);
        model.addAttribute("trilha", trilha);

        boolean podeEditar = false;
        if (trilha != null && userSession.isLogado()) {
            podeEditar = userSession.isAdmin() || trilha.getAutorId().equals(userSession.getUsuarioLogado().getId());
        }
        model.addAttribute("podeEditar", podeEditar);

        return "trilhas/detalhes";
    }

    @GetMapping("/{id}/editar")
    public String showEditTrilhaForm(@PathVariable Long id, Model model) {
        Trilha trilha = findById(id);
        if (trilha == null) {
            model.addAttribute("erro", "Trilha não encontrada para edição.");
            return "redirect:/trilhas";
        }

        if (!userSession.isLogado() || !(userSession.isAdmin() || trilha.getAutorId().equals(userSession.getUsuarioLogado().getId()))) {
            model.addAttribute("erro", "Você não tem permissão para editar esta trilha.");
            return "redirect:/trilhas/" + id;
        }

        model.addAttribute("acao", "editar");
        model.addAttribute("trilha", trilha);
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
        Trilha trilha = findById(id);
        if (trilha == null) {
            model.addAttribute("erro", "Trilha não encontrada para edição.");
            return "redirect:/trilhas";
        }

        if (!userSession.isLogado() || !(userSession.isAdmin() || trilha.getAutorId().equals(userSession.getUsuarioLogado().getId()))) {
            model.addAttribute("erro", "Você não tem permissão para editar esta trilha.");
            return "redirect:/trilhas/" + id;
        }

        try {
            NivelDificuldade nivelDificuldade = NivelDificuldade.valueOf(dificuldade);
        
            trilha.setNome(nome);
            trilha.setDescricao(descricao);
            trilha.setLocalizacao(localizacao);
            trilha.setDificuldade(nivelDificuldade);

            model.addAttribute("mensagem", "Trilha atualizada com sucesso!");
            return "redirect:/trilhas/" + id;
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", "Nível de dificuldade inválido.");
            model.addAttribute("acao", "editar");
            model.addAttribute("trilha", trilha);
            model.addAttribute("dificuldades", NivelDificuldade.values());
            return "trilhas/formulario";
        }
    }
}
