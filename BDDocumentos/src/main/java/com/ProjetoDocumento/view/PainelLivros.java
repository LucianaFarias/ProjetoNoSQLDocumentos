package com.ProjetoDocumento.view;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.*;

import com.ProjetoDocumento.dto.LivroDTO;

public class PainelLivros extends JPanel {

    private JPanel listaLivros;
    private LivroDTO livroDestacado;
    private TelaCriticas tela;
    private Color corPadrao;
    private Color corDestaque;


    public Color getCorDestaque() {
        return corDestaque;
    }

    public void setCorDestaque(Color corDestaque) {
        this.corDestaque = corDestaque;
    }

    public PainelLivros(TelaCriticas tela) {
        this.tela = tela;
        setBounds(0, 0, 600, 400);
        setOpaque(false);
        setLayout(new BorderLayout());
        
    }
    
    public void exibirLivros(List<LivroDTO> livros){
        listaLivros = new JPanel();
        listaLivros.setBounds(0, 0, 600, 300);
        int quantidadeLivros = 5;
        
        if(livros.size()>quantidadeLivros){
            quantidadeLivros = livros.size();
        }
        livroDestacado = livros.get(0);
        
        listaLivros.setLayout(new GridLayout(1, quantidadeLivros, 0, 0));
        for(LivroDTO livro: livros){
            CaixaDetalhesLivro detalhesLivro = new CaixaDetalhesLivro(livro);
            if(corPadrao != null){
                detalhesLivro.setBackground(corPadrao);
            } 
            detalhesLivro.addActionListener(tela);
            listaLivros.add(detalhesLivro);
        }
        
        JScrollPane painelBarraDeRolagem = new JScrollPane();
        painelBarraDeRolagem.setViewportView(listaLivros);
        listaLivros.setVisible(true);
        painelBarraDeRolagem.setPreferredSize(this.getSize());
        painelBarraDeRolagem.setVisible(true);
        add(painelBarraDeRolagem, BorderLayout.CENTER);
        setVisible(true);
        
    }

    public void selecionarLivro(CaixaDetalhesLivro caixa){
        for(Component component: listaLivros.getComponents()){
            if(component instanceof CaixaDetalhesLivro){
                component = ((CaixaDetalhesLivro) component);
                component.setBackground(corPadrao);
            }
        }
        livroDestacado = caixa.destacarLivro(corDestaque); 
        repaint();
    }
    
    public LivroDTO getLivroDestacado() {
        return livroDestacado;
    }
    
    public void setLivroDestacado(LivroDTO livroDestacado) {
        this.livroDestacado = livroDestacado;
    }
    
    public Color getCorPadrao() {
        return corPadrao;
    }
    
    public void setCorPadrao(Color corPadrao) {
        this.corPadrao = corPadrao;
    }
    
}

