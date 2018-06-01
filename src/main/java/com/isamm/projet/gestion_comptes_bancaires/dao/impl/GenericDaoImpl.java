package com.isamm.projet.gestion_comptes_bancaires.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import com.isamm.projet.gestion_comptes_bancaires.dao.GenericDao;
import com.mongodb.WriteResult;

/**
 * 
 * An implementation of the generic DAO interface
 * Classes that implement specific DAO interfaces must extend this abstract class and specify its type
 *
 * @param <T>
 */

public abstract class GenericDaoImpl<T> implements GenericDao<T> {
	
	private MongoCollection collection;
	private Class<T> clazz;
	
	public GenericDaoImpl(MongoCollection collection, Class<T> clazz) {
		this.collection = collection;
		this.clazz = clazz;
	}
	
	public long count() {
		return collection.count();
	}
	
	public List<T> findAll() {
		MongoCursor<T> cursor = collection.find().as(clazz);
		List<T> list = new ArrayList<T>();
		try {
			while(cursor.hasNext()) {
				list.add(cursor.next());
			}
		} finally {
			try {
				cursor.close();
			} catch (IOException e) {}
		}
		return list;
	}
	
	public T findById(ObjectId id) {
		return collection.findOne(id).as(clazz);
	}
	
	public List<T> findByIdRange(List<ObjectId> ids) {
		MongoCursor<T> cursor = collection.find("{_id: {$in: #}}", ids).as(clazz);
		List<T> list = new ArrayList<T>();
		try {
			while(cursor.hasNext()) {
				list.add(cursor.next());
			}
		} finally {
			try {
				cursor.close();
			} catch (IOException e) {}
		}
		return list;
	}
	
	public T save(T t) {
		WriteResult writeResult = collection.insert(t);
		if(writeResult.wasAcknowledged()) {
			return t;
		} else {
			return null;
		}
	}
	
	public T update(ObjectId id, String updateString) {
		collection.update(id).with(updateString);
		return findById(id);
	}
	
	public boolean delete(ObjectId id) {
		WriteResult writeResult = collection.remove(id);
		return writeResult.wasAcknowledged();
	}
	
}
