package com.ProjetoDocumento.main;

import com.ProjetoDocumento.view.TelaCriticas;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Inicia a interface gráfica na thread de eventos
        SwingUtilities.invokeLater(() -> {
            TelaCriticas telaCriticas = new TelaCriticas();
            telaCriticas.setVisible(true);
        });
    }
}
