package br.edu.iff.ccc.projetoprogweb.repository;

import br.edu.iff.ccc.projetoprogweb.entities.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    
    List<Avaliacao> findByTrilha_Id(Long trilhaId);
    
    List<Avaliacao> findByUsuarioId(Long usuarioId);
    
    List<Avaliacao> findByNotaGreaterThan(int nota);
    
    Long countByTrilha_Id(Long trilhaId);
    
    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.trilha.id = :trilhaId")
    Double findMediaByTrilhaId(@Param("trilhaId") Long trilhaId);
    
    @Query("SELECT a FROM Avaliacao a ORDER BY a.dataAvaliacao DESC")
    List<Avaliacao> findAllOrderByDataDesc();
}