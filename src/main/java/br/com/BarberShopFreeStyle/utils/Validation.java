package br.com.BarberShopFreeStyle.utils;

import br.com.caelum.stella.validation.CPFValidator;

import java.util.regex.Pattern;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * <p>
 * </p>
 *
 * @author kevyn.santos
 * @version 1.0 Created on Sep 23, 2021
 */
public class Validation
{

	public static boolean validateCpf( final String cpf, final boolean isFormated )
	{
		final CPFValidator cpfValidator = new CPFValidator( isFormated );

		try
		{
			cpfValidator.assertValid( cpf );
			return true;
		}

		catch ( final Exception e )
		{
			return false;
		}
	}

	public static boolean validateEmail( final String email )
	{
		final EmailValidator validator = EmailValidator.getInstance();

		return validator.isValid( email );
	}

	public static boolean validateTelefone( final String telefone )
	{
		return Pattern.matches( "\\(\\d{2,}\\) \\d{5}\\-\\d{4}", telefone )
			|| Pattern.matches( "\\(\\d{2,}\\) \\d{4}\\-\\d{4}", telefone );
	}

}
