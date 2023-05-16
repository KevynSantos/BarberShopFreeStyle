package br.com.BarberShopFreeStyle.dtos.app;

/**
 * <p>
 * </p>
 *
 * @author Kevyn
 * @version 1.0 Created on 15 de mai de 2023
 */
public class RegisterDto
{

	public String getAddress()
	{
		return this.address;
	}

	public String getConfirmCodeEmail()
	{
		return this.confirmCodeEmail;
	}

	public String getCpf()
	{
		return this.cpf;
	}

	public String getDateNasc()
	{
		return this.dateNasc;
	}

	public String getEmail()
	{
		return this.email;
	}

	public String getName()
	{
		return this.name;
	}

	public String getPassword()
	{
		return this.password;
	}

	public String getPhone()
	{
		return this.phone;
	}

	public void setAddress( final String address )
	{
		this.address = address;
	}

	public void setConfirmCodeEmail( final String confirmCodeEmail )
	{
		this.confirmCodeEmail = confirmCodeEmail;
	}

	public void setCpf( final String cpf )
	{
		this.cpf = cpf;
	}

	public void setDateNasc( final String dateNasc )
	{
		this.dateNasc = dateNasc;
	}

	public void setEmail( final String email )
	{
		this.email = email;
	}

	public void setName( final String name )
	{
		this.name = name;
	}

	public void setPassword( final String password )
	{
		this.password = password;
	}

	public void setPhone( final String phone )
	{
		this.phone = phone;
	}

	private String address;

	private String confirmCodeEmail;

	private String cpf;

	private String dateNasc;

	private String email;

	private String name;

	private String password;

	private String phone;

}
