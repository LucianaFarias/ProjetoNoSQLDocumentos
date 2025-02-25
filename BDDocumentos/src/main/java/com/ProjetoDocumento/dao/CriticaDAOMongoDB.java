package com.ProjetoDocumento.dao;

import com.ProjetoDocumento.dto.CriticaDTO;
import com.ProjetoDocumento.dto.LivroDTO;
import com.ProjetoDocumento.ConexaoBD.ConexaoMongoDB;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

import java.util.ArrayList;
import java.util.List;

public class CriticaDAOMongoDB implements CriticaDAO {

    private final MongoCollection<Document> criticaCollection;
    private final MongoCollection<Document> sequenciaCollection;

    public CriticaDAOMongoDB() {
        this.criticaCollection = ConexaoMongoDB.getDatabase().getCollection("criticas");
        this.sequenciaCollection = ConexaoMongoDB.getDatabase().getCollection("sequencias");
    }

    // Método para gerar um novo ID sequencial
    private String gerarProximoId(String nomeSequencia) {
        Document filtro = new Document("_id", nomeSequencia);
        Document atualizacao = new Document("$inc", new Document("contador", 1));
        UpdateOptions options = new UpdateOptions().upsert(true);
        sequenciaCollection.updateOne(filtro, atualizacao, options);
        Document sequenciaDoc = sequenciaCollection.find(filtro).first();
        int novoIdSequencial = sequenciaDoc.getInteger("contador");
        return nomeSequencia.substring(0, 3).toUpperCase() + String.format("%04d", novoIdSequencial);
    }

    // Método auxiliar para criar CriticaDTO a partir de Document
    private CriticaDTO criarCriticaDTO(Document doc) {
        LivroDTO livroDTO = new LivroDTO(doc.getString("livroId"), null, null, "", "");
        return new CriticaDTO(doc.getString("_id"), doc.getString("conteudo"), livroDTO);
    }

    public CriticaDTO salvar(CriticaDTO criticaDTO) throws Exception {
        String novoIdCritica = gerarProximoId("criticas_id");

        Document doc = new Document("_id", novoIdCritica)
                .append("livroId", criticaDTO.livro().id())
                .append("conteudo", criticaDTO.conteudo());

        criticaCollection.insertOne(doc);

        return new CriticaDTO(novoIdCritica, criticaDTO.conteudo(), criticaDTO.livro());
    }


    @Override
    public List<CriticaDTO> buscarPorLivro(LivroDTO livroDTO) throws Exception {
        List<CriticaDTO> criticas = new ArrayList<>();

        List<Document> documentos = criticaCollection.find(Filters.eq("livroId", livroDTO.id())).into(new ArrayList<>());  // Alteração aqui

        if (documentos.isEmpty()) {
            System.out.println("Nenhuma crítica encontrada para o livro com ID: " + livroDTO.id());
        } else {
            for (Document doc : documentos) {
                criticas.add(criarCriticaDTO(doc));
            }
        }
        return criticas;
    }
    @Override
    public void excluir(CriticaDTO criticaDTO) throws Exception {
        CriticaDTO criticaEncontrada = buscarPorId(criticaDTO);

        if (criticaEncontrada != null) {
            criticaCollection.deleteOne(Filters.eq("_id", criticaDTO.id()));
            System.out.println("Crítica excluída com sucesso!");
        } else {
            System.out.println("Crítica não encontrada para exclusão.");
        }
    }

    @Override
    public CriticaDTO buscarPorId(CriticaDTO criticaDTO) {
        Document doc = criticaCollection.find(Filters.eq("_id", criticaDTO.id())).first();

        if (doc != null) {
            return criarCriticaDTO(doc);  // Usar o método auxiliar para criar CriticaDTO
        }
        return null;
    }
}