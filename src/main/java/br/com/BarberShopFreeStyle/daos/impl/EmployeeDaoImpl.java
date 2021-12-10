package br.com.BarberShopFreeStyle.daos.impl;

import br.com.BarberShopFreeStyle.daos.EmployeeDao;
import br.com.BarberShopFreeStyle.models.Funcionario;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * </p>
 *
 * @author kevyn.santos
 * @version 1.0 Created on Sep 13, 2021
 */
@Repository
public class EmployeeDaoImpl
	extends AbstractDaoImpl<Funcionario>
	implements EmployeeDao
{

	/**
	 * <p>
	 * </p>
	 *
	 * @return
	 * @see br.com.BarberShopFreeStyle.daos.AbstractDao#all()
	 */
	@Override
	public List<Funcionario> all()
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
	public void delete( final Funcionario entity )
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
	protected Class< ? extends Funcionario> getBeanClass()
	{
		return Funcionario.class;
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param cpf
	 * @return
	 * @see br.com.BarberShopFreeStyle.daos.EmployeeDao#getByCpf(java.lang.String)
	 */
	@Override
	public Funcionario getByCpf( final String cpf )
	{
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();

		final EntityManager entityManager = this.em.createEntityManager();

		final CriteriaQuery<Funcionario> q = cb.createQuery( Funcionario.class );
		final Root<Funcionario> c = q.from( Funcionario.class );
		q.where( cb.and( cb.equal( c.get( "cpf" ), cpf ) ) );
		q.select( c );

		final List<Funcionario> list = entityManager.createQuery( q ).getResultList();

		entityManager.close();

		if ( list.isEmpty() )
		{
			return null;
		}

		return list.get( 0 );

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
	public Funcionario getById( final Long id )
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
	public Funcionario insert( final Funcionario entity )
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
	public Funcionario update( final Funcionario entity )
	{
		return updateObject( entity );
	}

	@Autowired
	private EntityManagerFactory em;

}
