package com.isamm.projet.gestion_comptes_bancaires.exception;


public class DecouvertAutoriseDepasse extends OperationNonAutorisee {

	private static final long serialVersionUID = 1L;

	public DecouvertAutoriseDepasse(String meassage) {
		super(meassage);
	}

}
