package br.com.BarberShopFreeStyle.api.messages;


/**
 * <p>
 * </p>
 * @author Kevyn
 * @version 1.0 Created on 4 de mai de 2023
 */
public enum MessagesApi
{
	UNKNOW_ERROR("Erro desconhecido"),
	
	LOGIN_INCORRECT("Login ou senha incorretos"),
	
	LOGIN_FIELDS_EMPTY("Login ou senha não preenchidos"),
	
	SUCCESS("Sucesso"),
	
	PARAM_CODE("code"),
	
	PARAM_MESSAGE("message"),
	
	EMAIL_IS_NOT_VALID("E-mail é inválido"),
	
	EMAIL_IS_EMPTY("E-mail não pode ser vazio"),
	
	EMAIL_NOT_SEND("Ocorreu um erro ao tentar enviar o email para esse destinatário: %S");
	
	MessagesApi(String message)
	{
		this.message = message;
	}
	
	public String getMessage()
	{
		return this.message;
	}
	
	private String message;
}
