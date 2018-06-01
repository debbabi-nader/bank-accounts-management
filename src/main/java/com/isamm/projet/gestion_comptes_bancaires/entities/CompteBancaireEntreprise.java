package com.isamm.projet.gestion_comptes_bancaires.entities;

import java.util.Collection;
import java.util.Date;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeCompteBancaire;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CompteBancaireEntreprise extends CompteBancaire {

	private static final long serialVersionUID = 1L;
	
	private String regimeFiscale;

	public CompteBancaireEntreprise() {
		super();
	}

	@JsonCreator
	public CompteBancaireEntreprise(@JsonProperty("iban") String iban,
									@JsonProperty("codeTitulaire") int codeTitulaire,
									@JsonProperty("decouvertAutorise") float decouvertAutorise,
									@JsonProperty("historiques") Collection<ObjectId> historiques,
									@JsonProperty("regimeFiscale") String regimeFiscale) {
		super(new ObjectId(), TypeCompteBancaire.COMPTE_BANCAIRE_ENTREPRISE, iban, new Date(), true, codeTitulaire, 0, decouvertAutorise, historiques);
		this.regimeFiscale = regimeFiscale;
	}

	@JsonProperty("regimeFiscale")
	public String getRegimeFiscale() {
		return regimeFiscale;
	}

	public void setRegimeFiscale(String regimeFiscale) {
		this.regimeFiscale = regimeFiscale;
	}

}
