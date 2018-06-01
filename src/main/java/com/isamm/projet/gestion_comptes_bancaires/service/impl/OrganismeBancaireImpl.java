package com.isamm.projet.gestion_comptes_bancaires.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.isamm.projet.gestion_comptes_bancaires.dao.impl.BanqueDaoImpl;
import com.isamm.projet.gestion_comptes_bancaires.dao.impl.CompteBancaireDaoImpl;
import com.isamm.projet.gestion_comptes_bancaires.dao.impl.HistoriqueDaoImpl;
import com.isamm.projet.gestion_comptes_bancaires.dao.impl.TitulaireDeCompteDaoImpl;
import com.isamm.projet.gestion_comptes_bancaires.entities.Banque;
import com.isamm.projet.gestion_comptes_bancaires.entities.BanqueA;
import com.isamm.projet.gestion_comptes_bancaires.entities.CompteBancaire;
import com.isamm.projet.gestion_comptes_bancaires.entities.CompteBancaireEntreprise;
import com.isamm.projet.gestion_comptes_bancaires.entities.CompteBancaireParticulier;
import com.isamm.projet.gestion_comptes_bancaires.entities.Historique;
import com.isamm.projet.gestion_comptes_bancaires.entities.TitulaireDeCompte;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeCompteBancaire;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeCompteBancaireParticulier;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeOperation;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeTitulaireDeCompte;
import com.isamm.projet.gestion_comptes_bancaires.exception.CompteBancaireInexistant;
import com.isamm.projet.gestion_comptes_bancaires.exception.DebitMaximalDepasse;
import com.isamm.projet.gestion_comptes_bancaires.exception.DecouvertAutoriseDepasse;
import com.isamm.projet.gestion_comptes_bancaires.exception.OperationNonAutorisee;
import com.isamm.projet.gestion_comptes_bancaires.service.OrganismeBancaire;

/**
 * 
 * OrganismeBancaireImpl class implements OrganismeBancaire interface and contains all the business logic of the application
 * It depends on the Data Access Layer to manipulate the entities, thus it doesn't communicate directly with the data base
 *
 */

public class OrganismeBancaireImpl implements OrganismeBancaire {

	private BanqueDaoImpl banqueDaoImpl = null;
	private CompteBancaireDaoImpl compteBancaireDaoImpl = null;
	private TitulaireDeCompteDaoImpl titulaireDeCompteDaoImpl = null;
	private HistoriqueDaoImpl historiqueDaoImpl = null;
	
	public OrganismeBancaireImpl() {
		this.banqueDaoImpl = new BanqueDaoImpl();
		this.compteBancaireDaoImpl = new CompteBancaireDaoImpl();
		this.titulaireDeCompteDaoImpl = new TitulaireDeCompteDaoImpl();
		this.historiqueDaoImpl = new HistoriqueDaoImpl();
	}
	
	@Override
	public boolean debiterCompte(ObjectId accountId, float money) throws OperationNonAutorisee {
		
		boolean allow = false;
		
		CompteBancaire compteBancaire = compteBancaireDaoImpl.findById(accountId);
		
		if (compteBancaire.getSolde() >= money) {
			
			if (compteBancaire.getTypeCompteBancaire().equals(TypeCompteBancaire.COMPTE_BANCAIRE_ENTREPRISE)) {
				allow = true;
			} else if (compteBancaire.getTypeCompteBancaire().equals(TypeCompteBancaire.COMPTE_BANCAIRE_PARTICULIER)) {
				CompteBancaireParticulier compteBancaireParticulier = (CompteBancaireParticulier) compteBancaire;
				if (compteBancaireParticulier.getDebitMaximalAutorise() >= money) {
					allow = true;
				} else {
					allow = false;
					throw new DebitMaximalDepasse(money + " depasse le debit maximal autorise " + compteBancaireParticulier.getDebitMaximalAutorise());
				}
			}
			
		} else {
			
			if (compteBancaire.getTypeCompteBancaire().equals(TypeCompteBancaire.COMPTE_BANCAIRE_ENTREPRISE)) {
				if (Math.abs(compteBancaire.getSolde() - money) <= compteBancaire.getDecouvertAutorise()) {
					allow = true;
				} else {
					allow = false;
					throw new DecouvertAutoriseDepasse(money + " depasse le decouvert autorise " + compteBancaire.getDecouvertAutorise());
				}
			} else if (compteBancaire.getTypeCompteBancaire().equals(TypeCompteBancaire.COMPTE_BANCAIRE_PARTICULIER)) {
				if (Math.abs(compteBancaire.getSolde() - money) <= compteBancaire.getDecouvertAutorise()) {
					CompteBancaireParticulier compteBancaireParticulier = (CompteBancaireParticulier) compteBancaire;
					if (compteBancaireParticulier.getDebitMaximalAutorise() >= money) {
						allow = true;
					} else {
						allow = false;
						throw new DebitMaximalDepasse(money + " depasse le debit maximal autorise " + compteBancaireParticulier.getDebitMaximalAutorise());
					}
				} else {
					allow = false;
					throw new DecouvertAutoriseDepasse(money + " depasse le decouvert autorise " + compteBancaire.getDecouvertAutorise());
				}
			}
			
		}

		if (allow) {
			compteBancaire = compteBancaireDaoImpl.update(accountId, "{$set: {solde:"+(compteBancaire.getSolde() - money)+"}}");
		}
		
		if (compteBancaire != null) {
			compteBancaire = addHistoryToAccount(accountId, TypeOperation.DEBIT, compteBancaire.getSolde());
			return true;
		} else {
			return false;
		}
		
	}

