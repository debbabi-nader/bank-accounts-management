package com.isamm.projet.gestion_comptes_bancaires.util;

import com.isamm.projet.gestion_comptes_bancaires.entities.Banque;
import com.isamm.projet.gestion_comptes_bancaires.entities.CompteBancaire;
import com.isamm.projet.gestion_comptes_bancaires.entities.CompteBancaireEntreprise;
import com.isamm.projet.gestion_comptes_bancaires.entities.CompteBancaireParticulier;
import com.isamm.projet.gestion_comptes_bancaires.entities.Historique;
import com.isamm.projet.gestion_comptes_bancaires.entities.PersonneMorale;
import com.isamm.projet.gestion_comptes_bancaires.entities.PersonnePhysique;
import com.isamm.projet.gestion_comptes_bancaires.entities.TitulaireDeCompte;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeCompteBancaire;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeTitulaireDeCompte;


public class ObjectLogger {
	
	private ObjectLogger() {
		
	}
	
	public static void log(Object o) {
		
		if (o != null) {
			
			System.out.println("");
			
			if(o instanceof Banque) {
				
				Banque banque = (Banque) o;
				System.out.println("*Id: " + banque.get_id().toString()
								   + " *code_banque: " + banque.getCodeBanque()
								   + " *titulaire: " + banque.getTitulaires()
								   + " *comptes: " + banque.getComptes());

			} else if(o instanceof CompteBancaire) {
				
				CompteBancaire compteBancaire = (CompteBancaire) o;
				System.out.print("*Id: " + compteBancaire.get_id().toString()
								 + " *type: " + compteBancaire.getTypeCompteBancaire()
								 + " *iban: " + compteBancaire.getIban()
								 + " *date_creation: " + compteBancaire.getDateCreationCompte()
								 + " *ouvert: " + compteBancaire.isEstOuvert()
								 + " *code_titulaire: " + compteBancaire.getCodeTitulaire()
								 + " *solde: " + compteBancaire.getSolde()
								 + " *decouvert_autorise: " + compteBancaire.getDecouvertAutorise()
								 + " *historique: " + compteBancaire.getHistoriques());
				
				if(compteBancaire.getTypeCompteBancaire().equals(TypeCompteBancaire.COMPTE_BANCAIRE_ENTREPRISE)) {
					
					CompteBancaireEntreprise compteBancaireEntreprise = (CompteBancaireEntreprise) compteBancaire;
					System.out.print(" *regimeFiscale: " + compteBancaireEntreprise.getRegimeFiscale());
					System.out.println("");
				
				} else if(compteBancaire.getTypeCompteBancaire().equals(TypeCompteBancaire.COMPTE_BANCAIRE_PARTICULIER)) {
				
					CompteBancaireParticulier compteBancaireParticulier = (CompteBancaireParticulier) compteBancaire;
					System.out.print(" *type_compte_particulier: " + compteBancaireParticulier.getTypeCompteBancaireParticulier()
									 + " *debit_max: " + compteBancaireParticulier.getDebitMaximalAutorise()
									 + " *num_carte_bancaire: " + compteBancaireParticulier.getNumeroCarteBancaire());
					System.out.println("");
					
				}

			} else if(o instanceof TitulaireDeCompte) {
				
				TitulaireDeCompte titulaireDeCompte = (TitulaireDeCompte) o;
				System.out.print("*Id: " + titulaireDeCompte.get_id().toString()
								 + " *type: " + titulaireDeCompte.getTypeTitulaireDeCompte()
								 + " *code_titulaire: " + titulaireDeCompte.getCodeTitulaire()
								 + " *nom: " + titulaireDeCompte.getNomTitulaire()
								 + " *adresse: " + titulaireDeCompte.getAdresseContact());
				
				if(titulaireDeCompte.getTypeTitulaireDeCompte().equals(TypeTitulaireDeCompte.PERSONNE_MORALE)) {
					
					PersonneMorale personneMorale = (PersonneMorale) titulaireDeCompte;
					System.out.print(" nom_commercial: " + personneMorale.getNomCommercial());
					System.out.println("");
					
				} else if(titulaireDeCompte.getTypeTitulaireDeCompte().equals(TypeTitulaireDeCompte.PERSONNE_PHYSIQUE)) {
					
					PersonnePhysique personnePhysique = (PersonnePhysique) titulaireDeCompte;
					System.out.print(" *points_fidelite: " + personnePhysique.getPointsFidelité());
					System.out.println("");
					
				}
				
			} else if(o instanceof Historique) {
				
				Historique historique = (Historique) o;
				System.out.println("*Id: " + historique.get_id().toString()
								   + " *date_operation: " + historique.getDateOperation()
								   + " *type_operation: " + historique.getTypeOperation()
								   + " *solde: " + historique.getSolde());
				
			}
			
			System.out.println("");
			
		}
		
	}
}
