package br.com.BarberShopFreeStyle.controllers;

import br.com.BarberShopFreeStyle.daos.DescontoDao;
import br.com.BarberShopFreeStyle.dtos.StatusCrudDto;
import br.com.BarberShopFreeStyle.enums.StatusCrudEnum;
import br.com.BarberShopFreeStyle.enums.StatusService;
import br.com.BarberShopFreeStyle.models.Desconto;
import br.com.BarberShopFreeStyle.models.Servico;
import br.com.BarberShopFreeStyle.models.Usuario;
import br.com.BarberShopFreeStyle.services.Service;
import br.com.BarberShopFreeStyle.utils.Conversion;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
@RequestMapping( "/service" )
public class ServiceController
	extends AbstractController
{

	private static final Logger LOG = LogManager.getLogger( "ServiceController" );

	@PostMapping( "/add" )
	@ResponseBody
	@Transactional
	public String add(
		@RequestParam( "pedidos" ) final String[] pedidos,
		@RequestParam( "cpf" ) final String cpf,
		@RequestParam( "status" ) final String status,
		@RequestParam( value = "descricao", required = false ) final String descricao,
		final HttpSession session )
		throws JsonProcessingException
	{
		final HashMap<String, Object> statusServer = new HashMap<String, Object>();

		try
		{

			final Usuario user = getUserFromSession( session );

			this.servicoService.add( pedidos, cpf, status, descricao, user );

			statusServer.put( "status", "success" );
		}

		catch ( final Exception e )
		{
			LOG.error( "Ocorreu um erro inesperado: " + e );
			statusServer.put( "status", "error:" + e );
		}

		return Conversion.convertToJson( statusServer );

	}
	
	@PostMapping("/enableOrDisableDiscount")
	@ResponseBody
	public String enableOrDisableDiscount(@RequestParam("enableOrDisable") boolean enableOrDisable, final HttpSession session) throws JsonProcessingException
	{
		getUserFromSession( session );
		
		StatusCrudDto status = null;
		
		final HashMap<String, Object> result = new HashMap<String, Object>();
		
		try
		{
			this.descontoDao.activeOrActive(enableOrDisable);
			status = new StatusCrudDto( StatusCrudEnum.SUCCESS, null );
		}
		catch(Exception e)
		{
			status = new StatusCrudDto( StatusCrudEnum.ERROR, e.getLocalizedMessage() );
		}
		
		result.put("status", status);
		
		return Conversion.convertToJson( result );
	}
	
	@GetMapping("/getStatusOfDiscount")
	@ResponseBody
	public String getStatusOfDiscount(final HttpSession session) throws JsonProcessingException
	{
		getUserFromSession( session );
		final HashMap<String, Object> result = new HashMap<String, Object>();
		StatusCrudDto status = null;
		try
		{
			Desconto discount = this.descontoDao.getDiscount();
			result.put("enable", discount.isAtivo());
			result.put("value", discount.getValor());
			status = new StatusCrudDto( StatusCrudEnum.SUCCESS, null );
		}
		catch(Exception e)
		{
			status = new StatusCrudDto( StatusCrudEnum.ERROR, e.getLocalizedMessage() );
		}
		
		result.put("status", status);
		
		return Conversion.convertToJson( result );
		
		
	}
	
	@PostMapping("/updateValueDiscount")
	@ResponseBody
	public String updateValueDiscount(@RequestParam("newValue") String newValue, final HttpSession session) throws JsonProcessingException
	{
		getUserFromSession( session );
		
		StatusCrudDto status = null;
		
		final HashMap<String, Object> result = new HashMap<String, Object>();
		
		try
		{
			this.descontoDao.updateValueDiscount(newValue);
			status = new StatusCrudDto( StatusCrudEnum.SUCCESS, null );
			
		}
		catch(Exception e)
		{
			status = new StatusCrudDto( StatusCrudEnum.ERROR, e.getLocalizedMessage() );
		}
		
		result.put("status", status);
		
		return Conversion.convertToJson( result );
	}
	
	@Autowired
	private DescontoDao descontoDao;

	@PostMapping( "/delete" )
	@ResponseBody
	@Transactional
	public ModelAndView delete( @RequestParam( value = "idServiceErase" ) final Long idService )
	{

		this.servicoService.delete( idService );
		return new ModelAndView( "redirect:/services?deleteSuccess" );
	}

	@GetMapping( "/getById" )
	@ResponseBody
	public String getById( @RequestParam( value = "serviceId" ) final Long serviceId )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();

		final Servico servico = this.servicoService.getById( serviceId );

		result.put( "service", servico );

		return Conversion.convertToJson( result );
	}

	@GetMapping( "/{outType:.+}" )
	public void getServices(
		@RequestParam( value = "pageNumber", required = false ) final Integer pageNumber,
		@RequestParam( value = "pageSize", required = false ) final Integer pageSize,
		@PathVariable( value = "outType" ) final String outType,
		@RequestParam( value = "text_pedido", required = false ) final String pedido,
		@RequestParam( value = "text_cpf", required = false ) final String cpf,
		@RequestParam( value = "text_data_inicial", required = false ) final String dataInicial,
		@RequestParam( value = "text_data_final", required = false ) final String dataFinal,
		@RequestParam( value = "select_status", required = false ) final String status,
		@RequestParam( value = "text_cpf_employee", required = false ) final String cpfEmployee,
		@RequestParam( value = "select_speciality_employee", required = false ) final String speciality,
		final HttpServletResponse response,
		final HttpSession session, Locale locale )
		throws Exception
	{

		final HashMap<String, Object> result = new HashMap<String, Object>();

		final Usuario user = getUserFromSession( session );

		final ServletOutputStream outputStream = response.getOutputStream();

		final Page<Servico> page = this.servicoService
			.getResultPage(
				pageNumber,
				pageSize,
				pedido,
				cpf,
				dataInicial,
				dataFinal,
				status,
				null,
				null,
				user,
				cpfEmployee,
				speciality );

		if ( outType.indexOf( "json" ) != -1 )
		{
			result.put( "servicos", page.getContent() );
			result.put( "totalElements", page.getTotalElements() );
			new ObjectMapper().writeValue( outputStream, result );
			outputStream.flush();
			outputStream.close();
		}

		if ( outType.indexOf( ".xls" ) != -1 )
		{

			this.servicoService.downloadExcel( page.getContent(), outputStream );
		}

		if ( outType.indexOf( ".pdf" ) != -1 )
		{
			this.servicoService.downloadPdf( page.getContent(), outputStream, locale );

		}

		response.setHeader( "Content-Type", "application/octet-stream" );

	}

	@PutMapping( "/update" )
	@ResponseBody
	public String update(
		@RequestParam( value = "serviceId" ) final Long serviceId,
		@RequestParam( value = "cpf" ) final String cpf,
		@RequestParam( value = "status" ) final String status )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();

		try
		{
			this.servicoService.update( serviceId, cpf, StatusService.valueOf( status ) );
			result.put( "status", "SUCESSO" );
		}
		catch ( final Exception e )
		{
			result.put( "status", e );
		}

		return Conversion.convertToJson( result );
	}

	@Autowired
	private Service servicoService;

}