	@Override
	public boolean crediterCompte(ObjectId accountId, float money) {
		
		CompteBancaire compteBancaire = compteBancaireDaoImpl.findById(accountId);
		
		compteBancaire = compteBancaireDaoImpl.update(accountId, "{$set: {solde:"+(compteBancaire.getSolde() + money)+"}}");
		
		if (compteBancaire != null) {
			compteBancaire = addHistoryToAccount(accountId, TypeOperation.CREDIT, compteBancaire.getSolde());
			return true;
		} else {
			return false;
		}
		
	}

	@Override
	public boolean virerArgent(ObjectId accountSourceId, ObjectId accountDestinationId, float money) throws OperationNonAutorisee {
		
		boolean confirmation;
		
		confirmation = debiterCompte(accountSourceId, money);
		if (confirmation) {
			confirmation = crediterCompte(accountDestinationId, money);
			if (!confirmation) {
				crediterCompte(accountSourceId, money);
			}
		}
		
		return confirmation;
		
	}

	@Override
	public CompteBancaireParticulier creerCompteBancaireParticulier(CompteBancaireParticulier compteBancaireParticulier) {
		return (CompteBancaireParticulier) compteBancaireDaoImpl.save(compteBancaireParticulier);
	}

	@Override
	public CompteBancaireEntreprise creerCompteBancaireEntreprise(CompteBancaireEntreprise compteBancaireEntreprise) {
		return (CompteBancaireEntreprise) compteBancaireDaoImpl.save(compteBancaireEntreprise);
	}

	@Override
	public boolean fermerCompte(ObjectId accountId) {
		
		CompteBancaire compteBancaire = compteBancaireDaoImpl.update(accountId, "{$set: {estOuvert: false}}");
		return compteBancaire.isEstOuvert();
		
	}
	
	/**
	 * if there is no banks in the data base initialize the application with one bank
	 * management of banks is out of the application's scope
	 * one bank is enough to perform accounts operations
	 * 
	 * @return
	 */
	public Banque initBanque() {
		
		if(banqueDaoImpl.count() == 0) {
			return banqueDaoImpl.save(new BanqueA(32, new ArrayList<ObjectId>(), new ArrayList<ObjectId>()));
		} else {
			return banqueDaoImpl.getFirstBank();
		}
		
	}
	
	/**
	 * add a TitulaireDeCompte Id or a CompteBancaire Id to a bank
	 * 
	 * @param bankId
	 * @param property
	 * @param values
	 * @return
	 */
	public Banque addToBank(ObjectId bankId, String property, List<ObjectId> values) {
		
		return banqueDaoImpl.addToBank(bankId, property, values);
		
	}
	
	/**
	 * check if a client is allowed to create an account with a given type
	 * 
	 * @param compteBancairesIds
	 * @param codeTitulaire
	 * @param typeCompteBancaire
	 * @param typeCompteBancaireParticulier
	 * @return
	 */
	public boolean allowAccountCreation(List<ObjectId> compteBancairesIds, int codeTitulaire, TypeCompteBancaire typeCompteBancaire, TypeCompteBancaireParticulier typeCompteBancaireParticulier) {
		
		boolean allow = false;
		
		if(typeCompteBancaire.equals(TypeCompteBancaire.COMPTE_BANCAIRE_ENTREPRISE)) {
			allow = (compteBancaireDaoImpl.findCompteBancaireEntrepriseByCodeInBank(compteBancairesIds, codeTitulaire) == null);
		} else if(typeCompteBancaire.equals(TypeCompteBancaire.COMPTE_BANCAIRE_PARTICULIER)) {
			allow = (compteBancaireDaoImpl.findCompteBancaireParticulierByTypeAndCodeInBank(compteBancairesIds, typeCompteBancaireParticulier, codeTitulaire) == null);
		}
		
		return allow;
		
	}
	
