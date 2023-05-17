package br.com.BarberShopFreeStyle.api;

import br.com.BarberShopFreeStyle.api.messages.MessagesApi;
import br.com.BarberShopFreeStyle.dtos.app.RegisterDto;
import br.com.BarberShopFreeStyle.models.ConfirmEmail;
import br.com.BarberShopFreeStyle.services.ConfirmEmailService;
import br.com.BarberShopFreeStyle.services.RegisterServiceApp;
import br.com.BarberShopFreeStyle.services.SimpleEmailService;
import br.com.BarberShopFreeStyle.utils.Conversion;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * </p>
 *
 * @author Kevyn
 * @version 1.0 Created on 15 de mai de 2023
 */
@RestController
@RequestMapping( "/api/register" )
public class RegisterApp
{

	@PostMapping( value = "/confirmCodeEmail", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	public String confirmCodeEmail( @RequestBody final MultiValueMap<String, Object> info )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<>();

		final RegisterDto dto = getDtoRegister( info );

		final Pair<Boolean, HashMap<String, Object>> pair = this.registerServiceApp
			.validFieldsForConfirmCodeVerification( dto );

		if ( !pair.getFirst() )
		{
			return Conversion.convertToJson( pair.getSecond() );
		}

		final ConfirmEmail object = this.confirmEmailService
			.getByEmailAndCode( dto.getEmail(), dto.getConfirmCodeEmail() );

		if ( Objects.isNull( object ) )
		{
			result.put( MessagesApi.PARAM_CODE.getMessage(), MessagesApi.INVALID_CODE.toString() );
			result.put( MessagesApi.PARAM_MESSAGE.getMessage(), MessagesApi.INVALID_CODE.getMessage() );
			return Conversion.convertToJson( result );
		}

		result.put( MessagesApi.PARAM_CODE.getMessage(), MessagesApi.SUCCESS.toString() );

		return Conversion.convertToJson( result );
	}

	private RegisterDto getDtoRegister( final MultiValueMap<String, Object> info )
	{
		final Map<String, Object> map = info.toSingleValueMap();
		final ObjectMapper mapper = new ObjectMapper();
		final RegisterDto dto = mapper.convertValue( map, RegisterDto.class );

		return dto;
	}

	@PostMapping( value = "/user", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	public String registerUser( @RequestBody final MultiValueMap<String, Object> info )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<>();

		final RegisterDto dto = getDtoRegister( info );

		final Pair<Boolean, HashMap<String, Object>> pair = this.registerServiceApp.validFieldsForRegisterUser( dto );

		if ( !pair.getFirst() )
		{
			return Conversion.convertToJson( pair.getSecond() );
		}

		final Pair<Boolean, HashMap<String, Object>> user = this.registerServiceApp.createClient( dto );

		if ( !user.getFirst() )
		{
			return Conversion.convertToJson( user.getSecond() );
		}

		result.put( MessagesApi.PARAM_CODE.getMessage(), MessagesApi.SUCCESS.toString() );

		return Conversion.convertToJson( result );
	}

	@PostMapping( value = "/sendCodeVerificationEmail", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	public String sendCodeVerificationEmail( @RequestBody final MultiValueMap<String, Object> info )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<>();

		final RegisterDto dto = getDtoRegister( info );

		final Pair<Boolean, HashMap<String, Object>> pair = this.registerServiceApp.validFieldsForSendEmail( dto );

		if ( !pair.getFirst() )
		{
			return Conversion.convertToJson( pair.getSecond() );
		}

		try
		{
			final String email = dto.getEmail();

			final String code = this.confirmEmailService.generateCodeVerificationEmail();

			final String[] contacts = new String[1];

			contacts[0] = email;

			final String subject = "Olá " + dto.getName() + ", confirme seu código por e-mail. [BarberShopFreeStyle]";

			final String body = String.format( "<body>Seu código de verificação é %s</body>", code );

			this.simpleEmailService.sendEmail( body, subject, contacts );

			this.confirmEmailService.save( email, code );

			result.put( MessagesApi.PARAM_CODE.getMessage(), MessagesApi.SUCCESS.toString() );
		}
		catch ( UnsupportedEncodingException | MessagingException e )
		{
			result.put( MessagesApi.PARAM_CODE.getMessage(), MessagesApi.EMAIL_NOT_SEND.toString() );
			result
				.put(
					MessagesApi.PARAM_MESSAGE.getMessage(),
					String.format( MessagesApi.EMAIL_NOT_SEND.getMessage(), e.getMessage() ) );
			return Conversion.convertToJson( result );
		}

		return Conversion.convertToJson( result );

	}

	@Autowired
	private ConfirmEmailService confirmEmailService;

	@Autowired
	private RegisterServiceApp registerServiceApp;

	@Autowired
	private SimpleEmailService simpleEmailService;
}
