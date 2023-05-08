package br.com.BarberShopFreeStyle.daos.impl;

import br.com.BarberShopFreeStyle.daos.TipoPerfilDao;
import br.com.BarberShopFreeStyle.enums.TypeProfile;
import br.com.BarberShopFreeStyle.models.TipoPerfil;

import java.util.List;

import javax.persistence.EntityManager;
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
public class TipoPerfilDaoImpl
	extends AbstractDaoImpl<TipoPerfil>
	implements TipoPerfilDao
{

	/**
	 * <p>
	 * </p>
	 *
	 * @return
	 * @see br.com.BarberShopFreeStyle.daos.AbstractDao#all()
	 */
	@Override
	public List<TipoPerfil> all()
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
	public void delete( final TipoPerfil entity )
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
	protected Class< ? extends TipoPerfil> getBeanClass()
	{
		return TipoPerfil.class;
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
	public TipoPerfil getById( final Long id )
	{
		return null;
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param type
	 * @return
	 * @see br.com.BarberShopFreeStyle.daos.TipoPerfilDao#getByType(br.com.BarberShopFreeStyle.enums.TypeProfile)
	 */

	@Override
	public TipoPerfil getByType( final TypeProfile type )
	{
		final CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();

		final CriteriaQuery<TipoPerfil> q = cb.createQuery( TipoPerfil.class );

		final Root<TipoPerfil> c = q.from( TipoPerfil.class );

		q.where( cb.equal( c.get( "tipo" ), type ) );

		q.select( c );

		final List<TipoPerfil> requests = this.entityManager.createQuery( q ).getResultList();

		if ( requests.isEmpty() )
		{
			return null;
		}

		return requests.get( 0 );
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
	public TipoPerfil insert( final TipoPerfil entity )
	{
		return null;
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
	public TipoPerfil update( final TipoPerfil entity )
	{
		return null;
	}

	@Autowired
	private EntityManager entityManager;

}
