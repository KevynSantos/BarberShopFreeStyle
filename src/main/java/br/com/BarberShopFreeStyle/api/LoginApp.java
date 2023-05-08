package br.com.BarberShopFreeStyle.api;

import br.com.BarberShopFreeStyle.api.messages.MessagesApi;
import br.com.BarberShopFreeStyle.models.Usuario;
import br.com.BarberShopFreeStyle.services.UserService;
import br.com.BarberShopFreeStyle.utils.Conversion;

import java.util.HashMap;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * <p>
 * </p>
 *
 * @author Kevyn
 * @version 1.0 Created on 4 de mai de 2023
 */
@RestController
@RequestMapping( "/api/login" )
public class LoginApp
{

	@PostMapping( value = "/auth" )
	public String auth(
		@RequestParam( value = "login", required = false ) final String login,
		@RequestParam( value = "password", required = false ) final String password )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();

		if ( Strings.isBlank( login ) || Strings.isBlank( password ) )
		{
			result.put( "code", MessagesApi.LOGIN_FIELDS_EMPTY.toString() );
			result.put( "message", MessagesApi.LOGIN_FIELDS_EMPTY.getMessage() );
			return Conversion.convertToJson( result );
		}

		final Usuario user = this.userService.getByLoginAndPassword( login, password );

		if ( user == null )
		{
			result.put( "code", MessagesApi.LOGIN_INCORRECT.toString() );
			result.put( "message", MessagesApi.LOGIN_INCORRECT.getMessage() );
			return Conversion.convertToJson( result );
		}

		result.put( "code", MessagesApi.SUCCESS.toString() );
		result.put( "idUser", user.getId() );
		result.put( "profile", user.getTipoPerfil().getTipo().toString() );

		return Conversion.convertToJson( result );
	}

	@Autowired
	private UserService userService;

}
