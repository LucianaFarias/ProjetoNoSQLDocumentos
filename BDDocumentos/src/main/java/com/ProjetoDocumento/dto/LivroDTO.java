package com.ProjetoDocumento.dto;

import com.ProjetoDocumento.model.Autor;
import com.ProjetoDocumento.model.Livro;

public record LivroDTO(String id, AutorDTO autor, String anoPublicacao, String titulo, String categoria) {

	public LivroDTO(Livro livro) {
		this(livro.getId(), new AutorDTO(livro.getAutor()), livro.getAnoPublicacao(), livro.getTitulo(), livro.getCategoria());
	}
	public LivroDTO(String id, AutorDTO autor,String anoPublicacao, String titulo, String categoria){
		this.id = id;
		this.autor = autor;
		this.anoPublicacao = anoPublicacao;
		this.titulo = titulo;
		this.categoria = categoria;
	}
}