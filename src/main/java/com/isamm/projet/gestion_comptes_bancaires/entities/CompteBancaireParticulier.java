package com.isamm.projet.gestion_comptes_bancaires.entities;

import java.util.Collection;
import java.util.Date;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeCompteBancaire;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeCompteBancaireParticulier;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CompteBancaireParticulier extends CompteBancaire {

	private static final long serialVersionUID = 1L;
	
	private TypeCompteBancaireParticulier typeCompteBancaireParticulier;
	private float debitMaximalAutorise;
	private int numeroCarteBancaire;
	
	public CompteBancaireParticulier() {
		super();
	}

	@JsonCreator
	public CompteBancaireParticulier(@JsonProperty("iban") String iban,
									 @JsonProperty("codeTitulaire") int codeTitulaire,
									 @JsonProperty("decouvertAutorise") float decouvertAutorise,
									 @JsonProperty("historiques") Collection<ObjectId> historiques,
									 @JsonProperty("typeCompteBancaireParticulier") TypeCompteBancaireParticulier typeCompteBancaireParticulier,
									 @JsonProperty("debitMaximalAutorise") float debitMaximalAutorise,
									 @JsonProperty("numeroCarteBancaire") int numeroCarteBancaire) {
		super(new ObjectId(), TypeCompteBancaire.COMPTE_BANCAIRE_PARTICULIER, iban, new Date(), true, codeTitulaire, 0, decouvertAutorise, historiques);
		this.typeCompteBancaireParticulier = typeCompteBancaireParticulier;
		this.debitMaximalAutorise = debitMaximalAutorise;
		this.numeroCarteBancaire = numeroCarteBancaire;
	}

	@JsonProperty("typeCompteBancaireParticulier")
	public TypeCompteBancaireParticulier getTypeCompteBancaireParticulier() {
		return typeCompteBancaireParticulier;
	}

	public void setTypeCompteBancaireParticulier(TypeCompteBancaireParticulier typeCompteBancaireParticulier) {
		this.typeCompteBancaireParticulier = typeCompteBancaireParticulier;
	}

	@JsonProperty("debitMaximalAutorise")
	public float getDebitMaximalAutorise() {
		return debitMaximalAutorise;
	}

	public void setDebitMaximalAutorise(float debitMaximalAutorise) {
		this.debitMaximalAutorise = debitMaximalAutorise;
	}

	@JsonProperty("numeroCarteBancaire")
	public int getNumeroCarteBancaire() {
		return numeroCarteBancaire;
	}

	public void setNumeroCarteBancaire(int numeroCarteBancaire) {
		this.numeroCarteBancaire = numeroCarteBancaire;
	}
	
}
