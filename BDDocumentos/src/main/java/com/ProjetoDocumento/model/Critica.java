package com.ProjetoDocumento.model;

import com.ProjetoDocumento.dto.CriticaDTO;
import jakarta.persistence.*;
@Entity
@Table(name = "criticas")
public class Critica {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 2000)
    private String conteudo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "livro_id")
    private Livro livro;

    public Critica() {
    }

    public Critica(CriticaDTO criticaDTO) {
        this.id = criticaDTO.id();
        this.conteudo = criticaDTO.conteudo();
        this.livro = new Livro(criticaDTO.livro());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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