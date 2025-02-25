package com.ProjetoDocumento.ConexaoBD;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ConexaoMySQL {

    private static ConexaoMySQL INSTANCIA;
    private EntityManagerFactory factory;

    private ConexaoMySQL() {
    	this.factory = Persistence.createEntityManagerFactory("livrosPU");
    }
    
    public static ConexaoMySQL getInstancia() {
    	if(INSTANCIA == null) {
    		INSTANCIA = new ConexaoMySQL();
    	}else if(!INSTANCIA.factory.isOpen()) {
            INSTANCIA = new ConexaoMySQL();
    	}
        return INSTANCIA;
    }
    
	public void fechar() {
		if (factory.isOpen()){
			factory.close();
		}	
	}

	public EntityManagerFactory getFactory() {
		return factory;
	}
}