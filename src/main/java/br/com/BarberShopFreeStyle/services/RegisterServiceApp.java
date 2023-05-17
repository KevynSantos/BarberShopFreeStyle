package br.com.BarberShopFreeStyle.services;

import br.com.BarberShopFreeStyle.dtos.app.RegisterDto;

import java.util.HashMap;

import org.springframework.data.util.Pair;

/**
 * <p>
 * </p>
 *
 * @author Kevyn
 * @version 1.0 Created on 16 de mai de 2023
 */
public interface RegisterServiceApp
{

	/**
	 * <p>
	 * </p>
	 * 
	 * @param dto
	 * @return
	 */
	Pair<Boolean, HashMap<String, Object>> createClient( RegisterDto dto );

	Pair<Boolean, HashMap<String, Object>> validFieldsForConfirmCodeVerification( RegisterDto dto );

	/**
	 * <p>
	 * </p>
	 *
	 * @param dto
	 * @return
	 */
	Pair<Boolean, HashMap<String, Object>> validFieldsForRegisterUser( RegisterDto dto );

	Pair<Boolean, HashMap<String, Object>> validFieldsForSendEmail( RegisterDto dto );
}
