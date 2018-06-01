package com.isamm.projet.gestion_comptes_bancaires.entities;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeTitulaireDeCompte;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonnePhysique extends TitulaireDeCompte {
	
	private static final long serialVersionUID = 1L;
	
	private int pointsFidelite;

	public PersonnePhysique() {
		super();
	}

	@JsonCreator
	public PersonnePhysique(@JsonProperty("codeTitulaire") int codeTitulaire,
							@JsonProperty("nomTitulaire") String nomTitulaire,
							@JsonProperty("adresseContact") String adresseContact,
							@JsonProperty("pointsFidelite") int pointsFidelite) {
		super(new ObjectId(), TypeTitulaireDeCompte.PERSONNE_PHYSIQUE, codeTitulaire, nomTitulaire, adresseContact);
		this.pointsFidelite = pointsFidelite;
	}

	@JsonProperty("pointsFidelite")
	public int getPointsFidelité() {
		return pointsFidelite;
	}

	public void setPointsFidelite(int pointsFidelite) {
		this.pointsFidelite = pointsFidelite;
	}
	
}
