package br.edu.iff.ccc.projetoprogweb.controller.restapi;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/api/v1")
public class RestApiMainController {

    private static List<Trilha> trilhas = new ArrayList<>();
    
    @GetMapping("/{id}")
    public String getHomePage(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        

        model.addAttribute("totalTrilhas", trilhas.size());
        model.addAttribute("trilhas", trilhas);

        if (!id.equals("home")) {
            try {
                Long trilhaId = Long.parseLong(id);
                Trilha trilha = trilhas.stream()
                    .filter(t -> t.getId().equals(trilhaId))
                    .findFirst()
                    .orElse(null);
                model.addAttribute("trilhaEspecifica", trilha);
            } catch (NumberFormatException e) {
            
            }
        }
        
        return "index.html";
    }
    

    @PostMapping("/trilhas")
    @ResponseBody
    public String cadastrarTrilha(@RequestBody TrilhaRequest request) {
        try {
            Trilha novaTrilha = new Trilha();
            novaTrilha.setId((long) (trilhas.size() + 1));
            novaTrilha.setNome(request.getNome());
            novaTrilha.setDescricao(request.getDescricao());
            novaTrilha.setLocalizacao(request.getLocalizacao());
            novaTrilha.setDificuldade(request.getDificuldade());
            
            trilhas.add(novaTrilha);
            
            return "{\"status\":\"success\",\"message\":\"Trilha cadastrada com sucesso!\",\"id\":" + novaTrilha.getId() + "}";
        } catch (Exception e) {
            return "{\"status\":\"error\",\"message\":\"Erro ao cadastrar trilha: " + e.getMessage() + "\"}";
        }
    }
    
    @GetMapping("/trilhas")
    @ResponseBody
    public Map<String, Object> getTrilhas() {
        Map<String, Object> response = new HashMap<>();
        response.put("trilhas", trilhas);
        response.put("total", trilhas.size());
        return response;
    }

    @GetMapping("/status")
    @ResponseBody
    public Map<String, Object> getStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "online");
        response.put("totalTrilhas", trilhas.size());
        return response;
    }
    
    public static class TrilhaRequest {
        private String nome;
        private String descricao;
        private String localizacao;
        private String dificuldade;
        
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
        
        public String getLocalizacao() { return localizacao; }
        public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
        
        public String getDificuldade() { return dificuldade; }
        public void setDificuldade(String dificuldade) { this.dificuldade = dificuldade; }
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
