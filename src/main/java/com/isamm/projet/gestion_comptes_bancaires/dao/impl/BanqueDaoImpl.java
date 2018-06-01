package com.isamm.projet.gestion_comptes_bancaires.dao.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

import com.isamm.projet.gestion_comptes_bancaires.dao.BanqueDao;
import com.isamm.projet.gestion_comptes_bancaires.entities.Banque;
import com.isamm.projet.gestion_comptes_bancaires.util.MongoCollectionFactory;

/**
 * 
 * Specific DAO interface implementation
 * BanqueDaoImpl extends GenericDaoImpl with the concrete type Banque 
 * for the implementation of DAO common methods and implements BanqueDao interface
 * to implement other specific methods.
 *
 */

public class BanqueDaoImpl extends GenericDaoImpl<Banque> implements BanqueDao {
	
	private static MongoCollection banqueCollection = MongoCollectionFactory.getMongoCollection("banquecollection");
	
	public BanqueDaoImpl() {
		super(banqueCollection, Banque.class);
	}

	@Override
	public Banque getFirstBank() {
		return banqueCollection.findOne().as(Banque.class);
	}

	@Override
	public Banque addToBank(ObjectId bankId, String property, List<ObjectId> values) {
		
		String updateString = "{$set: {"+property+":#}}";
		banqueCollection.update(bankId).with(updateString, values);
		return banqueCollection.findOne(bankId).as(Banque.class);

	}
	
}
