package com.ProjetoDocumento.main;

import com.ProjetoDocumento.controller.LivroController;
import com.ProjetoDocumento.dto.LivroDTO;

public class TesteMySQL {
    public static void main(String[] args) {
        LivroController livro= new LivroController();
        livro.mudarParaMySQL();

        LivroDTO teste = new LivroDTO(null, null,null,"noite",null );
        try {
            livro.buscarLivrosPorTitulo(teste);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
