package br.com.BarberShopFreeStyle.services;

import br.com.BarberShopFreeStyle.models.ConfirmEmail;

/**
 * <p>
 * </p>
 *
 * @author Kevyn
 * @version 1.0 Created on 15 de mai de 2023
 */
public interface ConfirmEmailService
	extends AbstractService<ConfirmEmail, Long>
{

	String generateCodeVerificationEmail();

	ConfirmEmail save( String email, String code );
}
