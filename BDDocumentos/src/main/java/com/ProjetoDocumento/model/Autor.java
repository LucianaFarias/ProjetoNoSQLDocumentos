package com.ProjetoDocumento.model;

import com.ProjetoDocumento.dto.AutorDTO;

import jakarta.persistence.*;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nome;

    public Autor(AutorDTO dto) {
        this.id = dto.id();
        this.nome = dto.nome();
    }

    public Autor() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}