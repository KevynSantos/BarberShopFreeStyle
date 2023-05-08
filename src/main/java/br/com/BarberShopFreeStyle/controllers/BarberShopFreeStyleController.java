package br.com.BarberShopFreeStyle.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.BarberShopFreeStyle.daos.DescontoDao;
import br.com.BarberShopFreeStyle.enums.Access;
import br.com.BarberShopFreeStyle.models.Usuario;

@Controller
public class BarberShopFreeStyleController extends AbstractController {
	
	
	@GetMapping(value = "/home")
	public ModelAndView home(HttpSession session, HttpServletRequest req, HttpServletResponse resp) 
	{
		
		
		Usuario user = getUserFromSession(session);
		
		final ModelAndView page = new ModelAndView("home");
		
		page.addObject("user", user);
		
		return page;
	}
	
	@GetMapping( value = "/addService")
	public ModelAndView addService(HttpSession session, HttpServletRequest req, HttpServletResponse resp) 
	{
		
		
		Usuario user = getUserFromSession(session);
		
		ModelAndView page = new ModelAndView("addService");
		
		page = checkPermissionProfile(user,Access.NAO_PODE_AGENDAR,page);
		
		page.addObject("user", user);
		
		
		
		return page;
	}
	
	@GetMapping(value = "/addScheduling")
	public ModelAndView addScheduling(HttpSession session, HttpServletRequest req, HttpServletResponse resp) 
	{
		Usuario user = getUserFromSession(session);
		
		ModelAndView page = new ModelAndView("addScheduling");
		
		page = checkPermissionProfile(user,Access.PODE_AGENDAR,page);
		
		page.addObject("user", user);
		
		return page;
	}
	
	@GetMapping(value = "/services")
	public ModelAndView services(HttpSession session, HttpServletRequest req, HttpServletResponse resp) 
	{
		Usuario user = getUserFromSession(session);
		
		ModelAndView page = new ModelAndView("services");
		
		page = checkPermissionProfile(user,Access.NAO_PODE_AGENDAR,page);
		
		page.addObject("user", user);
		if(this.descontoDao.getDiscount().isAtivo())
		{
			page.addObject("discount_of_client", this.descontoDao.getDiscount().getValor());
		}
		
		return page;
	}
	
	@GetMapping(value = "/clients")
	public ModelAndView clients(HttpSession session, HttpServletRequest req, HttpServletResponse resp) 
	{
		Usuario user = getUserFromSession(session);
		
		final ModelAndView page = new ModelAndView("clients");
		
		page.addObject("user", user);
		
		return page;
	}
	
	@GetMapping(value = "/schedules")
	public ModelAndView schedules(HttpSession session, HttpServletRequest req, HttpServletResponse resp) 
	{
		Usuario user = getUserFromSession(session);
		
		ModelAndView page = new ModelAndView("schedules");
		
		page = checkPermissionProfile(user,Access.PODE_AGENDAR,page);
		
		page.addObject("user", user);
		if(this.descontoDao.getDiscount().isAtivo())
		{
			page.addObject("discount_of_client", this.descontoDao.getDiscount().getValor());
		}
			
		
		return page;
	}
	
	@GetMapping(value = "/management")
	public ModelAndView management(HttpSession session,HttpServletRequest req, HttpServletResponse resp)
	{
		Usuario user = getUserFromSession(session);
		
		ModelAndView page = new ModelAndView("management");
		
		page.addObject("user", user);
		
		page = checkPermissionManagement(user, page);
		
		return page;
	}
	
	
	
	
	@Autowired
	private DescontoDao descontoDao;

}
