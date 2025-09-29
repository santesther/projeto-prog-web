package br.edu.iff.ccc.projetoprogweb.repository;

import br.edu.iff.ccc.projetoprogweb.entities.Trilha;
import br.edu.iff.ccc.projetoprogweb.entities.NivelDificuldade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface TrilhaRepository extends JpaRepository<Trilha, Long> {
    
    /**
    Busca trilha por nome exato
     * Spring Data JPA gera automaticamente: SELECT * FROM trilha WHERE nome = ?
     */
    Trilha findByNome(String nome);
    
    /**
     Busca trilhas por dificuldade
     * Spring Data JPA gera automaticamente: SELECT * FROM trilha WHERE dificuldade = ?
     */
    List<Trilha> findByDificuldade(NivelDificuldade dificuldade);
    
    /**
     Busca trilhas por localização
     * Spring Data JPA gera automaticamente: SELECT * FROM trilha WHERE localizacao = ?
     */
    List<Trilha> findByLocalizacao(String localizacao);
    
    /**
     Busca trilhas por nome (busca parcial, case-insensitive)
     * Spring Data JPA gera automaticamente: SELECT * FROM trilha WHERE LOWER(nome) LIKE LOWER(CONCAT('%', ?, '%'))
     */
    List<Trilha> findByNomeContainingIgnoreCase(String nome);

    /**
     * @QUERY CUSTOMIZADA: Busca trilhas por duração máxima
     */
    @Query("SELECT t FROM Trilha t WHERE t.duracaoEstimada <= ?1")
    List<Trilha> findByDuracaoMaxima(Integer duracaoMaxima);
    
    /**
     * @QUERY CUSTOMIZADA: Busca trilhas por distância máxima
     */
    @Query("SELECT t FROM Trilha t WHERE t.distancia <= ?1")
    List<Trilha> findByDistanciaMaxima(Double distanciaMaxima);

    /**
     * @QUERY CUSTOMIZADA COMPLEXA: Busca trilhas por múltiplos critérios
     */
    @Query("SELECT t FROM Trilha t WHERE " +
           "(:nome IS NULL OR LOWER(t.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
           "(:dificuldade IS NULL OR t.dificuldade = :dificuldade) AND " +
           "(:localizacao IS NULL OR LOWER(t.localizacao) LIKE LOWER(CONCAT('%', :localizacao, '%')))")
    List<Trilha> findByMultiplosCriterios(
        @Param("nome") String nome,
        @Param("dificuldade") NivelDificuldade dificuldade,
        @Param("localizacao") String localizacao
    );

    /**
     * @QUERY CUSTOMIZADA: Busca trilhas dentro de um range de distância
     */
    @Query("SELECT t FROM Trilha t WHERE t.distancia BETWEEN :distanciaMin AND :distanciaMax")
    List<Trilha> findByDistanciaRange(
        @Param("distanciaMin") Double distanciaMin, 
        @Param("distanciaMax") Double distanciaMax
    );

    /**
     * @QUERY CUSTOMIZADA: Busca trilhas dentro de um range de duração
     */
    @Query("SELECT t FROM Trilha t WHERE t.duracaoEstimada BETWEEN :duracaoMin AND :duracaoMax")
    List<Trilha> findByDuracaoRange(
        @Param("duracaoMin") Integer duracaoMin, 
        @Param("duracaoMax") Integer duracaoMax
    );

    /**
     * @QUERY CUSTOMIZADA: Busca trilhas por dificuldade e distância máxima
     */
    @Query("SELECT t FROM Trilha t WHERE t.dificuldade = :dificuldade AND t.distancia <= :distanciaMax")
    List<Trilha> findByDificuldadeAndDistanciaMax(
        @Param("dificuldade") NivelDificuldade dificuldade,
        @Param("distanciaMax") Double distanciaMax
    );

    /**
     Trilhas ordenadas por avaliação média
     * - LEFT JOIN entre entidades relacionadas
     * - GROUP BY para agregação
     * - Função AVG() para cálculo de média
     * - COALESCE para tratar valores nulos
     * - ORDER BY para ordenação
     */
    @Query("SELECT t FROM Trilha t LEFT JOIN t.avaliacoes a " +
           "GROUP BY t.id, t.nome, t.descricao, t.localizacao, t.dificuldade, t.distancia, t.duracaoEstimada, t.autorId, t.autorNome " +
           "ORDER BY COALESCE(AVG(a.nota), 0) DESC")
    List<Trilha> findAllOrderByAvaliacaoMedia();

    /**
     * @QUERY COM INNER JOIN: Busca trilhas com pelo menos uma avaliação
     */
    @Query("SELECT DISTINCT t FROM Trilha t INNER JOIN t.avaliacoes a")
    List<Trilha> findTrilhasComAvaliacoes();

    /**
     * @QUERY COM SUBCONSULTA: Busca trilhas sem avaliações
     */
    @Query("SELECT t FROM Trilha t WHERE t.avaliacoes IS EMPTY")
    List<Trilha> findTrilhasSemAvaliacoes();

    /**
     * Trilhas por autor ordenadas por número de avaliações
     * - LEFT JOIN
     * - Filtro por parâmetro
     * - GROUP BY
     * - Função COUNT() para contagem
     * - ORDER BY com agregação
     */
    @Query("SELECT t FROM Trilha t LEFT JOIN t.avaliacoes a WHERE t.autorId = :autorId " +
           "GROUP BY t.id, t.nome, t.descricao, t.localizacao, t.dificuldade, t.distancia, t.duracaoEstimada, t.autorId, t.autorNome " +
           "ORDER BY COUNT(a) DESC")
    List<Trilha> findByAutorIdOrderByAvaliacoesCount(@Param("autorId") Long autorId);

    /**
     * @QUERY COM FUNÇÃO SIZE: Busca trilhas populares
     */
    @Query("SELECT t FROM Trilha t WHERE SIZE(t.avaliacoes) >= :minAvaliacoes")
    List<Trilha> findTrilhasPopulares(@Param("minAvaliacoes") int minAvaliacoes);

    /**
     * Busca por texto livre em múltiplos campos
     * - Múltiplos campos de busca (nome, descrição, localização)
     * - Operador OR para busca em qualquer campo
     * - LOWER() e LIKE para busca case-insensitive
     * - CONCAT() para construção de padrão de busca
     */
    @Query("SELECT t FROM Trilha t WHERE " +
           "LOWER(t.nome) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(t.descricao) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(t.localizacao) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Trilha> findByTextoLivre(@Param("texto") String texto);
}
