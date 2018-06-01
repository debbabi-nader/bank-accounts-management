package com.isamm.projet.gestion_comptes_bancaires.dao;

import java.util.List;

import org.bson.types.ObjectId;

import com.isamm.projet.gestion_comptes_bancaires.entities.Banque;

/**
 * 
 * Specific DAO interface
 * Extends Generic DAO interface with Banque concrete type
 *
 */

public interface BanqueDao extends GenericDao<Banque> {
	
	public Banque getFirstBank();
	
	public Banque addToBank(ObjectId bankId, String property, List<ObjectId> values);
	
}
