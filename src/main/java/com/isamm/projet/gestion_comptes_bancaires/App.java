package com.isamm.projet.gestion_comptes_bancaires;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bson.types.ObjectId;

import com.isamm.projet.gestion_comptes_bancaires.entities.Banque;
import com.isamm.projet.gestion_comptes_bancaires.entities.CompteBancaire;
import com.isamm.projet.gestion_comptes_bancaires.entities.CompteBancaireEntreprise;
import com.isamm.projet.gestion_comptes_bancaires.entities.CompteBancaireParticulier;
import com.isamm.projet.gestion_comptes_bancaires.entities.Historique;
import com.isamm.projet.gestion_comptes_bancaires.entities.PersonneMorale;
import com.isamm.projet.gestion_comptes_bancaires.entities.PersonnePhysique;
import com.isamm.projet.gestion_comptes_bancaires.entities.TitulaireDeCompte;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeCompteBancaire;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeCompteBancaireParticulier;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeOperation;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeTitulaireDeCompte;
import com.isamm.projet.gestion_comptes_bancaires.exception.CompteBancaireInexistant;
import com.isamm.projet.gestion_comptes_bancaires.exception.OperationNonAutorisee;
import com.isamm.projet.gestion_comptes_bancaires.service.impl.OrganismeBancaireImpl;
import com.isamm.projet.gestion_comptes_bancaires.util.MongoConnection;
import com.isamm.projet.gestion_comptes_bancaires.util.ObjectLogger;

/**
 * 
 * Bank accounts management application
 * main class
 *
 */

public class App
{
	
	private static MongoConnection mongoConnection = MongoConnection.getMongoConnection();
	private static OrganismeBancaireImpl organismeBancaireImpl = new OrganismeBancaireImpl();
	private static Banque banque = organismeBancaireImpl.initBanque();
	private static Scanner sc = new Scanner(System.in);
	
	/**
	 * if there is a client with the provided codeTitulaire in this bank system as a PersonnePhysique, don't create a new PersonnePhysique
	 * we associate the new CompteBancaireParticulier to the PersonnePhysique found
	 * else check if there is a PersonneMorale with the same codeTitulaire, in order to assign its data to the new PersonnePhysique
	 * if neither is found create a new PersonnePhysique
	 * 
	 * @param typeCompteBancaireParticulier
	 * @param codeTitulaire
	 */
	public static void createCompteBancaireParticulier(TypeCompteBancaireParticulier typeCompteBancaireParticulier, int codeTitulaire) {
				
		TitulaireDeCompte titulaireDeCompte = organismeBancaireImpl.findTitulaireDeCompteByTypeAndCodeInBank((List<ObjectId>) banque.getTitulaires(), TypeTitulaireDeCompte.PERSONNE_PHYSIQUE, codeTitulaire);
		
		if(titulaireDeCompte == null) {
			
			// there is no PersonnePhysique with the given codeTitulaire
			
			PersonnePhysique personnePhysique = null;
			
			titulaireDeCompte = organismeBancaireImpl.findTitulaireDeCompteByTypeAndCodeInBank((List<ObjectId>) banque.getTitulaires(), TypeTitulaireDeCompte.PERSONNE_MORALE, codeTitulaire);
			
			if(titulaireDeCompte == null) {
				
				// there is no PersonneMorale with the given codeTitulaire
				
				String nomTitulaire = "";
				System.out.println("");
	    		System.out.println("Donner le nom du client");
	    		System.out.println("");
	    		nomTitulaire = sc.next();
	    		
	    		String adresseContact = "";
	    		System.out.println("");
	    		System.out.println("Donner l\'adresse du client");
	    		System.out.println("");
	    		adresseContact = sc.next();

	    		personnePhysique = new PersonnePhysique(codeTitulaire, nomTitulaire, adresseContact, 0);
	    		
			} else {
				
				// there is PersonneMorale with the same codeTitulaire use the same values
				personnePhysique = new PersonnePhysique(codeTitulaire, titulaireDeCompte.getNomTitulaire(), titulaireDeCompte.getAdresseContact(), 0);
			
			}
			
			personnePhysique = (PersonnePhysique) organismeBancaireImpl.createTitulaireDeCompte(personnePhysique);
			
			System.out.println("");
			System.out.println("Titulaire de compte ajoute entant q\'une personne physique");
			System.out.println("");
			
			ObjectLogger.log(personnePhysique);
			
			// add the client Id to the bank
			List<ObjectId> titulaires = (List<ObjectId>) banque.getTitulaires();
			titulaires.add(personnePhysique.get_id());
			banque = organismeBancaireImpl.addToBank(banque.get_id(), "titulaires", titulaires);
			
		} else {
			System.out.println("");
			System.out.println("Titulaire de compte existe deja entant q\'une personne physique");
			System.out.println("");
		}
		
		float decouvertAutorise = 0;
		int numeroCarteBancaire = 0;
		
		// only CompteBancaireParticulierCourant has a decouvertAutorise and a numeroCarteBancaire
		if (typeCompteBancaireParticulier.equals(TypeCompteBancaireParticulier.COMPTE_COURANT)) {
			do {
				System.out.println("");
	    		System.out.println("Donner le decouvert autorise");
	    		System.out.println("");
	    		try {
	    			decouvertAutorise = sc.nextFloat();
	    		} catch(Exception e) {
	    			sc.next();
	    		}
			} while(decouvertAutorise <= 0);
			
			numeroCarteBancaire = organismeBancaireImpl.generateCardNumber();
		}
		
		
		float debitMaximalAutorise = 0;
		do {
			System.out.println("");
    		System.out.println("Donner le debit maximal autorise");
    		System.out.println("");
    		try {
    			debitMaximalAutorise = sc.nextFloat();
    		} catch(Exception e) {
    			sc.next();
    		}
		} while(debitMaximalAutorise <= 0);
		
		Historique historique = new Historique(TypeOperation.CREATION, 0);
		historique = organismeBancaireImpl.addToHistory(historique);
		List<ObjectId> historiques = new ArrayList<ObjectId>();
		historiques.add(historique.get_id());
		
		CompteBancaireParticulier compteBancaireParticulier = new CompteBancaireParticulier(organismeBancaireImpl.generateIban(banque.getCodeBanque()), codeTitulaire, decouvertAutorise, historiques, typeCompteBancaireParticulier, debitMaximalAutorise, numeroCarteBancaire);
		compteBancaireParticulier = organismeBancaireImpl.creerCompteBancaireParticulier(compteBancaireParticulier);
		
		System.out.println("");
		System.out.println("Compte cree");
		System.out.println("");
		
		ObjectLogger.log(compteBancaireParticulier);
		
		// add the account Id to the bank
		List<ObjectId> comptes = (List<ObjectId>) banque.getComptes();
		comptes.add(compteBancaireParticulier.get_id());
		banque = organismeBancaireImpl.addToBank(banque.get_id(), "comptes", comptes);
		
	}
	
