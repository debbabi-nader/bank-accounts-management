package com.isamm.projet.gestion_comptes_bancaires.service;

import org.bson.types.ObjectId;

import com.isamm.projet.gestion_comptes_bancaires.entities.CompteBancaireEntreprise;
import com.isamm.projet.gestion_comptes_bancaires.entities.CompteBancaireParticulier;
import com.isamm.projet.gestion_comptes_bancaires.exception.OperationNonAutorisee;


public interface OrganismeBancaire {
	
	public boolean debiterCompte(ObjectId accountId, float money) throws OperationNonAutorisee;
	
	public boolean crediterCompte(ObjectId accountId, float money);
	
	public boolean virerArgent(ObjectId accountSourceId, ObjectId accountDestinationId, float money) throws OperationNonAutorisee;
	
	public CompteBancaireParticulier creerCompteBancaireParticulier(CompteBancaireParticulier compteBancaireParticulier);
	
	public CompteBancaireEntreprise creerCompteBancaireEntreprise(CompteBancaireEntreprise compteBancaireEntreprise);
	
	public boolean fermerCompte(ObjectId accountId);
	
}
