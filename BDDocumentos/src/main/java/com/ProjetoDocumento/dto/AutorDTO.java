package com.ProjetoDocumento.dto;

import com.ProjetoDocumento.model.Autor;

public record AutorDTO(int id, String nome) {
    public AutorDTO(Autor autor){
        this(autor.getId(), autor.getNome());
    }
}
