package br.edu.iff.ccc.projetoprogweb.service;

import br.edu.iff.ccc.projetoprogweb.entities.Trilha;
import br.edu.iff.ccc.projetoprogweb.exception.TrilhaNaoEncontrada;
import br.edu.iff.ccc.projetoprogweb.entities.NivelDificuldade;
import br.edu.iff.ccc.projetoprogweb.entities.Avaliacao;
import br.edu.iff.ccc.projetoprogweb.repository.TrilhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrilhaService {
    
    @Autowired
    private TrilhaRepository trilhaRepository;
    
    //tabela de favoritos usando chaves compostas "usuarioId-trilhaId"
    private List<String> favoritos = new ArrayList<>(); 
    
    @Autowired // Spring automaticamente injeta a instância configurada
    private AvaliacaoService avaliacaoService;
   
    public List<Trilha> findAll() {
        return trilhaRepository.findAll();
    }
    
    public Trilha save(Trilha trilha) {
        return trilhaRepository.save(trilha);
    }
    
    public Trilha findById(Long id) {
        return trilhaRepository.findById(id).orElseThrow(() -> new TrilhaNaoEncontrada("Trilha com Id " + id + " não encontrada."));
    }

    public List<Trilha> findByNomeContaining(String nome) {
        return trilhaRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Trilha> findByDificuldade(NivelDificuldade dificuldade) {
        return trilhaRepository.findByDificuldade(dificuldade);
    }

    public List<Trilha> findByLocalizacao(String localizacao) {
        return trilhaRepository.findByLocalizacao(localizacao);
    }

    public List<Trilha> findByDuracaoMaxima(Integer duracaoMaxima) {
        return trilhaRepository.findByDuracaoMaxima(duracaoMaxima);
    }

    public void toggleFavorito(Long usuarioId, Long trilhaId) {
        String favoritoKey = usuarioId + "-" + trilhaId; // Chave composta
        
        if (favoritos.contains(favoritoKey)) {
            favoritos.remove(favoritoKey); // Remove dos favoritos
        } else {
            favoritos.add(favoritoKey); // Adiciona aos favoritos
        }
    }

    public List<Trilha> findFavoritasByUsuarioId(Long usuarioId) {
        return favoritos.stream()
                .filter(key -> key.startsWith(usuarioId + "-")) // Filtra por usuário
                .map(key -> {
                    Long trilhaId = Long.parseLong(key.split("-")[1]); // Extrai ID da trilha
                    return findById(trilhaId); // Busca a trilha completa
                })
                .filter(trilha -> trilha != null) // Remove nulls (se trilha foi deletada)
                .collect(Collectors.toList()); // Coleta em lista
    }

    public List<Avaliacao> getAvaliacoesByTrilhaId(Long trilhaId) {
        return avaliacaoService.findByTrilhaId(trilhaId); // Delega para o serviço especializado
    }
    
    public Double getMediaAvaliacoes(Long trilhaId) {
        return avaliacaoService.getMediaByTrilhaId(trilhaId); // Delega para o serviço especializado
    }

    public void deleteById(Long id) {
        trilhaRepository.deleteById(id);
    }
}
