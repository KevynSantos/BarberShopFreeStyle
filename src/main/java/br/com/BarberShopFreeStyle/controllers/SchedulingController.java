package br.com.BarberShopFreeStyle.controllers;

import br.com.BarberShopFreeStyle.dtos.IntervalHoursScheduling;
import br.com.BarberShopFreeStyle.dtos.StatusCrudDto;
import br.com.BarberShopFreeStyle.enums.StatusCrudEnum;
import br.com.BarberShopFreeStyle.models.Agendamento;
import br.com.BarberShopFreeStyle.models.Servico;
import br.com.BarberShopFreeStyle.models.Usuario;
import br.com.BarberShopFreeStyle.services.SchedulingService;
import br.com.BarberShopFreeStyle.services.Service;
import br.com.BarberShopFreeStyle.utils.Conversion;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping( "/scheduling" )
public class SchedulingController
	extends AbstractController
{

	private static final Logger LOG = LogManager.getLogger( "SchedulingController" );

	@PostMapping( "/add" )
	@ResponseBody
	public String add(
		@RequestParam( "cpf" ) final String cpf,
		@RequestParam( "pedidos" ) final String[] pedidos,
		@RequestParam( "descricao" ) final String descricao,
		@RequestParam( "data" ) final String data,
		@RequestParam( "hora" ) final String hora,
		final HttpSession session )
		throws JsonProcessingException
	{
		final HashMap<String, Object> statusServer = new HashMap<String, Object>();

		try
		{

			final Usuario user = getUserFromSession( session );

			final List<StatusCrudDto> status = this.schedulingService.addScheduling( cpf, pedidos, descricao, data, hora, user );

			statusServer.put( "status", status);
		}

		catch ( final Exception e )
		{
			statusServer.put( "status", "ERRO:" + e );
		}

		return Conversion.convertToJson( statusServer );
	}

	@GetMapping( "/calculateTime" )
	@ResponseBody
	public String calculateTime( @RequestParam( "dateScheduling" ) final String date )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();

		final String week = this.schedulingService.calculateTime( date );

		result.put( "week", week );

		return Conversion.convertToJson( result );

	}

	@PostMapping( "/checkin" )
	@ResponseBody
	public ModelAndView checkin( @RequestParam( "idSchedulesCheckin" ) final Long idSchedulesCheckin )
	{
		final Servico service = this.service.getById( idSchedulesCheckin );

		final Agendamento agendamento = service.getAgendamento();

		this.schedulingService.checkin( agendamento );

		return new ModelAndView( "redirect:/schedules?checkinSuccess" );
	}

	@PostMapping( "/delete" )
	@ResponseBody
	public ModelAndView delete( @RequestParam( "idSchedules" ) final Long idScheduling )
	{
		final Servico service = this.service.getById( idScheduling );

		final Agendamento agendamento = service.getAgendamento();

		this.schedulingService.delete( this.schedulingService.getById(agendamento.getId()));

		this.service.delete( service.getId() );

		return new ModelAndView( "redirect:/schedules?deleteSuccess" );
	}

	@GetMapping( "/getById" )
	@ResponseBody
	public String getById( @RequestParam( "idService" ) final Long idService )
		throws JsonProcessingException
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();

		final Servico service = this.service.getById( idService );

		result.put( "service", service );

		return Conversion.convertToJson( result );

	}

	@GetMapping( "/{outType:.+}" )
	public void getSchedules(
		@RequestParam( value = "pageNumber", required = false ) final Integer pageNumber,
		@RequestParam( value = "pageSize", required = false ) final Integer pageSize,
		@PathVariable( value = "outType" ) final String outType,
		@RequestParam( value = "text_pedido", required = false ) final String pedido,
		@RequestParam( value = "text_cpf", required = false ) final String cpf,
		@RequestParam( value = "text_data_inicial", required = false ) final String dataInicial,
		@RequestParam( value = "text_data_final", required = false ) final String dataFinal,
		@RequestParam( value = "text_hora_inicial", required = false ) final String horaInicial,
		@RequestParam( value = "text_hora_final", required = false ) final String horaFinal,
		@RequestParam( value = "text_cpf_employee", required = false ) final String cpfEmployee,
		@RequestParam( value = "select_speciality_employee", required = false ) final String speciality,
		final HttpServletResponse response,
		final HttpSession session, Locale locale )
		throws Exception
	{
		final HashMap<String, Object> result = new HashMap<String, Object>();

		final ServletOutputStream outputStream = response.getOutputStream();

		final Usuario user = getUserFromSession( session );

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
				speciality,false );

		if ( outType.indexOf( "json" ) != -1 )
		{
			result.put( "agendamentos", page.getContent() );
			result.put( "totalElements", page.getTotalElements() );
			new ObjectMapper().writeValue( outputStream, result );
			outputStream.flush();
			outputStream.close();
		}

		if ( outType.indexOf( ".xls" ) != -1 )
		{

			this.schedulingService.downloadExcel( page.getContent(), outputStream );

		}

		if ( outType.indexOf( ".pdf" ) != -1 )
		{
			this.schedulingService.downloadPdf( page.getContent(), outputStream, locale );
		}

		response.setHeader( "Content-Type", "application/octet-stream" );

	}

	@PostMapping( "/final/scheduled" )
	@ResponseBody
	public ModelAndView scheduledFinal( @RequestParam( "idServiceFinal" ) final Long idServiceFinal )
	{
		final Servico service = this.service.getById( idServiceFinal );

		this.schedulingService.scheduledFinal( service );

		return new ModelAndView( "redirect:/schedules?sheduledFinal" );
	}
	
	@GetMapping("/getByDate")
	@ResponseBody
	public String getByDate(@RequestParam("date") String date, final HttpSession session) throws JsonProcessingException
	{
		final Usuario user = getUserFromSession( session );
		final HashMap<String, Object> result = new HashMap<String, Object>();
		
		List<IntervalHoursScheduling> listHours = this.schedulingService.getHoursByDate(date,user);
		
		result.put("schedules", listHours);
		
		return Conversion.convertToJson(result);
	}

	@PostMapping( "/update" )
	@ResponseBody
	public String update(
		@RequestParam( "cpf" ) final String cpf,
		@RequestParam( "data" ) final String data,
		@RequestParam( "hora" ) final String hora,
		@RequestParam( "serviceId" ) final Long serviceId, final HttpSession session )
		throws ParseException, JsonProcessingException
	{
		
		
		Usuario usuario = getUserFromSession( session );
		
		final HashMap<String, Object> result = new HashMap<String, Object>();
		
		try
		{
			StatusCrudDto status = this.schedulingService.update( serviceId,  cpf, data, hora, usuario );
			result.put("status",status);
		}
		catch(Exception e)
		{
			result.put("status", new StatusCrudDto(StatusCrudEnum.ERROR,e.getMessage()));
		}
		
		return Conversion.convertToJson(result);
	}

	@Autowired
	private SchedulingService schedulingService;

	@Autowired
	private Service service;

}