	/**
	 * check if there is a PersonnePhysique with the same codeTitulaire, in order to assign its data to the new PersonneMorale
	 * otherwise create a new PersonneMorale
	 *  
	 * @param codeTitulaire
	 */
	public static void createCompteBancaireEntreprise(int codeTitulaire) {
		
		String nomTitulaire = "";
		String adresseContact = "";
		
		TitulaireDeCompte titulaireDeCompte = organismeBancaireImpl.findTitulaireDeCompteByTypeAndCodeInBank((List<ObjectId>) banque.getTitulaires(), TypeTitulaireDeCompte.PERSONNE_PHYSIQUE, codeTitulaire);
		
		if(titulaireDeCompte == null) {
			
			// there is no PersonnePhysique with the given codeTitulaire
			
			System.out.println("");
    		System.out.println("Donner le nom du client");
    		System.out.println("");
    		nomTitulaire = sc.next();
    		
    		System.out.println("");
    		System.out.println("Donner l\'adresse du client");
    		System.out.println("");
    		adresseContact = sc.next();
    		
		} else {
			
			// there is a PersonnePhysique with the given codeTitulaire use its values
			
			nomTitulaire = titulaireDeCompte.getNomTitulaire();
			adresseContact = titulaireDeCompte.getAdresseContact();

		}
		
		String nomCommercial = "";
		System.out.println("");
		System.out.println("Donner le nom commercial du client");
		System.out.println("");
		nomCommercial = sc.next();
		
		PersonneMorale personneMorale = new PersonneMorale(codeTitulaire, nomTitulaire, adresseContact, nomCommercial);
		personneMorale = (PersonneMorale) organismeBancaireImpl.createTitulaireDeCompte(personneMorale);
		
		System.out.println("");
		System.out.println("Titulaire de compte ajoute entant q\'une personne morale");
		System.out.println("");
		
		ObjectLogger.log(personneMorale);
		
		// add the client Id to the bank
		List<ObjectId> titulaires = (List<ObjectId>) banque.getTitulaires();
		titulaires.add(personneMorale.get_id());
		banque = organismeBancaireImpl.addToBank(banque.get_id(), "titulaires", titulaires);
		
		float decouvertAutorise = 0;
		do {
			System.out.println("");
    		System.out.println("Donner le decouvert autorise");
    		System.out.println("");
    		try {
    			decouvertAutorise = sc.nextFloat();
    		} catch(Exception e) {
    			sc.next();
    		}
		} while(decouvertAutorise <= 0);
		
		String regimeFiscale = "";
		System.out.println("");
		System.out.println("Donner le regime fiscale du client");
		System.out.println("");
		regimeFiscale = sc.next();
		
		Historique historique = new Historique(TypeOperation.CREATION, 0);
		historique = organismeBancaireImpl.addToHistory(historique);
		List<ObjectId> historiques = new ArrayList<ObjectId>();
		historiques.add(historique.get_id());
		
		CompteBancaireEntreprise compteBancaireEntreprise = new CompteBancaireEntreprise(organismeBancaireImpl.generateIban(banque.getCodeBanque()), codeTitulaire, decouvertAutorise, historiques, regimeFiscale);
		compteBancaireEntreprise = organismeBancaireImpl.creerCompteBancaireEntreprise(compteBancaireEntreprise);

		System.out.println("");
		System.out.println("Compte cree");
		System.out.println("");
		
		ObjectLogger.log(compteBancaireEntreprise);
		
		// add the account Id to the bank
		List<ObjectId> comptes = (List<ObjectId>) banque.getComptes();
		comptes.add(compteBancaireEntreprise.get_id());
		banque = organismeBancaireImpl.addToBank(banque.get_id(), "comptes", comptes);
		
	}
	
