package com.ProjetoDocumento.controller;

import com.ProjetoDocumento.dao.*;
import com.ProjetoDocumento.dto.AutorDTO;
import com.ProjetoDocumento.dto.LivroDTO;

import java.util.List;

public class LivroController {

    private LivroDAO livroDAO;
    private CriticaDAO criticaDAO;

    public LivroController() {
        this.criticaDAO = new CriticaDAOMongoDB();
        this.livroDAO = new LivroDAOMongoDB();
    }

    public List<LivroDTO> buscarLivrosPorTitulo(LivroDTO livroDTO) throws Exception {
        return livroDAO.buscarPorTitulo(livroDTO);
    }

    public List<LivroDTO> buscarLivrosPorAutor(AutorDTO autorDTO) throws Exception {
        return livroDAO.buscarPorAutor(autorDTO);
    }

    public void salvarLivro(LivroDTO livroDTO) throws Exception {
        livroDAO.salvar(livroDTO);
    }
    public List<LivroDTO> listarTodosLivros() {
        try {
            List<LivroDTO> livros = livroDAO.listarTodosLivros();
            return livros;
        } catch (Exception e) {
            System.out.println("Erro ao listar livros: " + e.getMessage());
            return null;
        }
    }
    public void mudarParaMongoDB(){
        this.livroDAO=new LivroDAOMongoDB();
        this.criticaDAO= new CriticaDAOMongoDB();
    }
    public void mudarParaMySQL(){
        this.livroDAO=new LivroDAOMySQL();
        this.criticaDAO= new CriticaDAOMySQL();
    }
}