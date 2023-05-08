package br.com.BarberShopFreeStyle.controllers;

import java.io.IOException;
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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.BarberShopFreeStyle.dtos.PedidoDto;
import br.com.BarberShopFreeStyle.dtos.StatusCrudDto;
import br.com.BarberShopFreeStyle.enums.TypeInService;
import br.com.BarberShopFreeStyle.models.Pedido;
import br.com.BarberShopFreeStyle.models.Usuario;
import br.com.BarberShopFreeStyle.services.PedidoTipoPerfilService;
import br.com.BarberShopFreeStyle.services.ProductService;
import br.com.BarberShopFreeStyle.services.RequestService;
import br.com.BarberShopFreeStyle.utils.Conversion;

@Controller
@RequestMapping( "/product" )
public class ProductController extends AbstractController {
	
	@GetMapping( "/{outType:.+}" )
	public void getProducts(@RequestParam( value = "pageNumber", required = false ) final Integer pageNumber,
			@PathVariable( value = "outType" ) final String outType,
			@RequestParam( value = "pageSize", required = false ) final Integer pageSize,
			final HttpServletResponse response, Locale locale) throws JsonGenerationException, JsonMappingException, IOException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();
		
		boolean isProduct = true;

		final Page<PedidoDto> page = this.pedidoTipoPerfilService.getRequests( pageNumber, pageSize, isProduct );

		final ServletOutputStream outputStream = response.getOutputStream();

		if ( outType.indexOf( "json" ) != -1 )
		{
			result.put( "products", page.getContent() );
			result.put( "totalElements", page.getTotalElements() );
			new ObjectMapper().writeValue( outputStream, result );
			outputStream.flush();
			outputStream.close();
		}

		if ( outType.indexOf( ".xls" ) != -1 )
		{

			this.productService.downloadExcel( page.getContent(), outputStream );
		}

		if ( outType.indexOf( ".pdf" ) != -1 )
		{
			this.productService.downloadPdf( page.getContent(), outputStream, locale );

		}

		response.setHeader( "Content-Type", "application/octet-stream" );
	}
	
	@PostMapping("/add")
	@ResponseBody
	public String add(
			@RequestParam( "text_new_name_product_save" ) final String name,
			@RequestParam( "text_new_valor_product" ) final String valor, HttpSession session ) throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();
		
		Usuario user = getUserFromSession(session);

		String[] speciality = {"ADMINISTRADOR"};
		try
		{
			final String status = this.pedidoService.add( name, null, valor, user,speciality, TypeInService.PRODUCT );
			result.put( "status", status );
		}
		catch ( final Exception e )
		{
			result.put( "status", "error:" + e );
		}

		return Conversion.convertToJson( result );
		
	}
	
	@GetMapping( "/getById" )
	@ResponseBody
	public String getById( @RequestParam( "idProduct" ) final Long idRequest )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();

		final Pedido pedido = this.pedidoService.getById( idRequest );

		result.put( "product", pedido );

		return Conversion.convertToJson( result );
	}
	
	@PutMapping( "/update" )
	@ResponseBody
	public String update(
		@RequestParam( "idProduct" ) final Long idRequest,
		@RequestParam( "text_edit_name_product" ) final String name,
		@RequestParam( "text_edit_preco_product" ) final String valor )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();

		try
		{
			final List<StatusCrudDto> status = this.pedidoService.update( idRequest, name, null, valor );
			result.put( "status", status );
		}
		catch ( final Exception e )
		{
			result.put( "status", "ERRO:" + e );
		}

		return Conversion.convertToJson( result );
	}
	
	@PostMapping( "/delete" )
	@ResponseBody
	@Transactional
	public ModelAndView delete( @RequestParam( "idProductErase" ) final Long idRequestErase )
	{
		final Pedido entity = this.pedidoService.getById( idRequestErase );
		this.pedidoService.delete( entity );
		return new ModelAndView( "redirect:/management?deleteSuccessProduct" );
	}
	
	
	@Autowired
	private PedidoTipoPerfilService pedidoTipoPerfilService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private RequestService pedidoService;

}
