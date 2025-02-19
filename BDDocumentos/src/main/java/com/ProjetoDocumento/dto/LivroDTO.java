package com.ProjetoDocumento.dto;

import com.ProjetoDocumento.model.Livro;

public record LivroDTO(String id, AutorDTO autor, int anoPublicacao, String titulo, String categoria) {
	
	public LivroDTO(Livro livro) {
		this(livro.getId(), new AutorDTO(livro.getAutor()), livro.getAnoPublicacao(), livro.getTitulo(),livro.getCategoria());
	}
}