package com.isamm.projet.gestion_comptes_bancaires.enumeration;


public enum TypeCompteBancaire {
	
	COMPTE_BANCAIRE_PARTICULIER(Constants.COMPTE_BANCAIRE_PARTICULIER_TYPE), COMPTE_BANCAIRE_ENTREPRISE(Constants.COMPTE_BANCAIRE_ENTREPRISE_TYPE);
	
	private TypeCompteBancaire(String typeCompteBancaire) {
		
	}
	
	public static class Constants {
		public static final String COMPTE_BANCAIRE_PARTICULIER_TYPE = "COMPTE_BANCAIRE_PARTICULIER";
		public static final String COMPTE_BANCAIRE_ENTREPRISE_TYPE = "COMPTE_BANCAIRE_ENTREPRISE";
	}

}
