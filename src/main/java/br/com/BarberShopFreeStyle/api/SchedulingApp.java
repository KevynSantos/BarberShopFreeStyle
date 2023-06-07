package br.com.BarberShopFreeStyle.api;

import br.com.BarberShopFreeStyle.controllers.AbstractController;
import br.com.BarberShopFreeStyle.dtos.app.ListagemAgendamentosApp;
import br.com.BarberShopFreeStyle.models.Servico;
import br.com.BarberShopFreeStyle.models.Usuario;
import br.com.BarberShopFreeStyle.services.SchedulingService;
import br.com.BarberShopFreeStyle.services.Service;
import br.com.BarberShopFreeStyle.services.UserService;

import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * </p>
 *
 * @author Kevyn
 * @version 1.0 Created on 2 de jun de 2023
 */
@RestController
@RequestMapping( "/api/scheduling" )
public class SchedulingApp
	extends AbstractController
{

	@GetMapping( "/list" )
	public void getSchedules(
		@RequestParam( value = "idUser", required = true ) final Long idUser,
		@RequestParam( value = "pageNumber", required = false ) final Integer pageNumber,
		@RequestParam( value = "pageSize", required = false ) final Integer pageSize,
		@RequestParam( value = "text_pedido", required = false ) final String pedido,
		@RequestParam( value = "text_cpf", required = false ) final String cpf,
		@RequestParam( value = "text_data_inicial", required = false ) final String dataInicial,
		@RequestParam( value = "text_data_final", required = false ) final String dataFinal,
		@RequestParam( value = "text_hora_inicial", required = false ) final String horaInicial,
		@RequestParam( value = "text_hora_final", required = false ) final String horaFinal,
		@RequestParam( value = "text_cpf_employee", required = false ) final String cpfEmployee,
		@RequestParam( value = "select_speciality_employee", required = false ) final String speciality,
		final HttpServletResponse response,
		final HttpSession session )
		throws Exception
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();

		final ServletOutputStream outputStream = response.getOutputStream();

		try
		{

			final Usuario user = this.userService.getById( idUser );

			throwNoUserLogged( user );

			final Page<Servico> page = this.service
				.getResultPage(
					pageNumber,
					pageSize,
					pedido,
					cpf,
					dataInicial,
					dataFinal,
					null,
					horaInicial,
					horaFinal,
					user,
					cpfEmployee,
					speciality );

			final List<ListagemAgendamentosApp> list = this.schedulingService.buildDtoForListSchedulesInApp( page );

			result.put( "agendamentos", list );
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
	private SchedulingService schedulingService;

	@Autowired
	private Service service;

	@Autowired
	private UserService userService;
}
