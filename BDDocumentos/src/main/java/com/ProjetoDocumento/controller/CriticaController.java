package com.ProjetoDocumento.controller;

import com.ProjetoDocumento.dao.CriticaDAO;
import com.ProjetoDocumento.dao.CriticaDAOMongoDB;
import com.ProjetoDocumento.dto.CriticaDTO;
import com.ProjetoDocumento.dto.LivroDTO;

import java.util.List;

public class CriticaController {

    private CriticaDAO criticaDAO;

    public CriticaController() {
        this.criticaDAO = new CriticaDAOMongoDB();
    }

    public CriticaDTO salvarCritica(CriticaDTO criticaDTO) throws Exception {
        return criticaDAO.salvar(criticaDTO);
    }

    public List<CriticaDTO> buscarCriticasPorLivro(LivroDTO livroDTO) throws Exception {
        return criticaDAO.buscarPorLivro(livroDTO);
    }

    public void excluirCritica(CriticaDTO criticaDTO) throws Exception {
        criticaDAO.excluir(criticaDTO);
    }

    public CriticaDTO buscarCriticaPorId(CriticaDTO criticaDTO) throws Exception {
        return criticaDAO.buscarPorId(criticaDTO);
    }
}