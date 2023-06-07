package br.com.BarberShopFreeStyle.services;


import br.com.BarberShopFreeStyle.dtos.IntervalHoursScheduling;
import br.com.BarberShopFreeStyle.dtos.StatusCrudDto;
import br.com.BarberShopFreeStyle.enums.IntervalStatus;
import br.com.BarberShopFreeStyle.models.Agendamento;
import br.com.BarberShopFreeStyle.models.Pedido;
import br.com.BarberShopFreeStyle.models.Servico;
import br.com.BarberShopFreeStyle.models.Usuario;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;

import org.springframework.data.domain.Page;

public interface SchedulingService
	extends AbstractService<Agendamento, Long>
{

	List<StatusCrudDto> addScheduling(
		final String cpf,
		final String[] pedidos,
		final String descricao,
		final String data,
		final String hora,
		final Usuario usuario )
		throws ParseException;

	String calculateTime( String date );

	void checkin( Agendamento agendamento );

	void downloadExcel( List<Servico> schedulings, ServletOutputStream outputStream )
		throws IOException;

	void downloadPdf( final List<Servico> services, final OutputStream outputStream, Locale locale )
		throws IOException;

	void scheduledFinal( Servico servico );

	StatusCrudDto update( Long serviceId,  String cpf, String data, String hora, Usuario usuario )
		throws ParseException;
	


	boolean checkintervalRangeTimeRequests(String data, String time, List<Pedido> listRequests, Usuario usuario, IntervalStatus intervalStatus, Servico servico) throws ParseException;
	
	List<IntervalHoursScheduling> getHoursByDate(String date, Usuario user);

	/**
	 * <p></p>
	 * @param page 
	 * @return
	 */
	List<br.com.BarberShopFreeStyle.dtos.app.ListagemAgendamentosApp> buildDtoForListSchedulesInApp(Page<Servico> page);

}
