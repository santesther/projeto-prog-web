package br.edu.iff.ccc.projetoprogweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/principal")
public class PrincipalController {

    private List<Trilha> trilhas = new ArrayList<>();
    private Long proximoId = 1L;

    @GetMapping
    public String principal(Model model) {
        model.addAttribute("trilhas", trilhas);
        model.addAttribute("totalTrilhas", trilhas.size());
        return "index";
    }

    @PostMapping("/cadastrar")
    public String cadastrarTrilha(
            @RequestParam String nome,
            @RequestParam String descricao,
            @RequestParam String localizacao,
            @RequestParam String dificuldade,
            Model model) {
        
        Trilha trilha = new Trilha();
        trilha.setId(proximoId++);
        trilha.setNome(nome);
        trilha.setDescricao(descricao);
        trilha.setLocalizacao(localizacao);
        trilha.setDificuldade(dificuldade);
        
        trilhas.add(trilha);
        
        model.addAttribute("mensagem", "Trilha cadastrada com sucesso!");
        model.addAttribute("trilhas", trilhas);
        model.addAttribute("totalTrilhas", trilhas.size());
        
        return "index";
    }

    public static class Trilha {
        private Long id;
        private String nome;
        private String descricao;
        private String localizacao;
        private String dificuldade;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
        
        public String getLocalizacao() { return localizacao; }
        public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
        
        public String getDificuldade() { return dificuldade; }
        public void setDificuldade(String dificuldade) { this.dificuldade = dificuldade; }
    }
}
