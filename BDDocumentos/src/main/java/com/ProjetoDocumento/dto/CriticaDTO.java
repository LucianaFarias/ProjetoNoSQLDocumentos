package com.ProjetoDocumento.dto;

 import com.ProjetoDocumento.model.Critica;

    public record CriticaDTO(Long id, String conteudo, LivroDTO livro) {
        public CriticaDTO(Critica critica) {
            this(critica.getId(), critica.getConteudo(), new LivroDTO(critica.getLivro()));
        }
    }
