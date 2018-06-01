package com.isamm.projet.gestion_comptes_bancaires.util;

import org.jongo.MongoCollection;

/**
 * 
 * A factory class to create a collection from the MongoConnection
 * with a static method using the MongoCollection name 
 *
 */

public class MongoCollectionFactory {
	
	private MongoCollectionFactory() {
		
	}
	
	public static MongoCollection getMongoCollection(String collectionName) {
		
		MongoConnection mongoConnection = MongoConnection.getMongoConnection();
		return mongoConnection.getJongo().getCollection(collectionName);
	
	}

}
