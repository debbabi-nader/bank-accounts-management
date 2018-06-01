package com.isamm.projet.gestion_comptes_bancaires.entities;

import java.io.Serializable;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeTitulaireDeCompte;


@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "typeTitulaireDeCompte")
@JsonSubTypes({
	@JsonSubTypes.Type(value = PersonneMorale.class, name = TypeTitulaireDeCompte.Constants.PERSONNE_MORALE_TYPE),
	@JsonSubTypes.Type(value = PersonnePhysique.class, name = TypeTitulaireDeCompte.Constants.PERSONNE_PHYSIQUE_TYPE)
})
public abstract class TitulaireDeCompte implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ObjectId _id;
	private TypeTitulaireDeCompte typeTitulaireDeCompte;
	private int codeTitulaire;
	private String nomTitulaire;
	private String adresseContact;
	
	public TitulaireDeCompte() {
		super();
	}

	public TitulaireDeCompte(ObjectId _id, TypeTitulaireDeCompte typeTitulaireDeCompte, int codeTitulaire, String nomTitulaire,
			String adresseContact) {
		super();
		this._id = _id;
		this.typeTitulaireDeCompte = typeTitulaireDeCompte;
		this.codeTitulaire = codeTitulaire;
		this.nomTitulaire = nomTitulaire;
		this.adresseContact = adresseContact;
	}

	@JsonProperty("_id")
	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	@JsonProperty("typeTitulaireDeCompte")
	public TypeTitulaireDeCompte getTypeTitulaireDeCompte() {
		return typeTitulaireDeCompte;
	}

	public void setTypeTitulaireDeCompte(TypeTitulaireDeCompte typeTitulaireDeCompte) {
		this.typeTitulaireDeCompte = typeTitulaireDeCompte;
	}

	@JsonProperty("codeTitulaire")
	public int getCodeTitulaire() {
		return codeTitulaire;
	}

	public void setCodeTitulaire(int codeTitulaire) {
		this.codeTitulaire = codeTitulaire;
	}

	@JsonProperty("nomTitulaire")
	public String getNomTitulaire() {
		return nomTitulaire;
	}

	public void setNomTitulaire(String nomTitulaire) {
		this.nomTitulaire = nomTitulaire;
	}

	@JsonProperty("adresseContact")
	public String getAdresseContact() {
		return adresseContact;
	}

	public void setAdresseContact(String adresseContact) {
		this.adresseContact = adresseContact;
	}

}
