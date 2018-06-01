package com.isamm.projet.gestion_comptes_bancaires.enumeration;


public enum TypeTitulaireDeCompte {
	
	PERSONNE_MORALE(Constants.PERSONNE_MORALE_TYPE), PERSONNE_PHYSIQUE(Constants.PERSONNE_PHYSIQUE_TYPE);
	
	private TypeTitulaireDeCompte(String typeTitulaireDeCompte) {
		
	}
	
	public static class Constants {
		public static final String PERSONNE_MORALE_TYPE = "PERSONNE_MORALE";
		public static final String PERSONNE_PHYSIQUE_TYPE = "PERSONNE_PHYSIQUE";
	}
	
}
