package br.com.BarberShopFreeStyle.services;

import java.util.List;

public interface AbstractService<T, K> {
	
	List<T> all();
	
	T getById(K id);
	
	T insert(T entity);
	
	T update(T entity);
	
	void delete(T entity);
	
	void deleteById(K id);

}