	/**
	 * check if a client with the given type and code exist in this bank system
	 * 
	 * @param titulaireDeCompteIds
	 * @param typeTitulaireDeCompte
	 * @param codeTitulaire
	 * @return
	 */
	public TitulaireDeCompte findTitulaireDeCompteByTypeAndCodeInBank(List<ObjectId> titulaireDeCompteIds, TypeTitulaireDeCompte typeTitulaireDeCompte, int codeTitulaire) {
		
		return titulaireDeCompteDaoImpl.findByTypeAndCodeInBank(titulaireDeCompteIds, typeTitulaireDeCompte, codeTitulaire);

	}
	
	/**
	 * generate a unique Iban using unix timestamp and a random key
	 * 
	 * @param codebanque
	 * @return
	 */
	public String generateIban(int codebanque) {
		Long max = new Long(9999999999999L);
		Long min = new Long(1000000000000L);
		Long accountId = Math.abs(max-Instant.now().getEpochSecond());
		if (accountId < min) {
			accountId = accountId + min;
		}
		int key = (int)(Math.random() * 99 + 10);
		return "TN59"+codebanque+"001"+accountId+key;
	}
	
	/**
	 * generate a unique cardNumber using unix timestamp
	 * 
	 * @return
	 */
	public int generateCardNumber() {
		int max = 999999999;
		int min = 100000000;
		int cardNumber = (int) Math.abs(max-Instant.now().getEpochSecond());
		if (cardNumber < min) {
			cardNumber = cardNumber + min + (int)(Math.random() * 999 + 100);
		}
		return cardNumber;
	}
	
	/**
	 * create a new TitulaireDeCompte
	 * 
	 * @param titulaireDeCompte
	 * @return
	 */
	public TitulaireDeCompte createTitulaireDeCompte(TitulaireDeCompte titulaireDeCompte) {
		
		return titulaireDeCompteDaoImpl.save(titulaireDeCompte);
		
	}
	
	/**
	 * search for a CompteBancaire using its unique iban in a bank system
	 * 
	 * @param compteBancaireIds
	 * @param iban
	 * @return
	 * @throws CompteBancaireInexistant
	 */
	public CompteBancaire findCompteBancaireByIbanInBank(List<ObjectId> compteBancaireIds, String iban) throws CompteBancaireInexistant {
		
		CompteBancaire compteBancaire = compteBancaireDaoImpl.findCompteBancaireByIbanInBank(compteBancaireIds, iban);
		
		if (compteBancaire == null) {
			throw new CompteBancaireInexistant("Compte bancaire avec iban "+iban+" inexistant");
		}
		
		return compteBancaire;
		
	}
	
	/**
	 * 
	 * @param accountId
	 * @return
	 */
	public CompteBancaire findCompteBancaireById(ObjectId accountId) {
		
		return compteBancaireDaoImpl.findById(accountId);
		
	}
	
	/**
	 * create a record and assign it to an account given by its id
	 * 
	 * @param accountId
	 * @param typeOperation
	 * @param solde
	 * @return
	 */
	public CompteBancaire addHistoryToAccount(ObjectId accountId, TypeOperation typeOperation, float solde) {
		
		Historique historique = new Historique(typeOperation, solde);
		historique = historiqueDaoImpl.save(historique);
		CompteBancaire compteBancaire = compteBancaireDaoImpl.findById(accountId);
		List<ObjectId> historyIds = (List<ObjectId>) compteBancaire.getHistoriques();
		historyIds.add(historique.get_id());
		return compteBancaireDaoImpl.addHistoryToAccount(accountId, historyIds);
		
	}
	
	/**
	 * save a record to database
	 * 
	 * @param historique
	 * @return
	 */
	public Historique addToHistory(Historique historique) {
		
		return historiqueDaoImpl.save(historique);
	
	}
	
	/**
	 * search for accounts records
	 * 
	 * @param accountHistoryIds
	 * @return
	 */
	public List<Historique> getAccountHistory(List<ObjectId> accountHistoryIds) {
		
		return historiqueDaoImpl.findByIdRange(accountHistoryIds);

	}
	
	/**
	 * search for las deposit
	 * 
	 * @param accountHistoryIds
	 * @return
	 */
	public Historique getLastDeposit(List<ObjectId> accountHistoryIds) {
		
		return historiqueDaoImpl.getLastDeposit(accountHistoryIds);
		
	}
	
}
