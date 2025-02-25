package com.ProjetoDocumento.dto;

import com.ProjetoDocumento.model.Autor;

public record AutorDTO(String id, String nome) {
    public AutorDTO(Autor autor){

        this(autor.getId(), autor.getNome());
    }
    public AutorDTO(String id, String nome) {
        this.id = id;
    this.nome = nome;
    }
    @Override
    public String toString() {
        return "ID: " + id + ", Nome: " + nome;
    }
}
