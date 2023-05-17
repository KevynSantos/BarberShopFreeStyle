package br.com.BarberShopFreeStyle.services.impl;

import br.com.BarberShopFreeStyle.api.messages.MessagesApi;
import br.com.BarberShopFreeStyle.dtos.StatusCrudDto;
import br.com.BarberShopFreeStyle.dtos.app.RegisterDto;
import br.com.BarberShopFreeStyle.services.RegisterServiceApp;
import br.com.BarberShopFreeStyle.services.UserService;
import br.com.BarberShopFreeStyle.utils.Validation;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Override
	public Pair<Boolean, HashMap<String, Object>> createClient( final RegisterDto dto )
	{
		final HashMap<String, Object> result = new HashMap<>();
		boolean sucess = true;
		try
		{
			final List<StatusCrudDto> status = this.userService
				.addClientInApp(
					dto.getName(),
					dto.getCpf(),
					dto.getDateNasc(),
					dto.getEmail(),
					dto.getPhone(),
					dto.getAddress(),
					dto.getPassword() );

			final boolean registred = status.stream().filter( s -> s.getMessage().equals( "SUCCESS" ) ).count() > 0;

			if ( !registred )
			{
				final String message = getMessageErrorByStatus( status );
				result.put( MessagesApi.PARAM_CODE.getMessage(), MessagesApi.INSPECT_ERROR.toString() );
				result
					.put(
						MessagesApi.PARAM_MESSAGE.getMessage(),
						String.format( MessagesApi.INSPECT_ERROR.getMessage(), message ) );
				sucess = false;
			}
		}
		catch ( final Exception e )
		{
			result.put( MessagesApi.PARAM_CODE.getMessage(), MessagesApi.UNKNOW_ERROR.toString() );
			result
				.put(
					MessagesApi.PARAM_MESSAGE.getMessage(),
					String.format( MessagesApi.UNKNOW_ERROR.getMessage(), e.getMessage() ) );
			sucess = false;
		}

		return Pair.of( sucess, result );
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param dto
	 * @return
	 * @see br.com.BarberShopFreeStyle.services.RegisterServiceApp#createClient(br.com.BarberShopFreeStyle.dtos.app.RegisterDto)
	 */
	private String getMessageErrorByStatus( final List<StatusCrudDto> status )
	{
		String message = "";

		for ( final StatusCrudDto s : status )
		{
			message += s.getMessage();
		}

		return message;
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param dto
	 * @return
	 * @see br.com.BarberShopFreeStyle.services.RegisterServiceApp#validFieldsForRegisterUser(br.com.BarberShopFreeStyle.dtos.app.RegisterDto)
	 */

	private Pair<Boolean, HashMap<String, Object>> requiredField(
		final HashMap<String, Object> result,
		final String field,
		final String fieldMessage )
	{
		if ( Strings.isBlank( field ) )
		{
			result.put( MessagesApi.PARAM_CODE.getMessage(), MessagesApi.REQUIRED_FIELD.toString() );
			result
				.put(
					MessagesApi.PARAM_MESSAGE.getMessage(),
					String.format( MessagesApi.REQUIRED_FIELD.getMessage(), fieldMessage ) );
			return Pair.of( false, result );
		}

		return Pair.of( true, result );
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param dto
	 * @return
	 * @see br.com.BarberShopFreeStyle.services.RegisterServiceApp#validFieldsForConfirmCodeVerification(br.com.BarberShopFreeStyle.dtos.app.RegisterDto)
	 */
	@Override
	public Pair<Boolean, HashMap<String, Object>> validFieldsForConfirmCodeVerification( final RegisterDto dto )
	{
		final HashMap<String, Object> result = new HashMap<>();

		final String email = dto.getEmail();

		if ( ( email == null ) || Strings.isBlank( email ) )
		{
			result.put( MessagesApi.PARAM_CODE.getMessage(), MessagesApi.EMAIL_IS_EMPTY.toString() );
			result.put( MessagesApi.PARAM_MESSAGE.getMessage(), MessagesApi.EMAIL_IS_EMPTY.getMessage() );
			return Pair.of( false, result );
		}

		final String code = dto.getConfirmCodeEmail();

		if ( ( code == null ) || Strings.isBlank( code ) )
		{
			result.put( MessagesApi.PARAM_CODE.getMessage(), MessagesApi.INVALID_CODE.toString() );
			result.put( MessagesApi.PARAM_MESSAGE.getMessage(), MessagesApi.INVALID_CODE.getMessage() );
			return Pair.of( false, result );
		}

		return Pair.of( true, result );
	}

	@Override
	public Pair<Boolean, HashMap<String, Object>> validFieldsForRegisterUser( final RegisterDto dto )
	{
		final HashMap<String, Object> result = new HashMap<>();
		final Pair<Boolean, HashMap<String, Object>> validName = requiredField( result, dto.getName(), "Nome" );
		final Pair<Boolean, HashMap<String, Object>> validCpf = requiredField( result, dto.getCpf(), "CPF" );
		final Pair<Boolean, HashMap<String, Object>> validDateNasc = requiredField(
			result,
			dto.getDateNasc(),
			"Data de Nascimento" );
		final Pair<Boolean, HashMap<String, Object>> validAdress = requiredField(
			result,
			dto.getAddress(),
			"Endereço" );

		final Pair<Boolean, HashMap<String, Object>> validEmail = requiredField( result, dto.getEmail(), "E-mail" );

		final Pair<Boolean, HashMap<String, Object>> validPhone = requiredField( result, dto.getPhone(), "Telefone" );

		final Pair<Boolean, HashMap<String, Object>> validPassword = requiredField(
			result,
			dto.getPassword(),
			"Senha" );

		if ( !validName.getFirst() )
		{
			return validName;
		}

		if ( !validCpf.getFirst() )
		{
			return validCpf;
		}

		if ( !validDateNasc.getFirst() )
		{
			return validDateNasc;
		}

		if ( !validAdress.getFirst() )
		{
			return validAdress;
		}

		if ( !validPhone.getFirst() )
		{
			return validPhone;
		}

		if ( !validEmail.getFirst() )
		{
			return validEmail;
		}

		if ( !validPassword.getFirst() )
		{
			return validPassword;
		}

		result.put( MessagesApi.PARAM_CODE.getMessage(), MessagesApi.SUCCESS.toString() );

		return Pair.of( true, result );
	}

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

	@Autowired
	private UserService userService;

}
