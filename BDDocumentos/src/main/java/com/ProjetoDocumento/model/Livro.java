package com.ProjetoDocumento.model;

import com.ProjetoDocumento.dto.LivroDTO;
import org.bson.Document;

public class Livro {

    private String id;
    private String titulo;
    private Autor autor;
    private int ano;
    private String categoria;

    public Livro() {
    }

    public Livro(LivroDTO livroDTO) {
        this.id = livroDTO.id();
        this.titulo = livroDTO.titulo();
        this.autor = new Autor(livroDTO.autor());
        this.ano = livroDTO.anoPublicacao();
        this.categoria=livroDTO.categoria();
    }

    public String getId() {
        return id;
    }

    public void setId(String isbn) {
        this.id = isbn;
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

    public int getAnoPublicacao() {
        return ano;
    }

    public void setAnoPublicacao(int ano) {
        this.ano = ano;
    }

    // Método para converter Livro para um Document do MongoDB
    public Document toDocument() {
        Document document = new Document();
        document.append("id", id);
        document.append("titulo", titulo);
        document.append("autor", autor != null ? autor.toDocument() : null);  // Garantindo que o autor seja convertido corretamente
        document.append("anoPublicacao", ano);
        return document;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }
}