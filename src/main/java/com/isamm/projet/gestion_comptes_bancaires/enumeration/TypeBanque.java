package com.isamm.projet.gestion_comptes_bancaires.enumeration;


public enum TypeBanque {
	
	BANQUE_A(Constants.BANQUE_A_TYPE);
	
	private TypeBanque(String typeBanqueString) {
		
	}

	public static class Constants {
		public static final String BANQUE_A_TYPE = "BANQUE_A";
	}
	
}
