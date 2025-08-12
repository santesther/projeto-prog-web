package br.edu.iff.ccc.projetoprogweb.service;

import br.edu.iff.ccc.projetoprogweb.entities.Trilha;
import br.edu.iff.ccc.projetoprogweb.entities.NivelDificuldade;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrilhaService {
    
    private List<Trilha> trilhas = new ArrayList<>();
    private Long nextId = 1L;
    
    public TrilhaService() {
        Trilha trilha1 = new Trilha();
        trilha1.setId(nextId++);
        trilha1.setNome("Trilha da Pedra Bonita");
        trilha1.setDescricao("Uma trilha desafiadora com vistas panorâmicas da cidade do Rio de Janeiro.");
        trilha1.setLocalizacao("Rio de Janeiro, RJ");
        trilha1.setDificuldade(NivelDificuldade.DIFICIL);
        trilha1.setAutorId(1L);
        trilha1.setAutorNome("João Trilheiro");
        trilhas.add(trilha1);

        Trilha trilha2 = new Trilha();
        trilha2.setId(nextId++);
        trilha2.setNome("Caminho das Águas");
        trilha2.setDescricao("Trilha leve passando por cachoeiras e riachos cristalinos.");
        trilha2.setLocalizacao("Petrópolis, RJ");
        trilha2.setDificuldade(NivelDificuldade.FACIL);
        trilha2.setAutorId(2L);
        trilha2.setAutorNome("Maria Silva");
        trilhas.add(trilha2);
        
        Trilha trilha3 = new Trilha();
        trilha3.setId(nextId++);
        trilha3.setNome("Pico do Aventureiro");
        trilha3.setDescricao("Trilha de dificuldade moderada com subida íngreme e vista espetacular.");
        trilha3.setLocalizacao("Nova Friburgo, RJ");
        trilha3.setDificuldade(NivelDificuldade.MODERADA);
        trilha3.setAutorId(1L);
        trilha3.setAutorNome("João Trilheiro");
        trilhas.add(trilha3);
    }
    
    public List<Trilha> findAll() {
        return new ArrayList<>(trilhas);
    }
    
    public Trilha save(Trilha trilha) {
        if (trilha.getId() == null) {
            trilha.setId(nextId++);
            trilhas.add(trilha);
        } else {
            for (int i = 0; i < trilhas.size(); i++) {
                if (trilhas.get(i).getId().equals(trilha.getId())) {
                    trilhas.set(i, trilha);
                    break;
                }
            }
        }
        return trilha;
    }
}
