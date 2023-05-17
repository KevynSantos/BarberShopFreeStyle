package br.com.BarberShopFreeStyle.services.impl;

import br.com.BarberShopFreeStyle.daos.ConfirmEmailDao;
import br.com.BarberShopFreeStyle.models.ConfirmEmail;
import br.com.BarberShopFreeStyle.services.ConfirmEmailService;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * </p>
 *
 * @author Kevyn
 * @version 1.0 Created on 15 de mai de 2023
 */
@Service
public class ConfirmEmailServiceImpl
	extends AbstractService
	implements ConfirmEmailService
{

	/**
	 * <p>
	 * </p>
	 *
	 * @return
	 * @see br.com.BarberShopFreeStyle.services.AbstractService#all()
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
	 * @see br.com.BarberShopFreeStyle.services.AbstractService#delete(java.lang.Object)
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
	 * @see br.com.BarberShopFreeStyle.services.AbstractService#deleteById(java.lang.Object)
	 */
	@Override
	public void deleteById( final Long id )
	{
	}

	@Override
	public String generateCodeVerificationEmail()
	{
		final String letras = "ABCDEFGHIJKLMNOPQRSTUVXYZ";
		final Random gerador = new Random();
		final char caracter = letras.charAt( gerador.nextInt( letras.length() ) );
		final Integer numbers = gerador.nextInt( 99999 );

		final String code = String.valueOf( caracter ) + numbers.toString();

		return code;
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @param email
	 * @param code
	 * @return
	 * @see br.com.BarberShopFreeStyle.services.ConfirmEmailService#getByEmailAndCode(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public ConfirmEmail getByEmailAndCode( final String email, final String code )
	{
		return this.confirmEmailDao.getByEmailAndCode( email, code );
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param id
	 * @return
	 * @see br.com.BarberShopFreeStyle.services.AbstractService#getById(java.lang.Object)
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
	 * @see br.com.BarberShopFreeStyle.services.AbstractService#insert(java.lang.Object)
	 */
	@Override
	public ConfirmEmail insert( final ConfirmEmail entity )
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
	 * @see br.com.BarberShopFreeStyle.services.ConfirmEmailService#save(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public ConfirmEmail save( final String email, final String code )
	{
		final ConfirmEmail object = new ConfirmEmail();

		object.setEmail( email );
		object.setDate( new Date() );
		object.setCode( code );

		final ConfirmEmail objectSave = this.confirmEmailDao.insert( object );

		return objectSave;
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param entity
	 * @return
	 * @see br.com.BarberShopFreeStyle.services.AbstractService#update(java.lang.Object)
	 */
	@Override
	public ConfirmEmail update( final ConfirmEmail entity )
	{
		return null;
	}

	@Autowired
	private ConfirmEmailDao confirmEmailDao;

}
