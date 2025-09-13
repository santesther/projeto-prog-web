package br.edu.iff.ccc.projetoprogweb.service;

import br.edu.iff.ccc.projetoprogweb.entities.Avaliacao;
import br.edu.iff.ccc.projetoprogweb.entities.Trilha;
import br.edu.iff.ccc.projetoprogweb.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AvaliacaoService {
    
    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    public Avaliacao criarAvaliacao(Trilha trilha, Long usuarioId, int nota, String comentario) {
        if (nota < 1 || nota > 5) {
            throw new AvaliacaoInvalidaException("A nota deve estar entre 1 e 5");
        }
        
        Avaliacao novaAvaliacao = new Avaliacao();
        novaAvaliacao.setTrilha(trilha);
        novaAvaliacao.setUsuarioId(usuarioId);
        novaAvaliacao.setNota(nota);
        novaAvaliacao.setComentario(comentario);
        novaAvaliacao.setDataAvaliacao(LocalDateTime.now());
        
        return save(novaAvaliacao);
    }

    public class AvaliacaoInvalidaException extends RuntimeException {
        public AvaliacaoInvalidaException(String message) {
            super(message);
        }
    }
    
    public Avaliacao save(Avaliacao avaliacao) {
        return avaliacaoRepository.save(avaliacao);
    }

    public List<Avaliacao> findAll() {
        return avaliacaoRepository.findAll();
    }
    
    public Avaliacao findById(Long id) {
        Optional<Avaliacao> avaliacao = avaliacaoRepository.findById(id);
        return avaliacao.orElse(null);
    }
   
    public List<Avaliacao> findByTrilhaId(Long trilhaId) {
        return avaliacaoRepository.findByTrilha_Id(trilhaId);
    }

    public List<Avaliacao> findByUsuarioId(Long usuarioId) {
        return avaliacaoRepository.findByUsuarioId(usuarioId);
    }
    
    public Double getMediaByTrilhaId(Long trilhaId) {
        Double media = avaliacaoRepository.findMediaByTrilhaId(trilhaId);
        return media != null ? media : 0.0;
    }
    
    public Avaliacao atualizarAvaliacao(Long id, int nota, String comentario) {
        Avaliacao avaliacao = findById(id);
        if (avaliacao != null) {
            // apenas campos permitidos
            avaliacao.setNota(nota);                   
            avaliacao.setComentario(comentario);      
            
            // Não atualiza data, usuário ou trilha 
            
            return save(avaliacao);                      
        }
        return null;
    }
    
    public void deleteById(Long id) {
        avaliacaoRepository.deleteById(id);
    }
    
    public void excluir(Long id) {
        deleteById(id);                               
    }
    
    public Long countByTrilhaId(Long trilhaId) {
        return avaliacaoRepository.countByTrilha_Id(trilhaId);
    }
    
    public List<Avaliacao> findByNotaGreaterThan(int nota) {
        return avaliacaoRepository.findByNotaGreaterThan(nota);
    }
    
    public List<Avaliacao> findAllOrderByDataDesc() {
        return avaliacaoRepository.findAllOrderByDataDesc();
    }
}