package br.com.BarberShopFreeStyle.controllers;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.BarberShopFreeStyle.services.SimpleEmailService;

@Controller
@RequestMapping("/email")
public class SimpleEmailController {
	
	@PostMapping("/send")
	@ResponseBody
	public void sendEmail(@RequestParam("bodyhtml") String bodyHtml,@RequestParam("subject") String subject,@RequestParam("contacts") final String[] contacts ) throws UnsupportedEncodingException, MessagingException
	{
		service.sendEmail(bodyHtml, subject, removeCaracteres(contacts));
	}
	
	private String[] removeCaracteres(String[] array)
	{
		String[] result = new String[array.length];
		int countResult =  0;
		for(String element : array)
		{
			result[countResult] = element.replace("[", "").replace("]", "").replace("\"", "");
			countResult++;
		}
		
		return result;
	}
	
	
	@Autowired
	private SimpleEmailService service;
}
