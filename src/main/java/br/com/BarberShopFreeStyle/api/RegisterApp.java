package br.com.BarberShopFreeStyle.api;

import br.com.BarberShopFreeStyle.api.messages.MessagesApi;
import br.com.BarberShopFreeStyle.dtos.app.RegisterDto;
import br.com.BarberShopFreeStyle.services.ConfirmEmailService;
import br.com.BarberShopFreeStyle.services.RegisterServiceApp;
import br.com.BarberShopFreeStyle.services.SimpleEmailService;
import br.com.BarberShopFreeStyle.utils.Conversion;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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

	@PostMapping( value = "/sendCodeVerificationEmail", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	public String sendCodeVerificationEmail( @RequestBody final MultiValueMap<String, String> info )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<>();
		final Map<String, String> map = info.toSingleValueMap();
		final ObjectMapper mapper = new ObjectMapper();
		final RegisterDto dto = mapper.convertValue( map, RegisterDto.class );
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
