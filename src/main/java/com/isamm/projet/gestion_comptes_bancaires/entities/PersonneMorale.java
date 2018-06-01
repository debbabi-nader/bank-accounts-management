package com.isamm.projet.gestion_comptes_bancaires.entities;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeTitulaireDeCompte;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonneMorale extends TitulaireDeCompte {
	
	private static final long serialVersionUID = 1L;
	
	private String nomCommercial;

	public PersonneMorale() {
		super();
	}

	@JsonCreator
	public PersonneMorale(@JsonProperty("codeTitulaire") int codeTitulaire,
						  @JsonProperty("nomTitulaire") String nomTitulaire,
						  @JsonProperty("adresseContact") String adresseContact,
						  @JsonProperty("nomCommercial") String nomCommercial) {
		super(new ObjectId(), TypeTitulaireDeCompte.PERSONNE_MORALE, codeTitulaire, nomTitulaire, adresseContact);
		this.nomCommercial = nomCommercial;
	}

	@JsonProperty("nomCommercial")
	public String getNomCommercial() {
		return nomCommercial;
	}

	public void setNomCommercial(String nomCommercial) {
		this.nomCommercial = nomCommercial;
	}

}
