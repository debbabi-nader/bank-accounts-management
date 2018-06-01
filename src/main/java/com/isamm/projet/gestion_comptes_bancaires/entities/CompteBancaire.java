package com.isamm.projet.gestion_comptes_bancaires.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeCompteBancaire;


@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "typeCompteBancaire")
@JsonSubTypes({
	@JsonSubTypes.Type(value = CompteBancaireEntreprise.class, name = TypeCompteBancaire.Constants.COMPTE_BANCAIRE_ENTREPRISE_TYPE),
	@JsonSubTypes.Type(value = CompteBancaireParticulier.class, name = TypeCompteBancaire.Constants.COMPTE_BANCAIRE_PARTICULIER_TYPE)
})
public abstract class CompteBancaire implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ObjectId _id;
	private TypeCompteBancaire typeCompteBancaire;
	private String iban;
	private Date dateCreationCompte;
	private boolean estOuvert;
	private int codeTitulaire;
	private float solde;
	private float decouvertAutorise;
	private Collection<ObjectId> historiques;
	
	public CompteBancaire() {
		super();
	}

	public CompteBancaire(ObjectId _id, TypeCompteBancaire typeCompteBancaire, String iban, Date dateCreationCompte, boolean estOuvert, int codeTitulaire,
			float solde, float decouvertAutorise, Collection<ObjectId> historiques) {
		super();
		this._id = _id;
		this.typeCompteBancaire = typeCompteBancaire;
		this.iban = iban;
		this.dateCreationCompte = dateCreationCompte;
		this.estOuvert = estOuvert;
		this.codeTitulaire = codeTitulaire;
		this.solde = solde;
		this.decouvertAutorise = decouvertAutorise;
		this.historiques = historiques;
	}

	@JsonProperty("_id")
	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	@JsonProperty("typeCompteBancaire")
	public TypeCompteBancaire getTypeCompteBancaire() {
		return typeCompteBancaire;
	}

	public void setTypeCompteBancaire(TypeCompteBancaire typeCompteBancaire) {
		this.typeCompteBancaire = typeCompteBancaire;
	}

	@JsonProperty("iban")
	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	@JsonProperty("dateCreationCompte")
	public Date getDateCreationCompte() {
		return dateCreationCompte;
	}

	public void setDateCreationCompte(Date dateCreationCompte) {
		this.dateCreationCompte = dateCreationCompte;
	}

	@JsonProperty("estOuvert")
	public boolean isEstOuvert() {
		return estOuvert;
	}

	public void setEstOuvert(boolean estOuvert) {
		this.estOuvert = estOuvert;
	}

	@JsonProperty("codeTitulaire")
	public int getCodeTitulaire() {
		return codeTitulaire;
	}

	public void setCodeTitulaire(int codeTitulaire) {
		this.codeTitulaire = codeTitulaire;
	}

	@JsonProperty("solde")
	public float getSolde() {
		return solde;
	}

	public void setSolde(float solde) {
		this.solde = solde;
	}

	@JsonProperty("decouvertAutorise")
	public float getDecouvertAutorise() {
		return decouvertAutorise;
	}

	public void setDecouvertAutorise(float decouvertAutorise) {
		this.decouvertAutorise = decouvertAutorise;
	}

	@JsonProperty("historiques")
	public Collection<ObjectId> getHistoriques() {
		return historiques;
	}

	public void setHistoriques(Collection<ObjectId> historiques) {
		this.historiques = historiques;
	}
	
}
