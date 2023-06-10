package br.com.BarberShopFreeStyle.api;

import br.com.BarberShopFreeStyle.controllers.AbstractController;
import br.com.BarberShopFreeStyle.models.Cliente;
import br.com.BarberShopFreeStyle.models.Usuario;
import br.com.BarberShopFreeStyle.services.ClientService;
import br.com.BarberShopFreeStyle.services.UserService;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * </p>
 *
 * @author Kevyn
 * @version 1.0 Created on 10 de jun de 2023
 */
@RestController
@RequestMapping( "/api/client" )
public class ClientApp
	extends AbstractController
{

	@GetMapping( "/list" )
	public void getClients(
		@RequestParam( value = "idUser", required = true ) final Long idUser,
		@RequestParam( value = "pageNumber", required = false ) final Integer pageNumber,
		@RequestParam( value = "pageSize", required = false ) final Integer pageSize,
		@RequestParam( value = "text_nome", required = false ) final String nome,
		@RequestParam( value = "text_cpf", required = false ) final String cpf,
		@RequestParam( value = "text_data_inicial", required = false ) final String dataInicial,
		@RequestParam( value = "text_data_final", required = false ) final String dataFinal,
		@RequestParam( value = "text_email", required = false ) final String email,
		@RequestParam( value = "text_telefone", required = false ) final String telefone,
		@RequestParam( value = "text_endereco", required = false ) final String endereco,
		final HttpServletResponse response )
		throws JsonGenerationException,
			JsonMappingException,
			IOException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();
		final ServletOutputStream outputStream = response.getOutputStream();
		try
		{
			final Usuario user = this.userService.getById( idUser );

			throwNoUserLogged( user );

			final Page<Cliente> page = this.clientService
				.getResultPage( pageNumber, pageSize, nome, cpf, dataInicial, dataFinal, email, telefone, endereco );

			result.put( "clientes", page.getContent() );
			result.put( "totalElements", page.getTotalElements() );
			result.put( "code", "SUCCESS" );
		}
		catch ( final Exception e )
		{
			result.put( "code", "ERROR" );
			result.put( "message", e.getMessage() );
		}
		new ObjectMapper().writeValue( outputStream, result );
		outputStream.flush();
		outputStream.close();

		response.setHeader( "Content-Type", "application/octet-stream" );
	}

	@Autowired
	private ClientService clientService;

	@Autowired
	private UserService userService;
}
