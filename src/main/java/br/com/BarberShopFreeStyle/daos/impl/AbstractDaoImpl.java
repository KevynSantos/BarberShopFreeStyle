package br.com.BarberShopFreeStyle.daos.impl;

import java.sql.PreparedStatement;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.ObjectUtils;

import br.com.BarberShopFreeStyle.dtos.IntervalHoursScheduling;
import br.com.BarberShopFreeStyle.models.Cliente;
import br.com.BarberShopFreeStyle.models.Servico;



public abstract class AbstractDaoImpl<T> {

	

	
	@Autowired
	private EntityManagerFactory em;

	
	
	
	public T save( final T object )
	{
	
		EntityManager entityManager = em.createEntityManager();

		entityManager.getTransaction().begin();
		if (!ObjectUtils.isEmpty(object) && !entityManager.contains(object)) {
		   entityManager.persist(object);
		   entityManager.flush();
		}
		entityManager.getTransaction().commit();		
		entityManager.close();

	
		return object;
	}
	
	public T updateObject( final T object)
	{
		EntityManager entityManager = em.createEntityManager();
		entityManager.getTransaction().begin();
		if (!ObjectUtils.isEmpty(object) && !entityManager.contains(object)) {
			entityManager.merge(object);
			entityManager.flush();
		}
		entityManager.getTransaction().commit();
		entityManager.close();
		
		return object;
	}
	
	
	public void deleteObject( final T object )
	{
		
		
		EntityManager entityManager = em.createEntityManager();

		entityManager.getTransaction().begin();
		if (!ObjectUtils.isEmpty(object) && entityManager.contains(object)) {
		   entityManager.remove(object);
		   entityManager.flush();
		}
		entityManager.getTransaction().commit();		
		entityManager.close();
		

	}
	
	
	@SuppressWarnings("unchecked")
	public Page<T> listPages(@SuppressWarnings("rawtypes") Specification specs, Integer pageNumber, Integer pageSize, Class<T> typeParameterClass)
	{
		if(pageNumber == null)
		{
			pageNumber = 0;
		}
		
		else
		{
			pageNumber = pageNumber-1;
		}
		
		EntityManager entityManager = this.em.createEntityManager();
		
		
		final SimpleJpaRepository<T, Integer> simpleJpaRepository = new SimpleJpaRepository<T, Integer>(
				typeParameterClass,
				entityManager);

		
		Page<T> pages =  simpleJpaRepository
				.findAll(
					specs,
					PageRequest.of( pageNumber, pageSize) );
		
		entityManager.close();
		
		return pages;
	}
	
	
	
	
	public void saveListElements( final List<T> tList )
	{
		for ( final T t : tList )
		{
			save( t );
		}
	}
	
	protected abstract Class< ? extends T> getBeanClass();
}
