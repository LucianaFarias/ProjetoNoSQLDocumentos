package com.ProjetoDocumento.dao;

import java.util.List;

import com.ProjetoDocumento.dto.AutorDTO;
import com.ProjetoDocumento.dto.LivroDTO;

public interface LivroDAO {

    public void salvar(LivroDTO livro) throws Exception;
    public List<LivroDTO> buscarPorTitulo(LivroDTO livro) throws Exception;
	public List<LivroDTO> buscarPorAutor(AutorDTO autor) throws Exception;
    public List<LivroDTO> listarTodosLivros() throws Exception;

}
