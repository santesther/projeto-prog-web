package br.edu.iff.ccc.projetoprogweb.service;

import br.edu.iff.ccc.projetoprogweb.entities.Avaliacao;
import br.edu.iff.ccc.projetoprogweb.entities.Trilha;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AvaliacaoService {
    
    // Simula uma tabela de avaliações em memória
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    /**
     * Chamado pelo TrilhaService após @PostConstruct para garantir que
     * as dependências estejam injetadas.
     */
    public void initMockData(Trilha trilha) {
        if (trilha == null) return;                   
        
        Avaliacao avaliacao1 = new Avaliacao();
        avaliacao1.setId(1L);                           
        avaliacao1.setTrilha(trilha);                 
        avaliacao1.setUsuarioId(101L);                  
        avaliacao1.setUsuarioNome("Carlos Silva");     
        avaliacao1.setNota(5);                         
        avaliacao1.setComentario("Vista incrível! Valeu cada minuto.");
        avaliacao1.setDataAvaliacao(LocalDateTime.now()); 
        
        Avaliacao avaliacao2 = new Avaliacao();
        avaliacao2.setId(2L);
        avaliacao2.setTrilha(trilha);                 
        avaliacao2.setUsuarioId(102L);                 
        avaliacao2.setUsuarioNome("Ana Santos");
        avaliacao2.setNota(4);                        
        avaliacao2.setComentario("Trilha bem sinalizada, mas exige preparo físico.");
        avaliacao2.setDataAvaliacao(LocalDateTime.now().minusDays(1)); 
        
        avaliacoes.add(avaliacao1);                     // Adiciona à "tabela" de avaliações
        avaliacoes.add(avaliacao2);
        
        // Adiciona as avaliações à trilha também (navegação bidirecional)
        trilha.getAvaliacoes().add(avaliacao1);
        trilha.getAvaliacoes().add(avaliacao2);
        
        // Resultado: Trilha conhece suas avaliações E avaliações conhecem sua trilha
    }
    
    public Avaliacao criarAvaliacao(Trilha trilha, Long usuarioId, int nota, String comentario) {
        Avaliacao novaAvaliacao = new Avaliacao();
        novaAvaliacao.setTrilha(trilha);             
        novaAvaliacao.setUsuarioId(usuarioId);
        novaAvaliacao.setUsuarioNome("Usuário Demo");   // buscaria do UserService
        novaAvaliacao.setNota(nota);
        novaAvaliacao.setComentario(comentario);
        novaAvaliacao.setDataAvaliacao(LocalDateTime.now()); 
        
        // Adiciona à trilha também (mantém sincronização)
        trilha.adicionarAvaliacao(novaAvaliacao);       
        
        return save(novaAvaliacao);
    }
    
    public Avaliacao save(Avaliacao avaliacao) {
        if (avaliacao.getId() == null) {
            Long newId = getNextId();                   // Gera próximo ID disponível
            avaliacao.setId(newId);                     // Atribui ID
            avaliacao.setDataAvaliacao(LocalDateTime.now()); 
            avaliacoes.add(avaliacao);                  // Adiciona a tabela
        } else {
            for (int i = 0; i < avaliacoes.size(); i++) {
                if (avaliacoes.get(i).getId().equals(avaliacao.getId())) {
                    avaliacoes.set(i, avaliacao);       // Substitui a avaliação antiga
                    break;                              // Para depois de encontrar
                }
            }
        }
        return avaliacao;
    }
    
    private Long getNextId() {
        if (avaliacoes.isEmpty()) {
            return 1L;                                
        }
        
        return avaliacoes.stream()
                .mapToLong(Avaliacao::getId)           
                .max()                                  
                .orElse(0L) + 1L;                      
    }

  
    public List<Avaliacao> findAll() {
        return new ArrayList<>(avaliacoes);
    }
    
   
    public Avaliacao findById(Long id) {
        return avaliacoes.stream()
                .filter(avaliacao -> avaliacao.getId().equals(id))
                .findFirst()                            
                .orElse(null);                          
    }
   
    public List<Avaliacao> findByTrilhaId(Long trilhaId) {
        return avaliacoes.stream()
                .filter(avaliacao -> avaliacao.getTrilha().getId().equals(trilhaId))
                .collect(Collectors.toList());          
        
    }

    public List<Avaliacao> findByUsuarioId(Long usuarioId) {
        return avaliacoes.stream()
                .filter(avaliacao -> avaliacao.getUsuarioId().equals(usuarioId)) 
                .collect(Collectors.toList());
    }
    
    public Double getMediaByTrilhaId(Long trilhaId) {
        List<Avaliacao> avaliacoesTrilha = findByTrilhaId(trilhaId);
        
        if (avaliacoesTrilha.isEmpty()) {
            return 0.0;                               
        }
        
        double soma = 0;
        for (Avaliacao avaliacao : avaliacoesTrilha) {
            soma += avaliacao.getNota();               
        }
        
        return soma / avaliacoesTrilha.size();          // Média = soma / quantidade
    }
    
    public Avaliacao atualizarAvaliacao(Long id, int nota, String comentario) {
        Avaliacao avaliacao = findById(id);
        if (avaliacao != null) {
            // apenas campos permitidos
            avaliacao.setNota(nota);                   
            avaliacao.setComentario(comentario);      
            
            // Não atualiza data, usuário ou trilha 
            
            save(avaliacao);                      
        }
        return avaliacao;
    }
    
    public void deleteById(Long id) {
        avaliacoes.removeIf(avaliacao -> avaliacao.getId().equals(id));
    }
    
    public void excluir(Long id) {
        deleteById(id);                               
    }
}