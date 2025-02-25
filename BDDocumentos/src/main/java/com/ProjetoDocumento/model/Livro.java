package com.ProjetoDocumento.model;

import com.ProjetoDocumento.dto.LivroDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    private String id;

    private String titulo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    private String ano;  // Ano como String

    private String categoria;

    public Livro() {
    }

    public Livro(LivroDTO livroDTO) {
        this.id = livroDTO.id();
        this.titulo = livroDTO.titulo();
        this.autor = new Autor(livroDTO.autor());
        this.ano = livroDTO.anoPublicacao();
        this.categoria = livroDTO.categoria();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getAnoPublicacao() {
        return ano;
    }

    public void setAnoPublicacao(String ano) {
        this.ano = ano;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }
}