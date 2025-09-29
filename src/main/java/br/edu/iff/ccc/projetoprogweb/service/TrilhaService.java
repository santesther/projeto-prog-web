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
    
    /**
     * Spring automaticamente injeta uma instância do TrilhaRepository
     * configurado com JPA para acesso aos dados
     */
    @Autowired
    private TrilhaRepository trilhaRepository;
    
    /**
     * Formato da chave: "usuarioId-trilhaId" (ex: "1-5")
     */
    private List<String> favoritos = new ArrayList<>(); 
    
    /**
     * Permite que TrilhaService use funcionalidades do AvaliacaoService
     */
    @Autowired
    private AvaliacaoService avaliacaoService;
   
    /**
     * 1. Chama trilhaRepository.findAll()
     * 2. Spring Data JPA gera automaticamente: SELECT * FROM trilha
     * 3. Hibernate converte ResultSet em objetos Trilha
     * 4. Retorna List<Trilha> com todas as entidades
     */
    public List<Trilha> findAll() {
        return trilhaRepository.findAll();
    }
    
    public Trilha save(Trilha trilha) {
        return trilhaRepository.save(trilha);
    }
    
    public Trilha findById(Long id) {
        return trilhaRepository.findById(id)
            .orElseThrow(() -> new TrilhaNaoEncontrada("Trilha com Id " + id + " não encontrada."));
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

    public List<Trilha> findByDistanciaMaxima(Double distanciaMaxima) {
        return trilhaRepository.findByDistanciaMaxima(distanciaMaxima);
    }

    public List<Trilha> findByMultiplosCriterios(String nome, NivelDificuldade dificuldade, String localizacao) {
        return trilhaRepository.findByMultiplosCriterios(nome, dificuldade, localizacao);
    }

    public List<Trilha> findByDistanciaRange(Double distanciaMin, Double distanciaMax) {
        return trilhaRepository.findByDistanciaRange(distanciaMin, distanciaMax);
    }

    public List<Trilha> findByDuracaoRange(Integer duracaoMin, Integer duracaoMax) {
        return trilhaRepository.findByDuracaoRange(duracaoMin, duracaoMax);
    }

    public List<Trilha> findByDificuldadeAndDistanciaMax(NivelDificuldade dificuldade, Double distanciaMax) {
        return trilhaRepository.findByDificuldadeAndDistanciaMax(dificuldade, distanciaMax);
    }

    /**
     Ordenação por avaliação média
     * 1. LEFT JOIN entre Trilha e Avaliacao
     * 2. GROUP BY agrupa trilhas
     * 3. AVG(a.nota) calcula média das notas
     * 4. COALESCE trata trilhas sem avaliação (retorna 0)
     * 5. ORDER BY DESC ordena da maior para menor média
     */
    public List<Trilha> findAllOrderByAvaliacaoMedia() {
        return trilhaRepository.findAllOrderByAvaliacaoMedia();
    }

    /**
     Trilhas que têm avaliações
     * 1. INNER JOIN só retorna trilhas que têm pelo menos uma avaliação
     * 2. DISTINCT evita duplicatas (trilha com múltiplas avaliações)
     * 3. Trilhas sem avaliação são excluídas automaticamente
     */
    public List<Trilha> findTrilhasComAvaliacoes() {
        return trilhaRepository.findTrilhasComAvaliacoes();
    }

    /**
     Trilhas sem avaliações
     * 1. IS EMPTY verifica se coleção de avaliações está vazia
     * 2. Hibernate traduz para NOT EXISTS ou LEFT JOIN com NULL
     * 3. Retorna apenas trilhas que não têm nenhuma avaliação
     */
    public List<Trilha> findTrilhasSemAvaliacoes() {
        return trilhaRepository.findTrilhasSemAvaliacoes();
    }

    /**
    Busca em múltiplos campos
     * 1. Busca o termo em nome, descrição e localização
     * 2. Operador OR: qualquer campo que contenha o termo
     * 3. LOWER() torna busca case-insensitive
     * 4. CONCAT('%', :texto, '%') cria padrão LIKE
     */
    public List<Trilha> findByTextoLivre(String texto) {
        return trilhaRepository.findByTextoLivre(texto);
    }

    /**
     Sistema de favoritos
     * 1. Cria chave composta: "usuarioId-trilhaId"
     * 2. Verifica se já existe na lista de favoritos
     * 3. Se existe: remove (desfavoritar)
     * 4. Se não existe: adiciona (favoritar)
     * 5. Implementa toggle (liga/desliga) em uma operação
     */
    public void toggleFavorito(Long usuarioId, Long trilhaId) {
        String favoritoKey = usuarioId + "-" + trilhaId; // Chave composta
        
        if (favoritos.contains(favoritoKey)) {
            favoritos.remove(favoritoKey); // Remove dos favoritos
        } else {
            favoritos.add(favoritoKey); // Adiciona aos favoritos
        }
    }

    /**
    Buscar favoritos do usuário
     * 1. Stream API para processar lista de favoritos
     * 2. filter(): mantém apenas chaves que começam com "usuarioId-"
     * 3. map(): extrai trilhaId da chave e busca trilha completa
     * 4. filter(): remove nulls (trilhas que foram deletadas)
     * 5. collect(): converte Stream de volta para List
     */
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

    /**
     Buscar avaliações da trilha
     * 1. TrilhaService não implementa lógica de avaliações
     * 2. Delega responsabilidade para AvaliacaoService
     * 3. Mantém separação de responsabilidades
     * 4. AvaliacaoService tem expertise em operações de avaliação
     */
    public List<Avaliacao> getAvaliacoesByTrilhaId(Long trilhaId) {
        return avaliacaoService.findByTrilhaId(trilhaId); // Delega para o serviço especializado
    }
    
    /**
    Calcular média de avaliações
     * Similar ao método anterior, delega cálculo de média para AvaliacaoService
     */
    public Double getMediaAvaliacoes(Long trilhaId) {
        return avaliacaoService.getMediaByTrilhaId(trilhaId); // Delega para o serviço especializado
    }

    /**
     Excluir trilha
     * 1. Spring Data JPA gera: DELETE FROM trilha WHERE id = ?
     * 2. Se trilha não existir, não lança exceção (comportamento padrão)
     * 3. Se há dependências (avaliações), pode lançar DataIntegrityViolationException
     * 4. GlobalAdvice captura e retorna ProblemDetail apropriado
     */
    public void deleteById(Long id) {
        trilhaRepository.deleteById(id);
    }
}
