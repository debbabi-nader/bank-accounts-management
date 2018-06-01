package com.isamm.projet.gestion_comptes_bancaires.dao.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

import com.isamm.projet.gestion_comptes_bancaires.dao.CompteBancaireDao;
import com.isamm.projet.gestion_comptes_bancaires.entities.CompteBancaire;
import com.isamm.projet.gestion_comptes_bancaires.entities.CompteBancaireEntreprise;
import com.isamm.projet.gestion_comptes_bancaires.entities.CompteBancaireParticulier;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeCompteBancaire;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeCompteBancaireParticulier;
import com.isamm.projet.gestion_comptes_bancaires.util.MongoCollectionFactory;

/**
 * 
 * Specific DAO interface implementation
 * CompteBancaireDaoImpl extends GenericDaoImpl with the concrete type CompteBancaire 
 * for the implementation of DAO common methods and implements CompteBancaireDao interface
 * to implement other specific methods.
 *
 */

public class CompteBancaireDaoImpl extends GenericDaoImpl<CompteBancaire> implements CompteBancaireDao {
	
	private static MongoCollection compteBancaireCollection = MongoCollectionFactory.getMongoCollection("comptebancairecollection");

	public CompteBancaireDaoImpl() {
		super(compteBancaireCollection, CompteBancaire.class);
	}

	@Override
	public CompteBancaireEntreprise findCompteBancaireEntrepriseByCodeInBank(List<ObjectId> compteBancaireIds,
			int codeTitulaire) {
		return compteBancaireCollection.findOne("{_id: {$in:#}, typeCompteBancaire: #, codeTitulaire: #}", compteBancaireIds, TypeCompteBancaire.COMPTE_BANCAIRE_ENTREPRISE, codeTitulaire).as(CompteBancaireEntreprise.class);
	}

	@Override
	public CompteBancaireParticulier findCompteBancaireParticulierByTypeAndCodeInBank(List<ObjectId> compteBancaireIds,
			TypeCompteBancaireParticulier typeCompteBancaireParticulier, int codeTitulaire) {
		return compteBancaireCollection.findOne("{_id: {$in:#}, typeCompteBancaire: #, typeCompteBancaireParticulier: #, codeTitulaire: #}", compteBancaireIds, TypeCompteBancaire.COMPTE_BANCAIRE_PARTICULIER, typeCompteBancaireParticulier, codeTitulaire).as(CompteBancaireParticulier.class);
	}

	@Override
	public CompteBancaire findCompteBancaireByIbanInBank(List<ObjectId> compteBancaireIds, String iban) {
		return compteBancaireCollection.findOne("{_id: {$in:#}, iban: #}", compteBancaireIds, iban).as(CompteBancaire.class);
	}

	@Override
	public CompteBancaire addHistoryToAccount(ObjectId accountId, List<ObjectId> historyIds) {
		compteBancaireCollection.update(accountId).with("{$set: {historiques:#}}", historyIds);
		return compteBancaireCollection.findOne(accountId).as(CompteBancaire.class);
	}

}
