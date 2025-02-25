package com.ProjetoDocumento.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.ProjetoDocumento.controller.CriticaController;
import com.ProjetoDocumento.controller.LivroController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.ProjetoDocumento.dto.AutorDTO;
import com.ProjetoDocumento.dto.CriticaDTO;
import com.ProjetoDocumento.dto.LivroDTO;

public class TelaCriticas extends JFrame implements ActionListener{

    private PainelLivros painelLivros;
    private PainelCriticas painelCriticas;
    private JPanel parteCentral;
    private JPanel parteLateral;
    private JTextField caixaPesquisa;
    private JButton botaoPesquisar;
    private JButton botaoLimparPesquisa;
    private FormularioNovaCritica formulario;
    private JButton botaoNovaCritica;
    private Color corPadrao;
    private LivroController livroController;
    private CriticaController criticaController;

    public TelaCriticas() {
        this.livroController = new LivroController();
        this.criticaController = new CriticaController();
        configurarFormato();
        criarPainelLateral();
        exibirLivros();
        criarCritica();
        criarPainelCriticas();
        exibirCriticas();
        setVisible(true);
        repaint();
    }
    
    public void configurarFormato(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 900);
        setPreferredSize(new Dimension(800, 600));
        setExtendedState(MAXIMIZED_BOTH);
        setResizable(false);
        setTitle("Críticas literárias");
        corPadrao = criarCor(255, 229, 204);
        getContentPane().setBackground(corPadrao);
        setLayout(new BorderLayout(5, 0));
        parteCentral = new JPanel();
        parteCentral.setLayout(new BorderLayout());
        add(parteCentral, BorderLayout.CENTER);
    }

    public Color criarCor(int red, int green, int blue){
        float[] cor = new float[3];
		cor = Color.RGBtoHSB(red, green, blue, cor);
		return Color.getHSBColor(cor[0], cor[1], cor[2]);
    }
    
    public void exibirLivros(){

        if(painelLivros != null){
            painelLivros.setVisible(false);
            parteCentral.remove(painelLivros);
        }
        
        painelLivros = new PainelLivros(this);
        painelLivros.setCorPadrao(corPadrao);
        painelLivros.setCorDestaque(criarCor(255, 102, 102));
        List<LivroDTO> livros = livroController.listarTodosLivros(); //chamar o método que pega todos os livros
        painelLivros.exibirLivros(livros);
        parteCentral.add(painelLivros, BorderLayout.NORTH);
    }
    public void exibirLivrosFiltrados(List<LivroDTO> livros){

        if(painelLivros != null){
            painelLivros.setVisible(false);
            parteCentral.remove(painelLivros);
        }
        
        painelLivros = new PainelLivros(this);
        painelLivros.setCorPadrao(corPadrao);
        painelLivros.setCorDestaque(criarCor(255, 102, 102));
        painelLivros.exibirLivros(livros);
        parteCentral.add(painelLivros, BorderLayout.NORTH);
    }
    
    public void exibirCriticas(){
        if(painelLivros.getLivroDestacado() != null){
    
            // Buscar as críticas do livro e atualizar lista exibida
            List<CriticaDTO> criticasDoLivro = new ArrayList<>();
            try {
                criticasDoLivro = criticaController.buscarCriticasPorLivro(painelLivros.getLivroDestacado());
                if(criticasDoLivro.size()>0){
                    painelCriticas.mudarCriticas(criticasDoLivro);
                }
                repaint();
            } catch (Exception e) {
                //throw new RuntimeException(e);
            }

        }
    }

    public void criarBotaoNovaCritica(){
        botaoNovaCritica = new JButton("Criar nova crítica");
        botaoNovaCritica.setBounds(10, 10, 200, 40);
        botaoNovaCritica.addActionListener(this);
        botaoNovaCritica.setVisible(true);
    }
    
    public void criarCaixaPesquisa(){
        caixaPesquisa = new JTextField();
        caixaPesquisa.setPreferredSize(new Dimension(200, 30));
        
        botaoPesquisar = new JButton("Pesquisar");
        botaoPesquisar.setBounds(1000, 220, 100, 30);
        botaoPesquisar.addActionListener(this);
        
        botaoLimparPesquisa = new JButton("Limpar pesquisa");
        botaoLimparPesquisa.setBounds(1000, 250, 100, 30);
        botaoLimparPesquisa.addActionListener(this);
        
    }
    
    public void criarPainelLateral(){
        parteLateral = new JPanel();
        parteLateral.setBounds(500, 10, 200, 600);
        parteLateral.setBorder(BorderFactory.createEmptyBorder(40, 5, 0, 5));
        parteLateral.setOpaque(true);
        parteLateral.setBackground(corPadrao);
        parteLateral.setLayout(new BoxLayout(parteLateral, BoxLayout.Y_AXIS));
        
        criarBotaoNovaCritica();
        parteLateral.add(botaoNovaCritica);
        
        criarCaixaPesquisa();
        JPanel areaPesquisa = new JPanel();
        
        areaPesquisa.add(new JLabel("Pesquisar pelo título ou autor"));
        
        areaPesquisa.setLayout(new BoxLayout(areaPesquisa, BoxLayout.Y_AXIS));
        areaPesquisa.setBounds(500, 40, 200, 100);
        areaPesquisa.setBackground(criarCor(255, 102, 102));
        areaPesquisa.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        areaPesquisa.add(caixaPesquisa);
        
        JPanel botoesPesquisa = new JPanel();
        botoesPesquisa.setBackground(criarCor(255, 102, 102));
        botoesPesquisa.add(botaoPesquisar);
        botoesPesquisa.add(botaoLimparPesquisa);
        areaPesquisa.add(botoesPesquisa);
        
        parteLateral.add(areaPesquisa);
        
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(areaPesquisa);
        parteLateral.add(panel);
        
        
        add(parteLateral, BorderLayout.EAST);
    }
    
    public void criarPainelCriticas(){
        
        if(painelLivros.getLivroDestacado() != null){


            List<CriticaDTO> criticasDoLivro = new ArrayList<>();
            try {
                System.out.println(painelLivros.getLivroDestacado().id());
                criticasDoLivro = criticaController.buscarCriticasPorLivro(painelLivros.getLivroDestacado());
                painelCriticas = new PainelCriticas();
                painelCriticas.setCorPadrao(painelLivros.getCorDestaque());
                if(criticasDoLivro.size() > 0 ){
                    painelCriticas.exibirCriticas(criticasDoLivro);
                }
                parteCentral.add(painelCriticas, BorderLayout.CENTER);
            } catch (Exception e) {
                //throw new RuntimeException(e);
            }


        }
    }

    public List<LivroDTO> filtrarLivrosPorTitulo(LivroDTO livro){
        try {
            return livroController.buscarLivrosPorTitulo(livro);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<LivroDTO> filtrarLivrosPorAutor(AutorDTO autor){
        try {
            return livroController.buscarLivrosPorAutor(autor);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void criarCritica(){
        if(painelLivros.getLivroDestacado()!= null){
            if(formulario == null){
                formulario = new FormularioNovaCritica();
                parteLateral.add(formulario);
            }
            formulario.removeAll();
            formulario.exibirFormularioNovaCritica();
            formulario.getSalvar().addActionListener(this);
            formulario.getCancelar().addActionListener(this);
            formulario.getTitulo().setText("Nova crítica do livro "+painelLivros.getLivroDestacado().titulo());
            formulario.setVisible(false);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof CaixaDetalhesLivro){

            CaixaDetalhesLivro caixa = (CaixaDetalhesLivro) e.getSource();
            painelLivros.selecionarLivro(caixa);
            exibirCriticas();

        }else if(e.getSource() == botaoNovaCritica){

            criarCritica();
            formulario.setVisible(true);

        }else if(e.getSource() == formulario.getCancelar()){

            formulario.setVisible(false);
            if(painelCriticas == null){
                criarPainelCriticas();
            }
            painelCriticas.setVisible(true);

        }else if(e.getSource() == formulario.getSalvar()){

            if(!formulario.getCaixaTexto().getText().isBlank()){

                CriticaDTO novaCritica = new CriticaDTO(null, 
                formulario.getCaixaTexto().getText(), 
                painelLivros.getLivroDestacado());
                //chamar método do controller que salva uma critica nova
                try {
                    criticaController.salvarCritica(novaCritica);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                formulario.setVisible(false);
                exibirCriticas();

            }

        }else if(e.getSource() == botaoPesquisar){

            String pesquisa = caixaPesquisa.getText();

            if(!pesquisa.isBlank()){
                List<LivroDTO> livrosFiltrados = new ArrayList<>();

                LivroDTO livroProcurado = new LivroDTO(null, null, null, pesquisa, null);
                livrosFiltrados = filtrarLivrosPorTitulo(livroProcurado);

                AutorDTO autorPesquisado = new AutorDTO(null, pesquisa);
                for (LivroDTO livroDTO : filtrarLivrosPorAutor(autorPesquisado)) {
                    if(!livrosFiltrados.contains(livroDTO)){
                        livrosFiltrados.add(livroDTO);
                    }
                }

                if(livrosFiltrados.size() > 0){
                    exibirLivrosFiltrados(livrosFiltrados);
                    exibirCriticas();
                }
                
                
            }
        }else if(e.getSource() == botaoLimparPesquisa){

            caixaPesquisa.setText("");
            exibirLivros();
            exibirCriticas();
        }
    }
}
