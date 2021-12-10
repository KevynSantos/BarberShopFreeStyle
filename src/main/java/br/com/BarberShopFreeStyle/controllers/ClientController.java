package br.com.BarberShopFreeStyle.controllers;

import br.com.BarberShopFreeStyle.dtos.ClienteDto;
import br.com.BarberShopFreeStyle.dtos.StatusCrudDto;
import br.com.BarberShopFreeStyle.models.Cliente;
import br.com.BarberShopFreeStyle.services.ClientService;
import br.com.BarberShopFreeStyle.utils.Conversion;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping( "/client" )
public class ClientController
{

	@PostMapping( "/add" )
	@ResponseBody
	@Transactional
	public String add(
		@RequestParam( "text_new_name" ) final String nome,
		@RequestParam( "text_new_sobrenome" ) final String sobrenome,
		@RequestParam( "text_new_cpf" ) final String cpf,
		@RequestParam( "text_new_data_nascimento" ) final String dataNascimento,
		@RequestParam( "text_new_email" ) final String email,
		@RequestParam( "text_new_telefone" ) final String telefone,
		@RequestParam( "text_new_endereco" ) final String endereco )
		throws JsonProcessingException
	{
		final HashMap<String, Object> statusServer = new HashMap<String, Object>();

		try
		{
			final List<StatusCrudDto> status = this.clientService
				.add( nome, sobrenome, cpf, dataNascimento, endereco, telefone, email );
			statusServer.put( "status", status );
		}

		catch ( final Exception e )
		{
			statusServer.put( "status", "ERRO:" + e );
		}

		return Conversion.convertToJson( statusServer );

	}

	@PostMapping( "/delete" )
	@ResponseBody
	@Transactional
	public ModelAndView delete( @RequestParam( value = "idClientErase" ) final Long idCliente )
	{
		this.clientService.preparedDeleteClient( idCliente );

		return new ModelAndView( "redirect:/clients?deleteSuccess" );
	}

	@GetMapping( "/getById" )
	@ResponseBody
	public String getById( @RequestParam( value = "idClient" ) final Long idCliente )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();

		final Cliente cliente = this.clientService.getById( idCliente );

		result.put( "cliente", cliente );

		return Conversion.convertToJson( result );

	}

	@GetMapping( "/getClientByCPF" )
	@ResponseBody
	public String getClientByCPF( @RequestParam( "cpf" ) final String cpf )
		throws JsonProcessingException
	{
		final Cliente client = this.clientService.getByCPF( cpf );

		final HashMap<String, Object> result = new HashMap<String, Object>();

		result.put( "client", client );

		return Conversion.convertToJson( result );

	}
	
	@GetMapping("/getClientLikeCpf")
	@ResponseBody
	public String getClientLikeCpf(@RequestParam( "cpf" ) final String cpf) throws JsonProcessingException
	{
		final ClienteDto client = this.clientService.getClientLikeCpf( cpf );

		final HashMap<String, Object> result = new HashMap<String, Object>();

		result.put( "client", client );

		return Conversion.convertToJson( result );
	}
	
	@GetMapping("/getClientLikeNome")
	@ResponseBody
	public String getClientLikeNome(@RequestParam( "nome" ) final String nome) throws JsonProcessingException
	{
		final ClienteDto client = this.clientService.getClientLikeNome( nome );

		final HashMap<String, Object> result = new HashMap<String, Object>();

		result.put( "client", client );

		return Conversion.convertToJson( result );
	}

	@GetMapping( "/{outType:.+}" )
	public void getClients(
		@RequestParam( value = "pageNumber", required = false ) final Integer pageNumber,
		@PathVariable( value = "outType" ) final String outType,
		@RequestParam( value = "pageSize", required = false ) final Integer pageSize,
		@RequestParam( value = "text_nome", required = false ) final String nome,
		@RequestParam( value = "text_cpf", required = false ) final String cpf,
		@RequestParam( value = "text_data_inicial", required = false ) final String dataInicial,
		@RequestParam( value = "text_data_final", required = false ) final String dataFinal,
		@RequestParam( value = "text_email", required = false ) final String email,
		@RequestParam( value = "text_telefone", required = false ) final String telefone,
		@RequestParam( value = "text_endereco", required = false ) final String endereco,
		final HttpServletResponse response, Locale locale )
		throws Exception
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();

		final Page<Cliente> page = this.clientService
			.getResultPage( pageNumber, pageSize, nome, cpf, dataInicial, dataFinal, email, telefone, endereco );

		final ServletOutputStream outputStream = response.getOutputStream();

		if ( outType.indexOf( "json" ) != -1 )
		{
			result.put( "clientes", page.getContent() );
			result.put( "totalElements", page.getTotalElements() );
			new ObjectMapper().writeValue( outputStream, result );
			outputStream.flush();
			outputStream.close();
		}

		if ( outType.indexOf( ".xls" ) != -1 )
		{

			this.clientService.downloadExcel( page.getContent(), outputStream );
		}

		if ( outType.indexOf( ".pdf" ) != -1 )
		{
			this.clientService.downloadPdf( page.getContent(), outputStream, locale );

		}

		response.setHeader( "Content-Type", "application/octet-stream" );

	}

	@PutMapping( "/update" )
	@ResponseBody
	public String update(
		@RequestParam( value = "nome" ) final String nome,
		@RequestParam( value = "cpf" ) final String cpf,
		@RequestParam( value = "dataNasc" ) final String dataNasc,
		@RequestParam( value = "email" ) final String email,
		@RequestParam( value = "telefone" ) final String telefone,
		@RequestParam( value = "endereco" ) final String endereco,
		@RequestParam( value = "idClient" ) final Long idClient,
		@RequestParam( value = "sobrenome" ) final String sobrenome )
		throws ParseException,
			JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();

		try
		{
			final List<StatusCrudDto> status = this.clientService
				.update( nome, cpf, dataNasc, email, telefone, endereco, idClient, sobrenome );
			result.put( "status", status );
		}
		catch ( final Exception e )
		{
			result.put( "status", "ERRO:" + e );
		}

		return Conversion.convertToJson( result );
	}

	@Autowired
	private ClientService clientService;

}
