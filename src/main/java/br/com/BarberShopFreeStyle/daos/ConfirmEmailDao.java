package br.com.BarberShopFreeStyle.daos;

import br.com.BarberShopFreeStyle.models.ConfirmEmail;

/**
 * <p>
 * </p>
 *
 * @author Kevyn
 * @version 1.0 Created on 15 de mai de 2023
 */
public interface ConfirmEmailDao
	extends AbstractDao<ConfirmEmail, Long>
{

	/**
	 * <p>
	 * </p>
	 * 
	 * @param email
	 * @param code
	 * @return
	 */
	ConfirmEmail getByEmailAndCode( String email, String code );

}
