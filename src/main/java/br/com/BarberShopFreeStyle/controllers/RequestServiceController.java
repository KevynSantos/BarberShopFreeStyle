package br.com.BarberShopFreeStyle.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.BarberShopFreeStyle.dtos.StatusCrudDto;
import br.com.BarberShopFreeStyle.enums.StatusCrudEnum;
import br.com.BarberShopFreeStyle.models.Usuario;
import br.com.BarberShopFreeStyle.services.RequestServiceService;
import br.com.BarberShopFreeStyle.services.SchedulingService;
import br.com.BarberShopFreeStyle.services.Service;
import br.com.BarberShopFreeStyle.utils.Conversion;

@Controller
@RequestMapping("/requestService")
public class RequestServiceController extends AbstractController {
	
	@PutMapping("/update")
	@ResponseBody
	public String update(@RequestParam(value = "idService") Long idService, @RequestParam(value = "idRequests") List<Long> idRequests, HttpSession session) throws JsonProcessingException
	{
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		StatusCrudDto status = null;
		
		Usuario user = getUserFromSession(session);
		
		if(!checkIfContainsRequest(idRequests))
		{
			status = new StatusCrudDto(StatusCrudEnum.EMPTY_ORDER_LIST,null);
			result.put("status", status);
			return Conversion.convertToJson(result);
		}
		
		
		try
		{
			status = requestServiceService.update(idService, idRequests, user);
			result.put("status", status);
		}
		catch(Exception e)
		{
			status = new StatusCrudDto(StatusCrudEnum.ERROR,e.getMessage());
			result.put("status", status);
		}
		
		return Conversion.convertToJson(result);
	}
	
	private boolean checkIfContainsRequest(List<Long> idRequests)
	{
		if(idRequests.isEmpty())
		{
			return false;
		}
		
		return true;
	}
	
	@Autowired
	private RequestServiceService requestServiceService;
	
	
	

}
