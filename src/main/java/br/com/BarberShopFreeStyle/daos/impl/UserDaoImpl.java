package br.com.BarberShopFreeStyle.daos.impl;

import br.com.BarberShopFreeStyle.daos.UserDao;
import br.com.BarberShopFreeStyle.models.Usuario;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl
	extends AbstractDaoImpl<Usuario>
	implements UserDao
{

	@Override
	public List<Usuario> all()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete( final Usuario entity )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById( final Long id )
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected Class< ? extends Usuario> getBeanClass()
	{
		// TODO Auto-generated method stub
		return Usuario.class;
	}

	@Override
	public Usuario getById( final Long id )
	{
		// TODO Auto-generated method stub
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();

		final CriteriaQuery<Usuario> q = cb.createQuery( Usuario.class );

		final Root<Usuario> c = q.from( Usuario.class );

		q.where( cb.equal( c.get( "id" ), id ) );

		q.select( c );

		final List<Usuario> user = this.em.createQuery( q ).getResultList();

		if ( user.isEmpty() )
		{
			return null;
		}

		return user.get( 0 );
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @param login
	 * @return
	 * @see br.com.BarberShopFreeStyle.daos.UserDao#getByLogin(java.lang.String)
	 */
	@Override
	public Usuario getByLogin( final String login )
	{
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();

		final CriteriaQuery<Usuario> q = cb.createQuery( Usuario.class );

		final Root<Usuario> c = q.from( Usuario.class );

		q.where( cb.and( cb.equal( c.get( "login" ), login ), cb.isNull( c.get( "dataExclusao" ) ) ) );

		q.select( c );

		final List<Usuario> users = this.em.createQuery( q ).getResultList();

		if ( users.isEmpty() )
		{
			return null;
		}

		final Usuario user = users.get( 0 );

		return user;
	}

	@Override
	public Usuario getByLoginAndPassword( final String login, final String password )
	{
		// TODO Auto-generated method stub
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();

		final CriteriaQuery<Usuario> q = cb.createQuery( Usuario.class );

		final Root<Usuario> c = q.from( Usuario.class );

		q
			.where(
				cb
					.and(
						cb.equal( c.get( "login" ), login ),
						cb.equal( c.get( "password" ), password ),
						cb.isNull( c.get( "dataExclusao" ) ) ) );

		q.select( c );

		final List<Usuario> users = this.em.createQuery( q ).getResultList();

		if ( users.isEmpty() )
		{
			return null;
		}

		final Usuario user = users.get( 0 );

		return user;
	}

	@Override
	public Usuario insert( final Usuario entity )
	{
		// TODO Auto-generated method stub
		return save( entity );
	}

	@Override
	public Page<Usuario> listEmployees( final Specification specs, final Integer pageNumber, final Integer pageSize )
	{
		// TODO Auto-generated method stub
		return listPages( specs, pageNumber, pageSize, Usuario.class );
	}

	@Override
	public Usuario update( final Usuario entity )
	{
		// TODO Auto-generated method stub
		return updateObject( entity );
	}

	@Autowired
	private EntityManager em;

}
