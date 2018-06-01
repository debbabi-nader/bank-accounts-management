package com.isamm.projet.gestion_comptes_bancaires.dao;

import java.util.List;

import org.bson.types.ObjectId;

import com.isamm.projet.gestion_comptes_bancaires.entities.CompteBancaire;
import com.isamm.projet.gestion_comptes_bancaires.entities.CompteBancaireEntreprise;
import com.isamm.projet.gestion_comptes_bancaires.entities.CompteBancaireParticulier;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeCompteBancaireParticulier;

/**
 * 
 * Specific DAO interface
 * Extends Generic DAO interface with CompteBancaire concrete type
 *
 */

public interface CompteBancaireDao extends GenericDao<CompteBancaire> {
	
	public CompteBancaireEntreprise findCompteBancaireEntrepriseByCodeInBank(List<ObjectId> compteBancaireIds, int codeTitulaire);
	
	public CompteBancaireParticulier findCompteBancaireParticulierByTypeAndCodeInBank(List<ObjectId> compteBancaireIds, TypeCompteBancaireParticulier typeCompteBancaireParticulier,int codeTitulaire);
	
	public CompteBancaire findCompteBancaireByIbanInBank(List<ObjectId> compteBancaireIds, String iban);
	
	public CompteBancaire addHistoryToAccount(ObjectId accountId, List<ObjectId> historyIds);
	
}
