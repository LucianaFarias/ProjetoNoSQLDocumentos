package com.ProjetoDocumento.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;

public class FormularioNovaCritica extends JPanel{

    private JTextArea caixaTexto;
    private JLabel titulo;
    private JButton cancelar;
    private JButton salvar;

    public JTextArea getCaixaTexto() {
        return caixaTexto;
    }


    public void setCaixaTexto(JTextArea caixaTexto) {
        this.caixaTexto = caixaTexto;
    }


    public JLabel getTitulo() {
        return titulo;
    }


    public void setTitulo(JLabel titulo) {
        this.titulo = titulo;
    }


    public JButton getCancelar() {
        return cancelar;
    }


    public void setCancelar(JButton cancelar) {
        this.cancelar = cancelar;
    }


    public JButton getSalvar() {
        return salvar;
    }


    public void setSalvar(JButton salvar) {
        this.salvar = salvar;
    }


    public FormularioNovaCritica() {
        setBounds(0, 0, 800, 300);
        
    }


    public void exibirFormularioNovaCritica(){
        setLayout(new BorderLayout(0,5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setOpaque(false);


        titulo = new JLabel("Nova crítica");
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        add(titulo, BorderLayout.NORTH);

        
        caixaTexto = new JTextArea();
        caixaTexto.setBounds(10, 10, 600, 250);
        caixaTexto.setOpaque(true);
        caixaTexto.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        caixaTexto.setLineWrap(true);
        caixaTexto.setEditable(true);
        caixaTexto.setVisible(true);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(caixaTexto);
        add(scrollPane, BorderLayout.CENTER);

        cancelar = new JButton("Cancelar");
        cancelar.setBounds(700, 300, 100, 40);

        salvar = new JButton("Salvar");
        salvar.setBounds(600, 300, 100, 40);

        JPanel espacoBotoes = new JPanel();
        espacoBotoes.setOpaque(false);
        espacoBotoes.add(cancelar);
        espacoBotoes.add(salvar);
        add(espacoBotoes, BorderLayout.SOUTH);

        setVisible(true);
    }

}
