package br.edu.iff.ccc.projetoprogweb.repository;

import br.edu.iff.ccc.projetoprogweb.entities.Trilha;
import br.edu.iff.ccc.projetoprogweb.entities.NivelDificuldade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrilhaRepository extends JpaRepository<Trilha, Long> {
    
    Trilha findByNome(String nome);
    
    List<Trilha> findByDificuldade(NivelDificuldade dificuldade);
    
    List<Trilha> findByLocalizacao(String localizacao);
    
    List<Trilha> findByAutorId(Long autorId);
    
    List<Trilha> findByNomeContainingIgnoreCase(String nome);
    
    @Query("SELECT t FROM Trilha t WHERE t.duracaoEstimada <= ?1")
    List<Trilha> findByDuracaoMaxima(Integer duracaoMaxima);
    
    @Query("SELECT t FROM Trilha t WHERE t.distancia <= ?1")
    List<Trilha> findByDistanciaMaxima(Double distanciaMaxima);
}
