package com.ProjetoDocumento.view;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import java.awt.*;
import java.awt.event.*;

import com.ProjetoDocumento.dto.LivroDTO;

public class CaixaDetalhesLivro extends JButton{

    private LivroDTO livro;

    public CaixaDetalhesLivro(LivroDTO livro){
        this.livro = livro;
        formatar();
        preencher();
        setVisible(true);
    }
    
    public void formatar(){
        setPreferredSize(new Dimension(200, 300));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentY(SwingConstants.CENTER); 
        setBackground(Color.YELLOW);
    }

    public void preencher(){
        if(livro != null){
            Font fonteTitulo = new Font("Arial", Font.BOLD, 20);
            Font fonteNormal = new Font("Arial", Font.PLAIN, 12);

            JLabel titulo = new JLabel(livro.titulo());
            titulo.setFont(fonteTitulo);
            
            JLabel autor = new JLabel("Autor: "+livro.autor().nome());
            JLabel ano = new JLabel("Ano publicação: "+livro.anoPublicacao());
            JLabel categoria = new JLabel("Categoria: "+livro.categoria());
            autor.setFont(fonteNormal);
            ano.setFont(fonteNormal);
            categoria.setFont(fonteNormal);

            add(titulo);
            add(autor);
            add(ano);
            add(categoria);
            
        }
    }

    public LivroDTO destacarLivro(Color cor){
        setVisible(false);
        setBackground(cor);
        setVisible(true);
        return livro;
    }

    public LivroDTO getLivro() {
        return livro;
    }
    
    public void setLivro(LivroDTO livro) {
        this.livro = livro;
    }

}
