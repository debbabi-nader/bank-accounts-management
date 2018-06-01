package com.isamm.projet.gestion_comptes_bancaires.exception;


public class OperationNonAutorisee extends ExceptionBancaire {

	private static final long serialVersionUID = 1L;

	public OperationNonAutorisee(String meassage) {
		super(meassage);
	}

}
