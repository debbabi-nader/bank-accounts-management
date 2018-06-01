package com.isamm.projet.gestion_comptes_bancaires.entities;

import java.util.Collection;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isamm.projet.gestion_comptes_bancaires.enumeration.TypeBanque;


@JsonIgnoreProperties(ignoreUnknown = true)
public class BanqueA extends Banque {

	private static final long serialVersionUID = 1L;

	public BanqueA() {
		super();
	}

	@JsonCreator
	public BanqueA(@JsonProperty("codeBanque") int codeBanque,
				   @JsonProperty("titulaires") Collection<ObjectId> titulaires,
				   @JsonProperty("comptes") Collection<ObjectId> comptes) {
		super(new ObjectId(), TypeBanque.BANQUE_A, codeBanque, titulaires, comptes);
	}
	
}
