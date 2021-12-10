package br.com.BarberShopFreeStyle.services;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

public interface SimpleEmailService  {
	
	void sendEmail( final String bodyHtml, final String subject, final String[] contacts )throws UnsupportedEncodingException,
	MessagingException;

}
