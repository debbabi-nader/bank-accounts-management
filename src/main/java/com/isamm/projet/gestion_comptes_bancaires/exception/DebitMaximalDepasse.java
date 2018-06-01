package com.isamm.projet.gestion_comptes_bancaires.exception;


public class DebitMaximalDepasse extends OperationNonAutorisee {

	private static final long serialVersionUID = 1L;

	public DebitMaximalDepasse(String meassage) {
		super(meassage);
	}

}
