package com.ProjetoDocumento.dto;

import com.ProjetoDocumento.model.Critica;

public record CriticaDTO(String id, String conteudo, LivroDTO livro) {
    public CriticaDTO(Critica critica) {
        this(critica.getId(), critica.getConteudo(), new LivroDTO(critica.getLivro()));
    }
    public CriticaDTO(String id, String conteudo, LivroDTO livro) {
        this.id = id;
        this.conteudo = conteudo;
        this.livro = livro;
    }
}