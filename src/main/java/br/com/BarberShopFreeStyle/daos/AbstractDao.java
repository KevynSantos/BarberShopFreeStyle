package br.com.BarberShopFreeStyle.daos;

import java.util.List;

public interface AbstractDao<T, K> {
	
	List<T> all();
	
	T getById(K id);
	
	T insert(T entity);
	
	T update(T entity);
	
	void delete(T entity);
	
	void deleteById(K id);

}
