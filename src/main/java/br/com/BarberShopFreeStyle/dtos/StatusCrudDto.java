package br.com.BarberShopFreeStyle.dtos;

import br.com.BarberShopFreeStyle.enums.StatusCrudEnum;

/**
 * <p>
 * </p>
 *
 * @author kevyn.santos
 * @version 1.0 Created on Sep 23, 2021
 */
public class StatusCrudDto
{

	public StatusCrudDto( final StatusCrudEnum status, final String message )
	{
		if ( StatusCrudEnum.ERROR.equals( status ) )
		{
			this.messageError = status.toString() + ":" + message;
		}

		else
		{
			this.messageError = status.toString();
		}

	}

	public String getMessage()
	{
		return this.messageError;
	}

	private String messageError;

}
