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

    
    /**
     * Busca avaliações por range de notas
     */
    @Query("SELECT a FROM Avaliacao a WHERE a.nota BETWEEN :notaMin AND :notaMax")
    List<Avaliacao> findByNotaRange(
        @Param("notaMin") int notaMin, 
        @Param("notaMax") int notaMax
    );

    /**
     * Busca avaliações com comentários
     */
    @Query("SELECT a FROM Avaliacao a WHERE a.comentario IS NOT NULL AND TRIM(a.comentario) != ''")
    List<Avaliacao> findAvaliacoesComComentarios();

    /**
     * Busca avaliações sem comentários
     */
    @Query("SELECT a FROM Avaliacao a WHERE a.comentario IS NULL OR TRIM(a.comentario) = ''")
    List<Avaliacao> findAvaliacoesSemComentarios();

    /**
     * Busca avaliações por trilha e usuário
     */
    @Query("SELECT a FROM Avaliacao a WHERE a.trilha.id = :trilhaId AND a.usuarioId = :usuarioId")
    List<Avaliacao> findByTrilhaIdAndUsuarioId(
        @Param("trilhaId") Long trilhaId, 
        @Param("usuarioId") Long usuarioId
    );

    /**
     * Busca estatísticas de avaliações por trilha
     */
    @Query("SELECT " +
           "COUNT(a) as total, " +
           "AVG(a.nota) as media, " +
           "MIN(a.nota) as notaMinima, " +
           "MAX(a.nota) as notaMaxima " +
           "FROM Avaliacao a WHERE a.trilha.id = :trilhaId")
    Object[] findEstatisticasByTrilhaId(@Param("trilhaId") Long trilhaId);

    /**
     * Busca top avaliações (melhores notas) por trilha
     */
    @Query("SELECT a FROM Avaliacao a WHERE a.trilha.id = :trilhaId ORDER BY a.nota DESC, a.dataAvaliacao DESC")
    List<Avaliacao> findTopAvaliacoesByTrilhaId(@Param("trilhaId") Long trilhaId);
}