    public static void main( String[] args )
    {
    	
    	char choice = 0;
    	
    	ObjectLogger.log(banque);
    	
    	do {
    		
    		System.out.println("");
    		System.out.println("* Gestion des comptes bancaires");
    		System.out.println("");
    		System.out.println("a. Creer un compte");
    		System.out.println("b. Chercher un compte");
    		System.out.println("");
    		System.out.println("q. Quitter");
    		System.out.println("");
    		
    		do {
				System.out.println("");
				System.out.print("Saisir un choix ");
				System.out.println("");
				choice = sc.next().charAt(0);
			} while (choice != 'a' && choice != 'b' && choice != 'q');
    		
    		switch(choice) {
	    		case 'a': {
					System.out.println("");
					System.out.println("* Gestion des comptes bancaires");
		    		System.out.println("** Creer un compte");
		    		System.out.println("");
		    		
		    		int codeTitulaire = 0;
		    		
		    		do {
		    			System.out.println("");
			    		System.out.println("Donner l\'identifiant du client");
			    		System.out.println("");
			    		try {
			    			codeTitulaire = sc.nextInt();
			    		} catch (Exception e) {
			    			sc.next();
			    		}
		    			
		    		} while(codeTitulaire <= 0);
		    		
		    		System.out.println("");
		    		System.out.println("Choisir un type de compte");
		    		System.out.println("");
		    		System.out.println("a. Un compte particulier");
		    		System.out.println("b. Un compte entreprise");
		    		System.out.println("");
		    		
		    		do {
		    			System.out.println("");
						System.out.print("Saisir un choix ");
						System.out.println("");
						choice = sc.next().charAt(0);
		    		} while (choice != 'a' && choice != 'b');
		    		
		    		if(choice == 'a') {
		    			
		    			System.out.println("");
			    		System.out.println("Choisir un type de compte particulier");
			    		System.out.println("");
		    			System.out.println("");
			    		System.out.println("a. Un compte courant");
			    		System.out.println("b. Un compte epargne");
			    		System.out.println("");
			    		
			    		do {
			    			System.out.println("");
							System.out.print("Saisir un choix ");
							System.out.println("");
							choice = sc.next().charAt(0);
			    		} while (choice != 'a' && choice != 'b');
			    		
			    		if(choice == 'a') {
			    			
			    			if(organismeBancaireImpl.allowAccountCreation((List<ObjectId>) banque.getComptes(), codeTitulaire, TypeCompteBancaire.COMPTE_BANCAIRE_PARTICULIER, TypeCompteBancaireParticulier.COMPTE_COURANT)) {
			    				
			    				createCompteBancaireParticulier(TypeCompteBancaireParticulier.COMPTE_COURANT, codeTitulaire);
			    				
			    			} else {
			    				System.out.println("");
								System.out.print("Un compte particulier courant existe pour ce client");
								System.out.println("");
			    			}
			    			
			    		} else if(choice == 'b') {
			    			
			    			if(organismeBancaireImpl.allowAccountCreation((List<ObjectId>) banque.getComptes(), codeTitulaire, TypeCompteBancaire.COMPTE_BANCAIRE_PARTICULIER, TypeCompteBancaireParticulier.COMPTE_EPARGNE)) {
			    				
			    				createCompteBancaireParticulier(TypeCompteBancaireParticulier.COMPTE_EPARGNE, codeTitulaire);

			    			} else {
			    				System.out.println("");
								System.out.print("Un compte particulier epargne existe pour ce client");
								System.out.println("");
			    			}

			    		}
		    		} else if(choice == 'b') {
		    			
		    			if(organismeBancaireImpl.allowAccountCreation((List<ObjectId>) banque.getComptes(), codeTitulaire, TypeCompteBancaire.COMPTE_BANCAIRE_ENTREPRISE, null)) {
		    				
		    				createCompteBancaireEntreprise(codeTitulaire);
		    				
		    			} else {
		    				System.out.println("");
							System.out.print("Un compte entreprise existe pour ce client");
							System.out.println("");
		    			}
		    			
		    		}
		    		
					break;
				}
    			case 'b': {
    				System.out.println("");
    				System.out.println("* Gestion des comptes bancaires");
		    		System.out.println("** Chercher un compte");
		    		System.out.println("");
    				
		    		String iban = "";
		    		do {
    					System.out.println("");
    		    		System.out.println("Donner IBAN du compte");
    		    		System.out.println("");
    		    		iban = sc.next();
    				} while (iban.trim().equals(""));
		    		
		    		CompteBancaire compteBancaire = null;
		    		
		    		try {
						compteBancaire = organismeBancaireImpl.findCompteBancaireByIbanInBank((List<ObjectId>) banque.getComptes(), iban);
					} catch (CompteBancaireInexistant e) {
						System.out.println("");
						System.out.println(e.getMessage());
						System.out.println("");
					}
		    		
		    		if (compteBancaire != null) {
		    			
		    			TitulaireDeCompte titulaireDeCompte = null;
		    			
		    			if (compteBancaire.getTypeCompteBancaire().equals(TypeCompteBancaire.COMPTE_BANCAIRE_PARTICULIER)) {
		    				titulaireDeCompte = organismeBancaireImpl.findTitulaireDeCompteByTypeAndCodeInBank((List<ObjectId>) banque.getTitulaires(), TypeTitulaireDeCompte.PERSONNE_PHYSIQUE, compteBancaire.getCodeTitulaire());
		    			} else if (compteBancaire.getTypeCompteBancaire().equals(TypeCompteBancaire.COMPTE_BANCAIRE_ENTREPRISE)) {
		    				titulaireDeCompte = organismeBancaireImpl.findTitulaireDeCompteByTypeAndCodeInBank((List<ObjectId>) banque.getTitulaires(), TypeTitulaireDeCompte.PERSONNE_MORALE, compteBancaire.getCodeTitulaire());
		    			}
		    			
		    			System.out.println("Titulaire de compte :");
		    			ObjectLogger.log(titulaireDeCompte);
		    			
		    			System.out.println("Compte Bancaire :");
		    			ObjectLogger.log(compteBancaire);
		    			
			    		do {
			    			System.out.println("");
		    				System.out.println("* Gestion des comptes bancaires");
				    		System.out.println("** Operations sur le compte");
				    		System.out.println("");
				    		if (compteBancaire.isEstOuvert()) {
				    			System.out.println("a. Crediter le compte");
					    		System.out.println("b. Debiter le compte");
					    		System.out.println("c. Effectuer un virement vers un autre compte");
					    		System.out.println("d. Afficher historique du compte");
					    		System.out.println("e. Afficher la date de creation du compte");
					    		System.out.println("f. Afficher la date du dernier versement");
					    		System.out.println("g. Fermer le compte");
				    		} else {
					    		System.out.println("d. Afficher historique du compte");
					    		System.out.println("e. Afficher la date de creation du compte");
					    		System.out.println("f. Afficher la date du dernier versement");
				    		}
				    		System.out.println("");
				    		System.out.println("r. Retourner");
				    		System.out.println("");
				    		
				    		if (compteBancaire.isEstOuvert()) {
				    			do {
					    			System.out.println("");
									System.out.print("Saisir un choix ");
									System.out.println("");
									choice = sc.next().charAt(0);
					    		} while (choice != 'a' && choice != 'b' && choice != 'c' && choice != 'd' && choice != 'e' && choice != 'f' && choice != 'g' && choice != 'h' && choice != 'r');
				    		} else {
				    			do {
					    			System.out.println("");
									System.out.print("Saisir un choix ");
									System.out.println("");
									choice = sc.next().charAt(0);
					    		} while (choice != 'd' && choice != 'e' && choice != 'f' && choice != 'g' && choice != 'r');
				    		}
				    		
				    		switch(choice) {
				    			case 'a': {
				    				float money = 0;
				    				do {
				    					System.out.println("");
										System.out.print("Donner un montant ");
										System.out.println("");
										money = sc.nextFloat();
				    				} while (money <= 0);
				    				
				    				boolean confirmation = organismeBancaireImpl.crediterCompte(compteBancaire.get_id(), money);
				    				
				    				if (confirmation) {
				    					System.out.println("");
										System.out.print("Operation effectuee");
										System.out.println("");
										compteBancaire = organismeBancaireImpl.findCompteBancaireById(compteBancaire.get_id());
										ObjectLogger.log(compteBancaire);
				    				} else {
				    					System.out.println("");
										System.out.print("Operation echouee, ressayez plus tard");
										System.out.println("");
				    				}
				    				
				    				break;
				    			}
				    			case 'b': {
				    				float money = 0;
				    				do {
				    					System.out.println("");
										System.out.print("Donner un montant ");
										System.out.println("");
										money = sc.nextFloat();
				    				} while (money <= 0);
				    				
				    				boolean confirmation;
				    				
				    				try {
										confirmation = organismeBancaireImpl.debiterCompte(compteBancaire.get_id(), money);
									} catch (OperationNonAutorisee e) {
										confirmation = false;
										System.out.println("");
										System.out.println(e.getMessage());
										System.out.println("");
									}
				    				
				    				if (confirmation) {
				    					System.out.println("");
										System.out.print("Operation effectuee");
										System.out.println("");
										compteBancaire = organismeBancaireImpl.findCompteBancaireById(compteBancaire.get_id());
										ObjectLogger.log(compteBancaire);
				    				} else {
				    					System.out.println("");
										System.out.print("Operation echouee, ressayez plus tard");
										System.out.println("");
				    				}
				    				
				    				break;
				    			}
				    			case 'c': {
				    				String ibanDestination;
				    				System.out.println("");
									System.out.print("Donner iban du compte destination ");
									System.out.println("");
									ibanDestination = sc.next();
									
									CompteBancaire compteBancaireDestination = null;
									try {
										compteBancaireDestination = organismeBancaireImpl.findCompteBancaireByIbanInBank((List<ObjectId>) banque.getComptes(), ibanDestination);
									} catch (CompteBancaireInexistant e) {
										System.out.println("");
										System.out.println(e.getMessage());
										System.out.println("");
									}
									
									if (compteBancaireDestination != null) {
										if (compteBancaireDestination.isEstOuvert()) {
											float money = 0;
						    				do {
						    					System.out.println("");
												System.out.print("Donner un montant ");
												System.out.println("");
												money = sc.nextFloat();
						    				} while (money <= 0);
						    				
						    				boolean confirmation;
						    				
						    				try {
												confirmation = organismeBancaireImpl.virerArgent(compteBancaire.get_id(), compteBancaireDestination.get_id(), money);
											} catch (OperationNonAutorisee e) {
												confirmation = false;
												System.out.println("");
												System.out.println(e.getMessage());
												System.out.println("");
											}
						    				
						    				if (confirmation) {
						    					System.out.println("");
												System.out.print("Operation effectuee");
												System.out.println("");
												compteBancaire = organismeBancaireImpl.findCompteBancaireById(compteBancaire.get_id());
												System.out.println("compte bancaire source");
												ObjectLogger.log(compteBancaire);
												compteBancaireDestination = organismeBancaireImpl.findCompteBancaireById(compteBancaireDestination.get_id());
												System.out.println("compte bancaire destination");
												ObjectLogger.log(compteBancaireDestination);
						    				} else {
						    					System.out.println("");
												System.out.print("Operation echouee, ressayez plus tard");
												System.out.println("");
						    				}
						    				
										} else {
											System.out.println("");
											System.out.println("compte bancaire destination est ferme");
											System.out.println("");
										}
									}
				    				
				    				break;
				    			}
				    			case 'd': {
				    				List<Historique> historiques = organismeBancaireImpl.getAccountHistory((List<ObjectId>) compteBancaire.getHistoriques());
				    				
				    				for (Historique historique : historiques) {
				    					ObjectLogger.log(historique);
				    				}
				    				
				    				break;
				    			}
				    			case 'e': {
				    				LocalDateTime now = LocalDateTime.now();
				    				LocalDateTime dateCreation = LocalDateTime.ofInstant(compteBancaire.getDateCreationCompte().toInstant(), ZoneId.systemDefault());
				    				Period period = Period.between(now.toLocalDate(), dateCreation.toLocalDate());
				    				Duration duration = Duration.between(now.toLocalTime(), dateCreation.toLocalTime()).abs();
				    				System.out.println("");
				    				System.out.println("Date courante: " + now.getDayOfMonth() + "/" + now.getMonthValue() + "/" + now.getYear() + " - " + now.getHour() + ":" + now.getMinute() + ":" + now.getSecond());
				    				System.out.println("Compte cree le: " + dateCreation.getDayOfMonth() + "/" + dateCreation.getMonthValue() + "/" + dateCreation.getYear() + " a: " + dateCreation.getHour() + ":" + dateCreation.getMinute() + ":" + dateCreation.getSecond());
				    				System.out.println("Depuis: " + (duration.toMinutes() - duration.toHours() * 60) + " minutes " + duration.toHours() + " heures " + period.getDays() + " jours " + period.getMonths() + " mois " + period.getYears() + " annees");
				    				System.out.println("");
				    				break;
				    			}
				    			case 'f': {
				    				
				    				Historique historique = organismeBancaireImpl.getLastDeposit((List<ObjectId>) compteBancaire.getHistoriques());
				    				
				    				if (historique == null) {
				    					System.out.println("");
				    					System.out.println("Pas de versement effectuer sur ce compte");
				    					System.out.println("");
				    				} else {
				    					LocalDateTime now = LocalDateTime.now();
				    					LocalDateTime lastDeposit = LocalDateTime.ofInstant(historique.getDateOperation().toInstant(), ZoneId.systemDefault());
				    					Period period = Period.between(now.toLocalDate(), lastDeposit.toLocalDate());
					    				Duration duration = Duration.between(now.toLocalTime(), lastDeposit.toLocalTime()).abs();
					    				System.out.println("");
					    				System.out.println("Date courante: " + now.getDayOfMonth() + "/" + now.getMonthValue() + "/" + now.getYear() + " - " + now.getHour() + ":" + now.getMinute() + ":" + now.getSecond());
					    				System.out.println("Date dernier versement: " + lastDeposit.getDayOfMonth() + "/" + lastDeposit.getMonthValue() + "/" + lastDeposit.getYear() + " a: " + lastDeposit.getHour() + ":" + lastDeposit.getMinute() + ":" + lastDeposit.getSecond());
					    				System.out.println("Depuis: " + (duration.toMinutes() - duration.toHours() * 60) + " minutes " + duration.toHours() + " heures " + period.getDays() + " jours " + period.getMonths() + " mois " + period.getYears() + " annees");
					    				System.out.println("");
				    				}
				    				
				    				break;
				    			}
				    			case 'g': {
				    				do {
										System.out.println("");
										System.out.println("Voulez vouz vraiment fermer ce compte? [Y/N]");
										System.out.println("");
										choice = sc.next().charAt(0);
									} while (choice != 'Y' && choice != 'N');
									
									if (choice == 'Y') {
										boolean close = organismeBancaireImpl.fermerCompte(compteBancaire.get_id());
										
										if (!close) {
											System.out.println("");
											System.out.println("Compte ferme");
											System.out.println("");
											compteBancaire = organismeBancaireImpl.addHistoryToAccount(compteBancaire.get_id(), TypeOperation.FERMETURE, compteBancaire.getSolde());
											choice = 'r';
										} else {
											System.out.println("");
											System.out.println("Une erreur inattendue, ressayez plus tard");
											System.out.println("");
										}
										
									}
									
				    				break;
				    			}
				    		}
				    		
			    		} while (choice != 'r');
			    		
		    		}
    				
		    		break;
    			}
    		}
    		
    	} while(choice != 'q');
    	
    	sc.close();
    	mongoConnection.close();
    	
    }
    
}
