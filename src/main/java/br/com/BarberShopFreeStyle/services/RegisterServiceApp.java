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

	Pair<Boolean, HashMap<String, Object>> validFieldsForSendEmail( RegisterDto dto );
}
