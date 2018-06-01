package com.isamm.projet.gestion_comptes_bancaires.dao;

import java.util.List;

import org.bson.types.ObjectId;

import com.isamm.projet.gestion_comptes_bancaires.entities.Historique;

/**
 * 
 * Specific DAO interface
 * Extends Generic DAO interface with Historique concrete type
 *
 */

public interface HistoriqueDao extends GenericDao<Historique> {

	public Historique getLastDeposit(List<ObjectId> accountHistoryIds);
	
}
