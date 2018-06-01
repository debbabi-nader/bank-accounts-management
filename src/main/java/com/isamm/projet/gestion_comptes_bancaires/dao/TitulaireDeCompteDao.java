package com.isamm.projet.gestion_comptes_bancaires.dao;

import java.util.List;

import org.bson.types.ObjectId;

import com.isamm.projet.gestion_comptes_bancaires.entities.TitulaireDeCompte;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeTitulaireDeCompte;

/**
 * 
 * Specific DAO interface
 * Extends Generic DAO interface with TitulaireDeCompte concrete type
 *
 */

public interface TitulaireDeCompteDao extends GenericDao<TitulaireDeCompte> {
	
	public TitulaireDeCompte findByTypeAndCodeInBank(List<ObjectId> titulaireDeCompteIds, TypeTitulaireDeCompte typeTitulaireDeCompte, int codeTitulaire);
	
}
