package br.com.BarberShopFreeStyle.controllers;

import br.com.BarberShopFreeStyle.dtos.PedidoDto;
import br.com.BarberShopFreeStyle.dtos.StatusCrudDto;
import br.com.BarberShopFreeStyle.enums.TypeInService;
import br.com.BarberShopFreeStyle.models.Pedido;
import br.com.BarberShopFreeStyle.models.PedidoTipoPerfil;
import br.com.BarberShopFreeStyle.models.Usuario;
import br.com.BarberShopFreeStyle.services.PedidoTipoPerfilService;
import br.com.BarberShopFreeStyle.services.RequestService;
import br.com.BarberShopFreeStyle.utils.Conversion;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
@RequestMapping( "/request" )
public class RequestController extends AbstractController
{

	@PostMapping( "/add" )
	@ResponseBody
	public String add(
		@RequestParam( "text_new_name_request_save" ) final String nameRequest,
		@RequestParam( "text_new_time_request" ) final String time,
		@RequestParam( "text_new_valor_request" ) final String valor,
		@RequestParam("specialitys") String[] specialitys,
		HttpSession session )
		throws JsonProcessingException
	{
		
		Usuario user = getUserFromSession(session);
		final HashMap<String, Object> result = new HashMap<String, Object>();

		try
		{
			final String status = this.pedidoService.add( nameRequest, time, valor, user,specialitys,TypeInService.REQUEST );
			result.put( "status", status );
		}
		catch ( final Exception e )
		{
			result.put( "status", "error:" + e );
		}

		return Conversion.convertToJson( result );
	}

	@PostMapping( "/delete" )
	@ResponseBody
	@Transactional
	public ModelAndView delete( @RequestParam( "idRequestErase" ) final Long idRequestErase )
	{
		final Pedido entity = this.pedidoService.getById( idRequestErase );
		this.pedidoService.delete( entity );
		return new ModelAndView( "redirect:/management?deleteSuccessRequest" );
	}

	@GetMapping( "/getById" )
	@ResponseBody
	public String getById( @RequestParam( "idRequest" ) final Long idRequest )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();

		final Pedido pedido = this.pedidoService.getById( idRequest );

		result.put( "request", pedido );

		return Conversion.convertToJson( result );
	}
	
	@GetMapping("/getCountRequests")
	@ResponseBody
	public String getCountRequests(HttpSession session) throws JsonProcessingException
	{
		getUserFromSession(session);
		
		final HashMap<String, Object> result = new HashMap<String, Object>();
		
		List<Pedido> pedidos = this.pedidoService.all();
		
		result.put( "size", pedidos.size() );
		
		return Conversion.convertToJson( result );
		
	}

	@GetMapping( "/getByName" )
	@ResponseBody
	public String getByName( @RequestParam( "name" ) final String name, HttpSession session )
		throws JsonProcessingException
	{
		Usuario user = getUserFromSession(session);
		
		final List<PedidoTipoPerfil> pedidos = this.pedidoService.getByName( name, user,TypeInService.ALL );

		final HashMap<String, Object> result = new HashMap<String, Object>();

		result.put( "requests", pedidos );

		return Conversion.convertToJson( result );
	}

	@GetMapping( "/getRequestByService" )
	@ResponseBody
	public String getRequestByService( @RequestParam( "idService" ) final Long idService )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();

		final List<Pedido> pedidos = this.pedidoService.getRequestByService( idService );

		result.put( "requests", pedidos );

		return Conversion.convertToJson( result );

	}

	@GetMapping( "/{outType:.+}" )
	public void getRequests(
		@RequestParam( value = "pageNumber", required = false ) final Integer pageNumber,
		@PathVariable( value = "outType" ) final String outType,
		@RequestParam( value = "pageSize", required = false ) final Integer pageSize,
		final HttpServletResponse response, Locale locale )
		throws Exception
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();
		
		boolean isProduct = false;

		final Page<PedidoDto> page = this.pedidoTipoPerfilService.getRequests( pageNumber, pageSize,isProduct );

		final ServletOutputStream outputStream = response.getOutputStream();

		if ( outType.indexOf( "json" ) != -1 )
		{
			result.put( "requests", page.getContent() );
			result.put( "totalElements", page.getTotalElements() );
			new ObjectMapper().writeValue( outputStream, result );
			outputStream.flush();
			outputStream.close();
		}

		if ( outType.indexOf( ".xls" ) != -1 )
		{

			this.pedidoTipoPerfilService.downloadExcel( page.getContent(), outputStream );
		}

		if ( outType.indexOf( ".pdf" ) != -1 )
		{
			this.pedidoTipoPerfilService.downloadPdf( page.getContent(), outputStream, locale );

		}

		response.setHeader( "Content-Type", "application/octet-stream" );

	}

	@PutMapping( "/update" )
	@ResponseBody
	public String update(
		@RequestParam( "idRequest" ) final Long idRequest,
		@RequestParam( "text_edit_name_request" ) final String name,
		@RequestParam( "text_edit_time_request" ) final String time,
		@RequestParam( "text_new_valor_request" ) final String valor )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();

		try
		{
			final List<StatusCrudDto> status = this.pedidoService.update( idRequest, name, time, valor );
			result.put( "status", status );
		}
		catch ( final Exception e )
		{
			result.put( "status", "ERRO:" + e );
		}

		return Conversion.convertToJson( result );
	}
	
	@GetMapping("/getSumTimeRequestByIds")
	@ResponseBody
	public String getSumTimeRequestByIds(@RequestParam(value = "ids")List<String> ids,@RequestParam("hour") String hour) throws JsonProcessingException, ParseException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();
		
		java.sql.Time sum = this.pedidoService.getSumTimeRequestByIds(ids,hour);
		
		result.put("countTime", sum);
		
		return Conversion.convertToJson( result );
	}
	
	@GetMapping("/getTypeProfileByIdRequest")
	@ResponseBody
	public String getTypeProfileByIdRequest(@RequestParam("idRequest") Long idRequest) throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();
		
		List<PedidoTipoPerfil> list = pedidoTipoPerfilService.getTypeProfileByIdRequest(idRequest);
		
		result.put("pedidoTipoPerfil", list);
		
		return Conversion.convertToJson( result );
	}
	
	

	@Autowired
	private RequestService pedidoService;
	
	@Autowired
	private PedidoTipoPerfilService pedidoTipoPerfilService;

}
