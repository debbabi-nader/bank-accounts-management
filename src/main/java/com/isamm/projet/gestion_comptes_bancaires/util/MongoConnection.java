package com.isamm.projet.gestion_comptes_bancaires.util;

import org.jongo.Jongo;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * 
 * A singleton class to manage Mongodb connection across the application
 *
 */

public class MongoConnection {
	
	private static MongoConnection instance;
	
	private MongoClient mongoClient = null;
	private DB database = null;
	private Jongo jongo = null;
	
	private MongoConnection() {
		init();
	}
	
	public MongoClient getMongoClient() {
		
		if (mongoClient == null) {
			
			MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
			
			try {
				mongoClient = new MongoClient(connectionString);
			} catch(Exception e) {
				System.out.println("Error occured while trying to connect to MongoDB");
			}
			
		}
		
		return mongoClient;
		
	}
	
	@SuppressWarnings("deprecation")
	public DB getDatabase() {
		
		if (database == null) {
			
			try {
				database = mongoClient.getDB("banquedb");
			} catch (Exception e) {
				System.out.println("Error occured while trying to get banque database");
			}
			
		}
		
		return database;
		
	}
	
	public Jongo getJongo() {
		
		if (jongo == null) {
			
			try {
				jongo = new Jongo(database);
			} catch (Exception e) {
				System.out.println("Error occured while trying to create jongo");
			}
			
		}
		
		return jongo;
		
	}
	
	public void init() {
		
		getMongoClient();
		getDatabase();
		getJongo();
		
	}
	
	public void close() {
		
		if (mongoClient != null) {
			
			try {
				mongoClient.close();
				mongoClient = null;
				database = null;
				jongo = null;
			} catch(Exception e) {
				System.out.println("Error occured while trying to disconnect from MongoDB");
			}

		}
	}
	
	public static MongoConnection getMongoConnection() {
		
		if (instance == null) {
			instance = new MongoConnection();
		}
		
		return instance;
		
	}
	
}
