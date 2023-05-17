package br.com.BarberShopFreeStyle.daos.impl;

import br.com.BarberShopFreeStyle.daos.ConfirmEmailDao;
import br.com.BarberShopFreeStyle.models.ConfirmEmail;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * </p>
 *
 * @author Kevyn
 * @version 1.0 Created on 15 de mai de 2023
 */
@Repository
public class ConfirmEmailDaoImpl
	extends AbstractDaoImpl<ConfirmEmail>
	implements ConfirmEmailDao
{

	/**
	 * <p>
	 * </p>
	 *
	 * @return
	 * @see br.com.BarberShopFreeStyle.daos.AbstractDao#all()
	 */
	@Override
	public List<ConfirmEmail> all()
	{
		return null;
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param entity
	 * @see br.com.BarberShopFreeStyle.daos.AbstractDao#delete(java.lang.Object)
	 */
	@Override
	public void delete( final ConfirmEmail entity )
	{
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param id
	 * @see br.com.BarberShopFreeStyle.daos.AbstractDao#deleteById(java.lang.Object)
	 */
	@Override
	public void deleteById( final Long id )
	{
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @return
	 * @see br.com.BarberShopFreeStyle.daos.impl.AbstractDaoImpl#getBeanClass()
	 */
	@Override
	protected Class< ? extends ConfirmEmail> getBeanClass()
	{
		return null;
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param email
	 * @param code
	 * @return
	 * @see br.com.BarberShopFreeStyle.daos.ConfirmEmailDao#getByEmailAndCode(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public ConfirmEmail getByEmailAndCode( final String email, final String code )
	{
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<ConfirmEmail> q = cb.createQuery( ConfirmEmail.class );
		final Root<ConfirmEmail> c = q.from( ConfirmEmail.class );
		final Predicate conditions = cb.and( cb.equal( c.get( "code" ), code ), cb.equal( c.get( "email" ), email ) );
		final List<Order> orderList = new ArrayList();
		orderList.add( cb.desc( c.get( "date" ) ) );
		q.where( conditions );
		q.select( c );
		q.orderBy( orderList );

		ConfirmEmail result;

		final EntityManager entityManager = this.em.createEntityManager();

		try
		{

			result = entityManager.createQuery( q ).getSingleResult();
		}
		catch ( final NoResultException e )
		{
			result = null;
		}
		finally
		{
			entityManager.close();
		}

		return result;
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param id
	 * @return
	 * @see br.com.BarberShopFreeStyle.daos.AbstractDao#getById(java.lang.Object)
	 */
	@Override
	public ConfirmEmail getById( final Long id )
	{
		return null;
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param entity
	 * @return
	 * @see br.com.BarberShopFreeStyle.daos.AbstractDao#insert(java.lang.Object)
	 */
	@Override
	public ConfirmEmail insert( final ConfirmEmail entity )
	{
		return save( entity );
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param entity
	 * @return
	 * @see br.com.BarberShopFreeStyle.daos.AbstractDao#update(java.lang.Object)
	 */
	@Override
	public ConfirmEmail update( final ConfirmEmail entity )
	{
		return null;
	}

	@Autowired
	private EntityManagerFactory em;

}
