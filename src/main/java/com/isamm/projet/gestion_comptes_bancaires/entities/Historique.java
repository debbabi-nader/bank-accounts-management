package com.isamm.projet.gestion_comptes_bancaires.entities;

import java.io.Serializable;
import java.util.Date;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeOperation;


public class Historique implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ObjectId _id;
	private Date dateOperation;
	private TypeOperation typeOperation;
	private float solde;
	
	public Historique() {
		super();
	}

	public Historique(TypeOperation typeOperation, float solde) {
		super();
		this._id = new ObjectId();
		this.dateOperation = new Date();
		this.typeOperation = typeOperation;
		this.solde = solde;
	}

	@JsonProperty("_id")
	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	@JsonProperty("dateOperation")
	public Date getDateOperation() {
		return dateOperation;
	}

	public void setDateOperation(Date dateOperation) {
		this.dateOperation = dateOperation;
	}

	@JsonProperty("typeOperation")
	public TypeOperation getTypeOperation() {
		return typeOperation;
	}

	public void setTypeOperation(TypeOperation typeOperation) {
		this.typeOperation = typeOperation;
	}

	@JsonProperty("solde")
	public float getSolde() {
		return solde;
	}

	public void setSolde(float solde) {
		this.solde = solde;
	}
	
}
