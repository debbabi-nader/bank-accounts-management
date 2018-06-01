package com.isamm.projet.gestion_comptes_bancaires.dao;

import java.util.List;

import org.bson.types.ObjectId;

/**
 * 
 * Generic DAO interface contains common DAL methods implemented in GenericDaoImpl
 * Specific DAO interfaces must extend this interface and specify its type
 *
 * @param <T>
 */

public interface GenericDao<T> {
	
	public long count();
	
	public List<T> findAll();
	
	public T findById(ObjectId id);
	
	public List<T> findByIdRange(List<ObjectId> ids);
	
	public T save(T t);
	
	public T update(ObjectId id, String updateString);
	
	public boolean delete(ObjectId id);
	
}
