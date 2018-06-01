package com.isamm.projet.gestion_comptes_bancaires.entities;

import java.io.Serializable;
import java.util.Collection;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeBanque;


@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "typeBanque")
@JsonSubTypes({
	@JsonSubTypes.Type(value = BanqueA.class, name = TypeBanque.Constants.BANQUE_A_TYPE)
})
public abstract class Banque implements Serializable {

	private static final long serialVersionUID = 1L;

	private ObjectId _id;
	private TypeBanque typeBanque;
	private int codeBanque;
	private Collection<ObjectId> titulaires;
	private Collection<ObjectId> comptes;
	
	public Banque() {
		super();
	}

	public Banque(ObjectId _id, TypeBanque typeBanque, int codeBanque, Collection<ObjectId> titulaires, Collection<ObjectId> comptes) {
		super();
		this._id = _id;
		this.typeBanque = typeBanque;
		this.codeBanque = codeBanque;
		this.titulaires = titulaires;
		this.comptes = comptes;
	}

	@JsonProperty("_id")
	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	@JsonProperty("typeBanque")
	public TypeBanque getTypeBanque() {
		return typeBanque;
	}

	public void setTypeBanque(TypeBanque typeBanque) {
		this.typeBanque = typeBanque;
	}

	@JsonProperty("codeBanque")
	public int getCodeBanque() {
		return codeBanque;
	}

	public void setCodeBanque(int codeBanque) {
		this.codeBanque = codeBanque;
	}

	@JsonProperty("titulaires")
	public Collection<ObjectId> getTitulaires() {
		return titulaires;
	}

	public void setTitulaires(Collection<ObjectId> titulaires) {
		this.titulaires = titulaires;
	}

	@JsonProperty("comptes")
	public Collection<ObjectId> getComptes() {
		return comptes;
	}

	public void setComptes(Collection<ObjectId> comptes) {
		this.comptes = comptes;
	}
	
}
