package br.com.BarberShopFreeStyle.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>
 * </p>
 *
 * @author Kevyn
 * @version 1.0 Created on 15 de mai de 2023
 */
@Entity
@Table( name = "confirma_email" )
public class ConfirmEmail
{

	@Column( name = "id_cliente" )
	public Long getClient()
	{
		return this.client;
	}

	@Column( name = "codigo" )
	public String getCode()
	{
		return this.code;
	}

	@Column( name = "data_confirmacao" )
	public Date getConfirmDate()
	{
		return this.confirmDate;
	}

	@Column( name = "data" )
	public Date getDate()
	{
		return this.date;
	}

	@Column( name = "email" )
	public String getEmail()
	{
		return this.email;
	}

	@Id
	@Column( name = "id_confirma_email" )
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	public Long getId()
	{
		return this.id;
	}

	@Column( name = "id_usuario" )
	public Long getUser()
	{
		return this.user;
	}

	public void setClient( final Long client )
	{
		this.client = client;
	}

	public void setCode( final String code )
	{
		this.code = code;
	}

	public void setConfirmDate( final Date confirmDate )
	{
		this.confirmDate = confirmDate;
	}

	public void setDate( final Date date )
	{
		this.date = date;
	}

	public void setEmail( final String email )
	{
		this.email = email;
	}

	public void setId( final Long id )
	{
		this.id = id;
	}

	public void setUser( final Long user )
	{
		this.user = user;
	}

	private Long client;

	private String code;

	private Date confirmDate;

	private Date date;

	private String email;

	private Long id;

	private Long user;

}
