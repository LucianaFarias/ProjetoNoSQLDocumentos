package com.ProjetoDocumento.dao;

import com.ProjetoDocumento.ConexaoBD.ConexaoMongoDB;
import com.ProjetoDocumento.dto.AutorDTO;
import com.ProjetoDocumento.dto.LivroDTO;
import com.ProjetoDocumento.exception.LivroNaoExisteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import static com.ProjetoDocumento.API.APIMongoDB.buscarLivros;

import java.util.ArrayList;
import java.util.List;

public class LivroDAOMongoDB implements LivroDAO {

    private final MongoCollection<Document> livroCollection;
    private final MongoCollection<Document> sequenciaCollection;

    public LivroDAOMongoDB() {
        this.livroCollection = ConexaoMongoDB.getDatabase().getCollection("livros");
        this.sequenciaCollection = ConexaoMongoDB.getDatabase().getCollection("sequencias");  // Coleção de sequência
    }

    private String gerarProximoId(String nomeSequencia) {
        Document filtro = new Document("_id", nomeSequencia);

        Document atualizacao = new Document("$inc", new Document("contador", 1));
        UpdateOptions options = new UpdateOptions().upsert(true);
        sequenciaCollection.updateOne(filtro, atualizacao, options);
        Document sequenciaDoc = sequenciaCollection.find(filtro).first();
        int novoIdSequencial = sequenciaDoc.getInteger("contador");
        return nomeSequencia.substring(0, 3).toUpperCase() + String.format("%04d", novoIdSequencial);
    }

    public List<LivroDTO> buscarPorTitulo(LivroDTO livroDTO) throws Exception {
        String titulo = livroDTO.titulo();
        List<LivroDTO> livros = new ArrayList<>();
        try {
            List<Document> documentos = livroCollection.find(Filters.regex("titulo", titulo, "i")).into(new ArrayList<>());
            if (documentos.isEmpty()) {
                List<Document> livrosDaAPI = buscarLivros(titulo);
                if (livrosDaAPI.isEmpty()) {
                    throw new LivroNaoExisteException();
                } else {
                    livrosDaAPI.forEach(doc -> {
                        LivroDTO livro = criarLivroDTO(doc);
                        livros.add(livro);
                        salvar(livro); // Salvar o livro no banco
                    });
                }
            } else {
                documentos.forEach(doc -> {
                    LivroDTO livro = criarLivroDTO(doc);
                    livros.add(livro);
                });
            }
        } catch (Exception e) {
            throw new Exception("Erro ao buscar livros por título: " + e.getMessage(), e);
        }
        return livros;
    }

    public List<LivroDTO> buscarPorAutor(AutorDTO nomeAutor) throws Exception {
        List<LivroDTO> livros = new ArrayList<>();
        System.out.println("Iniciando busca por autor: " + nomeAutor.nome());

        try {
            List<Document> documentos = livroCollection.find(Filters.regex("autor", nomeAutor.nome(), "i")).into(new ArrayList<>());

            if (documentos.isEmpty()) {
                System.out.println("Nenhum livro encontrado no banco para o autor: " + nomeAutor.nome());
                List<Document> livrosDaAPI = buscarLivros(nomeAutor.nome());
                if (livrosDaAPI.isEmpty()) {
                    System.out.println("Nenhum livro encontrado na API para o autor: " + nomeAutor.nome());
                } else {
                    livrosDaAPI.forEach(doc -> {
                        LivroDTO livro = criarLivroDTO(doc);
                        livros.add(livro);
                        salvar(livro);
                    });
                }
            } else {
                documentos.forEach(doc -> {
                    LivroDTO livro = criarLivroDTO(doc);
                    livros.add(livro);
                });
            }
        } catch (Exception e) {
            throw new Exception("Erro ao buscar livros por autor: " + e.getMessage(), e);
        }
        return livros;
    }

    public void salvar(LivroDTO livroDTO) {
        String novoIdLivro = gerarProximoId("livros_id");

        try {
            Document livroExistente = livroCollection.find(Filters.eq("titulo", livroDTO.titulo())).first();
            if (livroExistente == null) {
                Document doc = new Document()
                        .append("_id", novoIdLivro) // ID gerado ao campo _id
                        .append("titulo", livroDTO.titulo())
                        .append("autor", livroDTO.autor().nome())
                        .append("anoPublicacao", livroDTO.anoPublicacao())
                        .append("categoria", livroDTO.categoria());
                livroCollection.insertOne(doc);
                System.out.println("Livro inserido no banco: " + livroDTO.titulo() + " " + livroDTO.autor().nome());
            } else {
                System.out.println("Livro já existe no banco.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao salvar livro: " + e.getMessage());
        }
    }
    private LivroDTO criarLivroDTO(Document doc) {
        AutorDTO autorDTO = new AutorDTO(null, doc.getString("autor"));
        return new LivroDTO(
                doc.getString("_id"),
                autorDTO,
                doc.getString("anoPublicacao"),
                doc.getString("titulo"),
                doc.getString("categoria")
        );
    }
    public List<LivroDTO> listarTodosLivros() throws Exception {
        List<LivroDTO> livros = new ArrayList<>();
        try {
            List<Document> documentos = livroCollection.find().into(new ArrayList<>());

            if (documentos.isEmpty()) {
                System.out.println("Nenhum livro encontrado no banco.");
            } else {
                documentos.forEach(doc -> {
                    LivroDTO livro = criarLivroDTO(doc);
                    livros.add(livro);
                });
            }
            livros.forEach(livro -> {
                System.out.println("ID: " + livro.id() + ", Título: " + livro.titulo() + ", Autor: " + livro.autor().nome() +
                        ", Ano de Publicação: " + livro.anoPublicacao() + ", Categoria: " + livro.categoria());
            });

        } catch (Exception e) {
            System.out.println("Erro ao listar todos os livros: " + e.getMessage());
            throw new Exception("Erro ao listar todos os livros: " + e.getMessage(), e);
        }

        return livros;
    }
}