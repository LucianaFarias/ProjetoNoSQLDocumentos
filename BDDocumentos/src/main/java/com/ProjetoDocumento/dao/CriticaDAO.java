package com.ProjetoDocumento.dao;

import java.util.List;

import com.ProjetoDocumento.dto.CriticaDTO;
import com.ProjetoDocumento.dto.LivroDTO;

public interface CriticaDAO {

    public CriticaDTO salvar(CriticaDTO critica) throws Exception;
    public List<CriticaDTO> buscarPorLivro(LivroDTO livro)throws Exception;
    public void excluir(CriticaDTO critica) throws Exception;
    public CriticaDTO buscarPorId(CriticaDTO id);

}