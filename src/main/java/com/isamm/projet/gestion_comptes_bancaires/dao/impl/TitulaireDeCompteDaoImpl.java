package com.isamm.projet.gestion_comptes_bancaires.dao.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

import com.isamm.projet.gestion_comptes_bancaires.dao.TitulaireDeCompteDao;
import com.isamm.projet.gestion_comptes_bancaires.entities.TitulaireDeCompte;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeTitulaireDeCompte;
import com.isamm.projet.gestion_comptes_bancaires.util.MongoCollectionFactory;

/**
 * 
 * Specific DAO interface implementation
 * TitulaireDeCompteDaoImpl extends GenericDaoImpl with the concrete type TitulaireDeCompte 
 * for the implementation of DAO common methods and implements TitulaireDeCompteDao interface
 * to implement other specific methods.
 *
 */

public class TitulaireDeCompteDaoImpl extends GenericDaoImpl<TitulaireDeCompte> implements TitulaireDeCompteDao {

	private static MongoCollection titulaireDeCompteCollection = MongoCollectionFactory.getMongoCollection("titulairedecomptecollection");
	
	public TitulaireDeCompteDaoImpl() {
		super(titulaireDeCompteCollection, TitulaireDeCompte.class);
	}

	@Override
	public TitulaireDeCompte findByTypeAndCodeInBank(List<ObjectId> titulaireDeCompteIds,
			TypeTitulaireDeCompte typeTitulaireDeCompte, int codeTitulaire) {
		return titulaireDeCompteCollection.findOne("{_id: {$in:#}, typeTitulaireDeCompte: #, codeTitulaire: #}", titulaireDeCompteIds, typeTitulaireDeCompte, codeTitulaire).as(TitulaireDeCompte.class);
	}

}
