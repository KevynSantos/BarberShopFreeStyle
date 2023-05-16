package br.com.BarberShopFreeStyle.services.impl;

import br.com.BarberShopFreeStyle.api.messages.MessagesApi;
import br.com.BarberShopFreeStyle.dtos.app.RegisterDto;
import br.com.BarberShopFreeStyle.services.RegisterServiceApp;
import br.com.BarberShopFreeStyle.utils.Validation;

import java.util.HashMap;

import org.apache.logging.log4j.util.Strings;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

/**
 * <p>
 * </p>
 *
 * @author Kevyn
 * @version 1.0 Created on 16 de mai de 2023
 */
@Service
public class RegisterServiceAppImpl
	extends AbstractService
	implements RegisterServiceApp
{

	/**
	 * <p>
	 * </p>
	 *
	 * @param dto
	 * @return
	 * @see br.com.BarberShopFreeStyle.services.RegisterServiceApp#validFieldsForSendEmail(br.com.BarberShopFreeStyle.dtos.app.RegisterDto)
	 */
	@Override
	public Pair<Boolean, HashMap<String, Object>> validFieldsForSendEmail( final RegisterDto dto )
	{
		final HashMap<String, Object> result = new HashMap<>();
		boolean isValid = true;
		final String email = dto.getEmail();
		if ( ( email == null ) || Strings.isBlank( email ) )
		{
			result.put( MessagesApi.PARAM_CODE.getMessage(), MessagesApi.EMAIL_IS_EMPTY.toString() );
			result.put( MessagesApi.PARAM_MESSAGE.getMessage(), MessagesApi.EMAIL_IS_EMPTY.getMessage() );
			isValid = false;
		}

		if ( !Validation.validateEmail( email ) )
		{
			result.put( MessagesApi.PARAM_CODE.getMessage(), MessagesApi.EMAIL_IS_NOT_VALID.toString() );
			result.put( MessagesApi.PARAM_MESSAGE.getMessage(), MessagesApi.EMAIL_IS_NOT_VALID.getMessage() );
			isValid = false;
		}

		if ( isValid )
		{
			result.put( MessagesApi.PARAM_CODE.getMessage(), MessagesApi.SUCCESS.toString() );
		}

		return Pair.of( isValid, result );
	}

}
