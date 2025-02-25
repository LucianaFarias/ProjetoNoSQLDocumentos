package com.ProjetoDocumento.dao;

import java.util.List;
import java.util.stream.Collectors;

import com.ProjetoDocumento.ConexaoBD.ConexaoMySQL;
import com.ProjetoDocumento.dto.AutorDTO;
import com.ProjetoDocumento.dto.CriticaDTO;
import com.ProjetoDocumento.dto.LivroDTO;
import com.ProjetoDocumento.model.Critica;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

public class CriticaDAOMySQL implements CriticaDAO{

    private EntityManagerFactory entityManagerFactory;

    public CriticaDAOMySQL() {

        this.entityManagerFactory = ConexaoMySQL.getInstancia().getFactory();
    }
    @Override
    public CriticaDTO salvar(CriticaDTO criticaDTO) throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // Iniciar transação
            entityManager.getTransaction().begin();

            // Converter CriticaDTO para Critica (entidade)
            Critica critica = new Critica(criticaDTO);

            // Persistir a crítica no banco de dados
            entityManager.persist(critica);

            // Commit da transação
            entityManager.getTransaction().commit();

            // Converter a entidade Livro para LivroDTO (presumindo que você tenha um método para isso)
            // Converter Autor para AutorDTO
            AutorDTO autorDTO = new AutorDTO(critica.getLivro().getAutor().getId(), critica.getLivro().getAutor().getNome());

            // Criar LivroDTO com AutorDTO
            LivroDTO livroDTO = new LivroDTO(critica.getLivro().getId(), autorDTO, critica.getLivro().getAnoPublicacao(),
                    critica.getLivro().getTitulo(), critica.getLivro().getCategoria());

            // Criar CriticaDTO com LivroDTO
            CriticaDTO criticaSalvaDTO = new CriticaDTO(critica.getId(), critica.getConteudo(), livroDTO);

            // Retornar o DTO
            return criticaSalvaDTO;
        } catch (Exception e) {
            // Rollback em caso de erro
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            // Fechar o EntityManager
            entityManager.close();
        }
    }
    @Override
    public List<CriticaDTO> buscarPorLivro(LivroDTO livro) throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Critica> resultList;
        try {
            entityManager.getTransaction().begin();
            TypedQuery<Critica> query = entityManager.createQuery(
	                "SELECT c FROM Critica c WHERE c.livro.id = :idLivro", Critica.class);
	        query.setParameter("idLivro", livro.id());

	        resultList = query.getResultList();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }
        return resultList.stream().map(CriticaDTO::new).collect(Collectors.toList());
    }

    @Override
    public void excluir(CriticaDTO critica) throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            Critica criticaEncontrada = entityManager.find(Critica.class, critica.id());
            entityManager.remove(criticaEncontrada);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public CriticaDTO buscarPorId(CriticaDTO id) {
        return null;
    }

}