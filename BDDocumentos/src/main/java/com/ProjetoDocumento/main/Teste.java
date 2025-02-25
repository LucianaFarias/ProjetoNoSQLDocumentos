package com.ProjetoDocumento.main;

import com.ProjetoDocumento.controller.CriticaController;
import com.ProjetoDocumento.controller.LivroController;
import com.ProjetoDocumento.dto.AutorDTO;
import com.ProjetoDocumento.dto.CriticaDTO;
import com.ProjetoDocumento.dto.LivroDTO;
import java.util.List;
import java.util.Scanner;

public class Teste {

    public static void main(String[] args) {
        LivroController livroController = new LivroController();
        CriticaController criticaController = new CriticaController();
        Scanner scanner = new Scanner(System.in);

        int opcao;
        do {
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Buscar livro por título");
            System.out.println("2 - Buscar livro por autor");
            System.out.println("3 - Fazer crítica");
            System.out.println("4 - Excluir crítica");
            System.out.println("5 - Ver críticas de um livro");
            System.out.println("0 - Sair");

            opcao = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (opcao) {
                    case 1:
                        // Buscar por título
                        System.out.print("Digite o título do livro: ");
                        String titulo = scanner.nextLine();
                        LivroDTO livroPorTitulo = new LivroDTO(null, new AutorDTO(null, "Autor Desconhecido"), "Ano Desconhecido", titulo, "Categoria Desconhecida");
                        List<LivroDTO> livrosEncontradosPorTitulo = livroController.buscarLivrosPorTitulo(livroPorTitulo);
                        if (livrosEncontradosPorTitulo.isEmpty()) {
                            System.out.println("Nenhum livro encontrado com esse título.");
                        } else {
                            livrosEncontradosPorTitulo.forEach(livro -> System.out.println("Livro encontrado por título: " + livro.id()+","+ livro.titulo()+","+","+ livro.autor()));
                        }
                        break;

                    case 2:
                        // Buscar por autor
                        System.out.print("Digite o nome do autor: ");
                        String nomeAutor = scanner.nextLine();
                        AutorDTO autorDTO = new AutorDTO(null, nomeAutor);
                        List<LivroDTO> livrosEncontradosPorAutor = livroController.buscarLivrosPorAutor(autorDTO);
                        if (livrosEncontradosPorAutor.isEmpty()) {
                            System.out.println("Nenhum livro encontrado para o autor.");
                        } else {
                            livrosEncontradosPorAutor.forEach(livro -> System.out.println("Livro encontrado por autor: " + livro.id()+","+ livro.titulo()+","+","+ livro.autor()));
                        }
                        break;

                    case 3:
                        // Fazer crítica
                        System.out.print("Digite o ID do livro para fazer a crítica: ");
                        String livroId = scanner.nextLine();
                        System.out.print("Digite o conteúdo da crítica: ");
                        String conteudo = scanner.nextLine();

                        try {
                            // Criando a crítica DTO
                            LivroDTO livroCritica = new LivroDTO(livroId, null, null, null, null);
                            CriticaDTO criticaDTO = new CriticaDTO(null, conteudo, livroCritica);

                            // Salvar crítica e garantir que o ID seja atribuído
                            CriticaDTO criticaSalva = criticaController.salvarCritica(criticaDTO); // Passa CriticaDTO e não uma String
                            System.out.println("Crítica salva com sucesso!");
                            System.out.println("ID da crítica: " + criticaSalva.id());
                            System.out.println("Conteúdo da crítica: " + criticaSalva.conteudo());
                        } catch (Exception e) {
                            System.out.println("Erro ao salvar crítica: " + e.getMessage());
                        }
                        break;

                    case 4:
                        // Excluir crítica
                        System.out.print("Digite o ID da crítica para excluir: ");
                        String criticaId = scanner.nextLine();
                        try {
                            CriticaDTO criticaDTOExcluir = new CriticaDTO(criticaId, null, null);
                            criticaController.excluirCritica(criticaDTOExcluir);
                            System.out.println("Crítica excluída com sucesso.");
                        } catch (Exception e) {
                            System.out.println("Erro ao excluir crítica: " + e.getMessage());
                        }
                        break;

                    case 5:
                        // Ver críticas de um livro
                        System.out.print("Digite o ID do livro para ver as críticas: ");
                        String idLivro = scanner.nextLine();
                        LivroDTO livroParaBuscarCriticas = new LivroDTO(idLivro, null, null, null, null);

                        // Buscar e exibir críticas
                        List<CriticaDTO> criticas = criticaController.buscarCriticasPorLivro(livroParaBuscarCriticas);
                        if (criticas.isEmpty()) {
                            System.out.println("Nenhuma crítica encontrada para o livro com ID: " + idLivro);
                        } else {
                            criticas.forEach(critica -> {
                                System.out.println("ID da Crítica: " + critica.id());
                                System.out.println("Conteúdo da Crítica: " + critica.conteudo());
                                System.out.println("-----------------------------------");
                            });
                        }
                        break;

                    case 0:
                        // Sair
                        System.out.println("Saindo...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (opcao != 0);
    }
}