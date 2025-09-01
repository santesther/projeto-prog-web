package br.edu.iff.ccc.projetoprogweb.service;

import br.edu.iff.ccc.projetoprogweb.entities.Trilha;
import jakarta.annotation.PostConstruct;
import br.edu.iff.ccc.projetoprogweb.entities.NivelDificuldade;
import br.edu.iff.ccc.projetoprogweb.entities.Avaliacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Service:
 * O Spring automaticamente detecta, instancia e gerencia esta classe
 * através do mecanismo de injeção de dependência.
 */
@Service
public class TrilhaService {
    
    // Simula uma tabela de trilhas em memória 
    private List<Trilha> trilhas = new ArrayList<>();
    
    // Simula uma tabela de favoritos usando chaves compostas "usuarioId-trilhaId"
    private List<String> favoritos = new ArrayList<>(); 
    
    // Permite acesso às avaliações sem criar acoplamento direto
    @Autowired
    private AvaliacaoService avaliacaoService;
    
    /**
     * @PostConstruct: Executado APÓS Spring criar o bean e injetar dependências
     * É essencial usar @PostConstruct em vez do construtor porque:
     * - No construtor, avaliacaoService ainda seria null
     * - @PostConstruct garante que todas as injeções foram feitas
     */
    @PostConstruct
    public void init() {
        
        Trilha trilha1 = new Trilha();
        trilha1.setId(1L);                                  
        trilha1.setNome("Trilha da Pedra Bonita");          
        trilha1.setDescricao("Uma trilha desafiadora com vistas panorâmicas da cidade do Rio de Janeiro.");
        trilha1.setLocalizacao("Rio de Janeiro, RJ");        
        trilha1.setDificuldade(NivelDificuldade.DIFICIL);  
        trilha1.setAutorId(1L);                              
        trilha1.setAutorNome("João Trilheiro");             
        trilhas.add(trilha1);                                

        Trilha trilha2 = new Trilha();
        trilha2.setId(2L);
        trilha2.setNome("Caminho das Águas");
        trilha2.setDescricao("Trilha leve passando por cachoeiras e riachos cristalinos.");
        trilha2.setLocalizacao("Petrópolis, RJ");
        trilha2.setDificuldade(NivelDificuldade.FACIL);
        trilha2.setAutorId(2L);
        trilha2.setAutorNome("Maria Silva");
        trilhas.add(trilha2);
        
        Trilha trilha3 = new Trilha();
        trilha3.setId(3L);
        trilha3.setNome("Pico do Aventureiro");
        trilha3.setDescricao("Trilha de dificuldade moderada com subida íngreme e vista espetacular.");
        trilha3.setLocalizacao("Nova Friburgo, RJ");
        trilha3.setDificuldade(NivelDificuldade.MODERADA);
        trilha3.setAutorId(1L);
        trilha3.setAutorNome("João Trilheiro");
        trilhas.add(trilha3);
        
        // Só pode chamar avaliacaoService APÓS @PostConstruct
        // porque agora as dependências já foram injetadas
        avaliacaoService.initMockData(trilha1);
    }
    
    /**
     * READ: Retorna todas as trilhas do sistema.
     */
    public List<Trilha> findAll() {
        return new ArrayList<>(trilhas);
    }

    /**
     * Simula o comportamento de auto incremento
     */
    private Long getNextId() {
        if (trilhas.isEmpty()) {
            return 1L;                                   // Primeiro ID se não houver trilhas
        }
        
        // Stream API p encontrar o maior ID existente
        return trilhas.stream()
                .mapToLong(Trilha::getId)                // Converte para stream de longs
                .max()                                   
                .orElse(0L) + 1L;                       // Se vazio, começa de 1 (0 + 1)
    }
    
    public Trilha save(Trilha trilha) {
        if (trilha.getId() == null) {
            // Nova trilha: gera ID sequencial e adiciona à lista
            Long newId = getNextId();                   
            trilha.setId(newId);                         
            trilhas.add(trilha);                        
        } else {
            for (int i = 0; i < trilhas.size(); i++) {
                if (trilhas.get(i).getId().equals(trilha.getId())) {
                    trilhas.set(i, trilha);              // Substitui a trilha antiga
                    break;                               // break após encontrar
                }
            }
        }
        return trilha;
    }
    
    
    public Trilha findById(Long id) {
        return trilhas.stream()
                .filter(trilha -> trilha.getId().equals(id))    // Filtra pelo ID
                .findFirst()                                    // Pega o primeiro resultado
                .orElse(null);                            // Retorna null se não encontrar
    }

    /**
     * @implNote Busca case-insensitive (converte tudo para minúsculas)
     */
    public List<Trilha> findByNomeContaining(String nome) {
        return trilhas.stream()
                .filter(trilha -> trilha.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());           // Coleta resultados em lista
    }

    /**
     * @implNote Usa chave composta "usuarioId-trilhaId" para simular tabela de favoritos
     */
    public void toggleFavorito(Long usuarioId, Long trilhaId) {
        // Cria chave composta para identificar o relacionamento usuário-trilha
        String favoritoKey = usuarioId + "-" + trilhaId;
        
        if (favoritos.contains(favoritoKey)) {
            favoritos.remove(favoritoKey);
        } else {
            favoritos.add(favoritoKey);
        }
    }

    public List<Trilha> findFavoritasByUsuarioId(Long usuarioId) {
        return favoritos.stream()
                // Filtra favoritos que começam com "usuarioId-"
                .filter(key -> key.startsWith(usuarioId + "-"))
                .map(key -> {
                    // Extrai o ID da trilha da chave composta
                    Long trilhaId = Long.parseLong(key.split("-")[1]);
                    return findById(trilhaId);           // Busca a trilha completa
                })
                .filter(trilha -> trilha != null)       // Remove nulls (trilha deletada)
                .collect(Collectors.toList());           // Coleta em lista
    }

  
    public List<Avaliacao> getAvaliacoesByTrilhaId(Long trilhaId) {
        Trilha trilha = findById(trilhaId);
        
        if (trilha != null) {
            return avaliacaoService.findByTrilhaId(trilhaId);
        }
        
        return new ArrayList<>(); //lista vazia se trilha nao existir
    }
    
   
    public Double getMediaAvaliacoes(Long trilhaId) {
        return avaliacaoService.getMediaByTrilhaId(trilhaId);
    }

   
    public void deleteById(Long id) {
        trilhas.removeIf(trilha -> trilha.getId().equals(id));
    }
}