package com.ProjetoDocumento.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.ProjetoDocumento.dto.CriticaDTO;

public class PainelCriticas extends JPanel {

    private JPanel listaCriticas;
    private Color corPadrao;

    public Color getCorPadrao() {
        return corPadrao;
    }

    public void setCorPadrao(Color corPadrao) {
        this.corPadrao = corPadrao;
    }

    public PainelCriticas() {
        setBounds(0, 300, 800, 300);
        setOpaque(false);
        setLayout(new BorderLayout());
        listaCriticas = new JPanel();
    }

    public void exibirCriticas(List<CriticaDTO> criticas) {
        listaCriticas.removeAll(); // Remove críticas anteriores

        int quantidadeCriticas = Math.max(5, criticas.size()); // Define tamanho mínimo
        listaCriticas.setLayout(new GridLayout(0, 1, 4, 8)); // Layout flexível (múltiplas linhas)

        for (CriticaDTO critica : criticas) {
            CaixaDetalhesCritica detalhesCritica = new CaixaDetalhesCritica(critica);
            if (corPadrao != null) {
                detalhesCritica.setBackground(corPadrao);
            }
            listaCriticas.add(detalhesCritica);
        }

        setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));

        JScrollPane painelBarraDeRolagem = new JScrollPane(listaCriticas);
        painelBarraDeRolagem.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        painelBarraDeRolagem.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        removeAll(); // Remove qualquer conteúdo anterior
        add(painelBarraDeRolagem, BorderLayout.CENTER);

        revalidate(); // Atualiza o layout
        repaint(); // Redesenha a interface
    }

    public void mudarCriticas(List<CriticaDTO> criticas) {
        exibirCriticas(criticas);
    }
}