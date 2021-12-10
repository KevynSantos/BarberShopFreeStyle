package br.com.BarberShopFreeStyle.daos;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import br.com.BarberShopFreeStyle.dtos.IntervalHoursScheduling;
import br.com.BarberShopFreeStyle.enums.IntervalStatus;
import br.com.BarberShopFreeStyle.enums.UpdateProductStatus;
import br.com.BarberShopFreeStyle.models.Agendamento;
import br.com.BarberShopFreeStyle.models.Servico;
import br.com.BarberShopFreeStyle.models.Usuario;

public interface SchedulingDao extends AbstractDao<Agendamento,Long> {
	
	void delete (Integer i);
	
	List<Servico> getSchedulingByDayNow();

	boolean checkintervalRangeTimeRequests(String data, String time, String maxIntervalLocalDateTime, Usuario usuario, IntervalStatus intervalStatus,List<UpdateProductStatus> updateProductStatus) throws ParseException;
	
	List<IntervalHoursScheduling> getHoursByDate(String date, Usuario user);





}
