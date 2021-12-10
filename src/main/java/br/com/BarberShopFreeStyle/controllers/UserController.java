package br.com.BarberShopFreeStyle.controllers;

import br.com.BarberShopFreeStyle.dtos.StatusCrudDto;
import br.com.BarberShopFreeStyle.enums.StatusChangedPassword;
import br.com.BarberShopFreeStyle.models.Usuario;
import br.com.BarberShopFreeStyle.services.UserService;
import br.com.BarberShopFreeStyle.utils.Conversion;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping( value = "/us" )
public class UserController
	extends AbstractController
{

	@PostMapping( "/add" )
	@ResponseBody
	public String add(
		@RequestParam( "text_new_name_employee" ) final String name,
		@RequestParam( "text_new_cpf_employee" ) final String cpf,
		@RequestParam( "text_new_data_nascimento_employee" ) final String dateNasc,
		@RequestParam( "text_new_email_employee" ) final String email,
		@RequestParam( "text_new_telefone_employee" ) final String telefone,
		@RequestParam( "text_new_adress_employee" ) final String adress,
		@RequestParam( "select_type_employee" ) final String tipoFuncionario,
		@RequestParam( "text_new_login_employee" ) final String login,
		@RequestParam( "text_new_senha_employee" ) final String password )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();

		try
		{
			final List<StatusCrudDto> status = this.userService
				.add( name, cpf, dateNasc, email, telefone, adress, tipoFuncionario, login, password );
			result.put( "status", status );
		}

		catch ( final Exception e )
		{
			result.put( "status", "ERRO:" + e );
		}

		return Conversion.convertToJson( result );
	}

	@PutMapping( "/changedPassword" )
	@ResponseBody
	public String changedPassword(
		@RequestParam( "idUserFromPassword" ) final Long idUser,
		@RequestParam( "text_edit_password" ) final String password,
		@RequestParam( "text_edit_confirm_password" ) final String confirmPassword )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();

		try
		{
			final StatusChangedPassword status = this.userService
				.tryChangedPassword( idUser, password, confirmPassword );
			result.put( "status", status.toString() );

		}
		catch ( final Exception e )
		{
			result.put( "status", e.getMessage() );
		}

		return Conversion.convertToJson( result );
	}

	@PostMapping( "/delete" )
	@ResponseBody
	@Transactional
	public ModelAndView delete( @RequestParam( "idEmployeeErase" ) final Long idEmployeeErase )
	{
		final Usuario entity = this.userService.getById( idEmployeeErase );
		this.userService.delete( entity );
		return new ModelAndView( "redirect:/management?deleteSuccessEmployee" );
	}

	@GetMapping( "/{outType:.+}" )
	public void getEmployees(
		@RequestParam( value = "pageNumber", required = false ) final Integer pageNumber,
		@PathVariable( value = "outType" ) final String outType,
		@RequestParam( value = "pageSize", required = false ) final Integer pageSize,
		final HttpServletResponse response, Locale locale )
		throws Exception
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();

		final Page<Usuario> page = this.userService.getResultPage( pageNumber, pageSize );

		final ServletOutputStream outputStream = response.getOutputStream();

		if ( outType.indexOf( "json" ) != -1 )
		{
			result.put( "usuarios", page.getContent() );
			result.put( "totalElements", page.getTotalElements() );
			new ObjectMapper().writeValue( outputStream, result );
			outputStream.flush();
			outputStream.close();
		}

		if ( outType.indexOf( ".xls" ) != -1 )
		{

			this.userService.downloadExcel( page.getContent(), outputStream );
		}

		if ( outType.indexOf( ".pdf" ) != -1 )
		{
			this.userService.downloadPdf( page.getContent(), outputStream, locale );

		}

		response.setHeader( "Content-Type", "application/octet-stream" );
	}

	@GetMapping( "/getUser" )
	@ResponseBody
	public String getUser( @RequestParam( "idUser" ) final Long idUser )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();
		final Usuario user = this.userService.getById( idUser );
		result.put( "user", user );
		return Conversion.convertToJson( result );
	}
	
	@GetMapping( "/getUserFromSession" )
	@ResponseBody
	public String getUsuarioFromSession(HttpSession session) throws JsonProcessingException
	{
		
		final HashMap<String, Object> result = new HashMap<String, Object>();
		Usuario user = getUserFromSession(session);
		result.put( "user", user );
		return Conversion.convertToJson( result );
		
	}
	
	@PostMapping("/changeData")
	@ResponseBody
	public String changeData(
			@RequestParam( "text_edit_name_data" ) String nome,
			@RequestParam( "text_edit_name_telefone_data" ) String telefone,
			@RequestParam( "text_edit_name_email_data" ) String email,
			@RequestParam( "text_edit_data_nascimento_data" ) String dtNascimento,
			@RequestParam( "text_edit_name_cpf_data" ) String cpf,
			@RequestParam( "text_edit_endereco_employee_data" ) String endereco,
			@RequestParam( value = "text_edit_name_senha_data", required=false ) String senha,
			HttpSession session) throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();
		
		Usuario user = getUserFromSession(session);
		
		try
		{
			List<StatusCrudDto> status = this.userService.changeData(nome,
					cpf,
					dtNascimento,
					email,
					telefone,
					endereco,
					senha, user);
		
			result.put("status", status);
		}
		catch(Exception e)
		{
			result.put( "status", "ERRO:" + e );
		}
		
		return Conversion.convertToJson( result );
	}

	@RequestMapping( value = "/login", method = RequestMethod.POST )
	@ResponseBody
	@Transactional
	public ModelAndView login(
		@RequestParam( "login" ) final String login,
		@RequestParam( "senha" ) final String password,
		final HttpSession session )
	{
		final Usuario user = this.userService.getByLoginAndPassword( login, password );

		ModelAndView page;

		if ( user == null )
		{
			page = new ModelAndView( "redirect:/" );
			page.addObject( "error", "loginIncorrect" );

			return page;
		}

		session.setAttribute( "userId", user.getId() );
		session.setAttribute( "userPassword", user.getPassword() );

		page = new ModelAndView( "redirect:/home" );

		return page;
	}

	@RequestMapping( value = "/logoff", method = RequestMethod.GET )
	public ModelAndView logoff( final HttpSession session )
	{
		session.removeAttribute( "userId" );
		session.removeAttribute( "userPassword" );

		final ModelAndView page = new ModelAndView( "redirect:/" );

		return page;

	}

	@PostMapping( "/update" )
	@ResponseBody
	public String update(
		@RequestParam( value = "text_edit_name_employee" ) final String nome,
		@RequestParam( value = "text_edit_cpf_employee" ) final String cpf,
		@RequestParam( value = "text_edit_date_employee" ) final String dataNasc,
		@RequestParam( value = "text_edit_email_employee" ) final String email,
		@RequestParam( value = "text_edit_telefone_employee" ) final String telefone,
		@RequestParam( value = "text_edit_endereco_employee" ) final String endereco,
		@RequestParam( value = "select_type_employee" ) final String typeEmployee,
		@RequestParam( value = "idUser" ) final Long idUser )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();
		try
		{

			final List<StatusCrudDto> status = this.userService
				.update( nome, cpf, dataNasc, email, telefone, endereco, typeEmployee, idUser );
			result.put( "status", status );
		}
		catch ( final Exception e )
		{
			result.put( "status", "ERRO:" + e );
		}

		return Conversion.convertToJson( result );
	}
	
	@GetMapping("/getRankingByUser")
	@ResponseBody
	public void getRankingByUser(@RequestParam("idUser")Long idUser, HttpSession session, final HttpServletResponse response) throws IOException, ParseException
	{
		getUserFromSession(session);
		
		final ServletOutputStream outputStream = response.getOutputStream();
		
		Usuario usuario = this.userService.getById(idUser);
		
		this.userService.getEmployeeRanking(usuario, outputStream);
		
	}

	@Autowired
	private UserService userService;

}
