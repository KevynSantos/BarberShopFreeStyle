package br.com.BarberShopFreeStyle.api.messages;

/**
 * <p>
 * </p>
 * 
 * @author Kevyn
 * @version 1.0 Created on 4 de mai de 2023
 */
public enum MessagesApi
{
	EMAIL_IS_EMPTY( "E-mail não pode ser vazio" ),

	EMAIL_IS_NOT_VALID( "E-mail é inválido" ),

	EMAIL_NOT_SEND( "Ocorreu um erro ao tentar enviar o email para esse destinatário: %S" ),

	INSPECT_ERROR( "Ocorreu um erro: %s" ),

	INVALID_CODE( "Código de verificação Inválido" ),

	LOGIN_FIELDS_EMPTY( "Login ou senha não preenchidos" ),

	LOGIN_INCORRECT( "Login ou senha incorretos" ),

	PARAM_CODE( "code" ),

	PARAM_MESSAGE( "message" ),

	REQUIRED_FIELD( "Campo %s inválido/ausente" ),

	SUCCESS( "Sucesso" ),

	UNKNOW_ERROR( "Erro desconhecido" );

	MessagesApi( final String message )
	{
		this.message = message;
	}

	public String getMessage()
	{
		return this.message;
	}

	private String message;
}
