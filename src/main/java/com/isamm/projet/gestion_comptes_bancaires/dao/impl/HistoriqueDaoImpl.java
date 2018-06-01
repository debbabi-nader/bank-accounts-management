package com.isamm.projet.gestion_comptes_bancaires.dao.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

import com.isamm.projet.gestion_comptes_bancaires.dao.HistoriqueDao;
import com.isamm.projet.gestion_comptes_bancaires.entities.Historique;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeOperation;
import com.isamm.projet.gestion_comptes_bancaires.util.MongoCollectionFactory;

/**
 * 
 * Specific DAO interface implementation
 * HistoriqueDaoImpl extends GenericDaoImpl with the concrete type Historique 
 * for the implementation of DAO common methods and implements HistoriqueDao interface
 * to implement other specific methods.
 *
 */

public class HistoriqueDaoImpl extends GenericDaoImpl<Historique> implements HistoriqueDao {

	private static MongoCollection historiqueCollection = MongoCollectionFactory.getMongoCollection("historiquecollection");
	
	public HistoriqueDaoImpl() {
		super(historiqueCollection, Historique.class);
	}

	@Override
	public Historique getLastDeposit(List<ObjectId> accountHistoryIds) {
		return historiqueCollection.findOne("{_id: {$in:#}, typeOperation: #}", accountHistoryIds, TypeOperation.CREDIT).orderBy("{dateOperation: 1}").as(Historique.class);
	}

}
