package com.ProjetoDocumento.model;

import com.ProjetoDocumento.dto.CriticaDTO;

public class Critica {

    private Long id;
    private String conteudo;
    private Livro livro;

    public Critica() {

    }

    public Critica(CriticaDTO criticaDTO) {
        this.id = criticaDTO.id();
        this.conteudo = criticaDTO.conteudo();
        this.livro = new Livro(criticaDTO.livro());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }
}
