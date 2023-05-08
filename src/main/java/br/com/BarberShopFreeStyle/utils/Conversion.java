package br.com.BarberShopFreeStyle.utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Conversion {
	
	public static String convertToJson(HashMap<String,Object> map) throws JsonProcessingException
	{
		
		String json = new ObjectMapper().writeValueAsString(map);
		
		return json;
	}
	
	public static String convertToDateTimeString(Date date)
	{
		if(date == null)
		{
			return "";
		}
		
		SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		return spf.format(date);
	}
	
	public static String convertToDateString(Date date)
	{
		SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
		
		return spf.format(date);
	}
	
	public static String convertToDateSqlString(Date date)
	{
		SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
		
		return spf.format(date);
	}
	
	public static String convertToTimeString(Time hour)
	{
		SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss");
		
		return spf.format(hour);
	}
	
	public static String convertToTimeStringEmail(Time hour)
	{
		SimpleDateFormat spf = new SimpleDateFormat("HH:mm");
		
		return spf.format(hour);
	}
	
	public static Date convertDate(String dateStr) throws ParseException
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		return formatter.parse(dateStr);
	}
	
	public static Calendar dateToCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;

    }
	
	public static Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }
	
	public static java.sql.Date convertDateSql(String dateStr) throws ParseException
	{
		Date data = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
		return new java.sql.Date(data.getTime());
	}
	
	public static java.sql.Time convertTimeSql(String hourStr) throws ParseException
	{
		return java.sql.Time.valueOf(hourStr+":00");
	}
	
	public static Date convertToDateViaInstant(LocalDate dateToConvert) {
	    return java.util.Date.from(dateToConvert.atStartOfDay()
	      .atZone(ZoneId.systemDefault())
	      .toInstant());
	}
	
	public static java.sql.Time convertToTimeSql(LocalTime localTime)
	{
		return java.sql.Time.valueOf( localTime );
	}
	
	public static java.sql.Date convertToDateSql(LocalDateTime localDateTime)
	{
		java.sql.Date result = java.sql.Date.valueOf(localDateTime.toLocalDate());
		
		return result;
		
	}
	
	public static Time converToTimeSql(LocalDateTime localDateTime)
	{
		java.sql.Time result =  java.sql.Time.valueOf(localDateTime.toLocalTime());
		
		return result;
	}
	



}
