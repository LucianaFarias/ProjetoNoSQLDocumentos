package com.ProjetoDocumento.model;

import com.ProjetoDocumento.dto.AutorDTO;
import org.bson.Document;

public class Autor {

    private int id;
    private String nome;

    public Autor() {

    }

    public Autor(AutorDTO dto) {
        this.id = dto.id();
        this.nome = dto.nome();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Método para converter Autor para um Document do MongoDB
    public Document toDocument() {
        Document document = new Document();
        document.append("id", id);
        document.append("nome", nome);
        return document;
    }
}