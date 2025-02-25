package com.ProjetoDocumento.dao;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.ProjetoDocumento.API.APIMySQL;
import com.ProjetoDocumento.ConexaoBD.ConexaoMySQL;
import com.ProjetoDocumento.dto.AutorDTO;
import com.ProjetoDocumento.dto.LivroDTO;
import com.ProjetoDocumento.model.Livro;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

public class LivroDAOMySQL implements LivroDAO{

    private EntityManagerFactory entityManagerFactory;
    private APIMySQL api;

    public LivroDAOMySQL() {
        this.entityManagerFactory = ConexaoMySQL.getInstancia().getFactory();
        this.api= new APIMySQL();
    }

    @Override
    public void salvar(LivroDTO livro) throws Exception{
        EntityManager entityManager = entityManagerFactory.createEntityManager();
	    try {
	        entityManager.getTransaction().begin();
	        Livro novoLivro = new Livro(livro);
	        entityManager.persist(novoLivro);
	        entityManager.getTransaction().commit();
	    } catch (Exception e) {
	        entityManager.getTransaction().rollback();
	        throw e;
	    } finally {
	        entityManager.close();
	    }
    }
    
    @Override
    public List<LivroDTO> buscarPorTitulo(LivroDTO livro) throws Exception{
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Livro> resultList;
        try {
            entityManager.getTransaction().begin();
            TypedQuery<Livro> query = entityManager.createQuery(
                    "SELECT l FROM Livro l WHERE l.autor.nome LIKE :titulo", Livro.class);
            query.setParameter("titulo", livro.titulo());
        
            resultList = query.getResultList();
            entityManager.getTransaction().commit();
            if(resultList.isEmpty()){
                api.buscarLivros(livro.titulo());
            }
            TypedQuery<Livro> query2 = entityManager.createQuery(
                    "SELECT l FROM Livro l WHERE l.autor.nome LIKE %:titulo%", Livro.class);
            query.setParameter("titulo", livro.titulo());
            resultList = query.getResultList();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }
        return resultList.stream().map(LivroDTO::new).collect(Collectors.toList());
    }
    @Override
    public List<LivroDTO> buscarPorAutor(AutorDTO autor) throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Livro> resultList;
        try {
            entityManager.getTransaction().begin();
            TypedQuery<Livro> query = entityManager.createQuery(
	                "SELECT l FROM Livro l WHERE l.autor.nome = :nomeAutor", Livro.class);
	        query.setParameter("nomeAutor", autor.nome());

	        resultList = query.getResultList();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }
        return resultList.stream().map(LivroDTO::new).collect(Collectors.toList());
    }

    public List<LivroDTO> listarTodosLivros() throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Livro> resultList;
        try {
            entityManager.getTransaction().begin();
            TypedQuery<Livro> query = entityManager.createQuery(
                    "SELECT l FROM Livro l", Livro.class);

            resultList = query.getResultList();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }
        return resultList.stream().map(LivroDTO::new).collect(Collectors.toList());
    }
    private static void calcularTempoDeProcessamento() {
        long startTime = System.currentTimeMillis();

        // Adicionar um intervalo entre as requisições
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Tempo de processamento: " + elapsedTime + " ms");
    }
}