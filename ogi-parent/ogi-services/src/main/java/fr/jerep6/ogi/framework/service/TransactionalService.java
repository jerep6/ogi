package fr.jerep6.ogi.framework.service;

import java.io.Serializable;
import java.util.Collection;

import fr.jerep6.ogi.framework.exception.BusinessException;

public interface TransactionalService<T, PK extends Serializable> {
	T read(PK id);

	T save(T bo);

	void validate(T bo) throws BusinessException;

	/**
	 * Update the entity. The return will managed. It is a new instance of bo
	 * 
	 * @param bo
	 *            business object to managed
	 * @return
	 */
	T update(T bo);

	/**
	 * @return all entity
	 */
	Collection<T> listAll();
}
