package br.com.BarberShopFreeStyle.daos.impl;

import br.com.BarberShopFreeStyle.daos.ConfirmEmailDao;
import br.com.BarberShopFreeStyle.models.ConfirmEmail;

import java.util.List;

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

}
