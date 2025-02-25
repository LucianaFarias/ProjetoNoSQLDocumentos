package com.ProjetoDocumento.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JEditorPane;
import javax.swing.SwingConstants;

import com.ProjetoDocumento.dto.CriticaDTO;

public class CaixaDetalhesCritica extends JEditorPane{
    
    private CriticaDTO critica;

    public CaixaDetalhesCritica(CriticaDTO critica){
        this.critica = critica;
        formatar();
        preencher();
        setVisible(true);
    }
    
    public void formatar(){
        setPreferredSize(new Dimension(200, 300));
        setAlignmentY(SwingConstants.CENTER); 
        setEditable(false);
		setCaretColor(getBackground());
		setDisabledTextColor(Color.WHITE);
    }

    public void preencher(){
        if(critica != null){
            Font fonteNormal = new Font("Arial", Font.PLAIN, 12);

            setText("Livro: " + critica.livro().titulo()+"\n"
                    + "Critica: " + critica.conteudo());
            setFont(fonteNormal);
        }
    }
}
