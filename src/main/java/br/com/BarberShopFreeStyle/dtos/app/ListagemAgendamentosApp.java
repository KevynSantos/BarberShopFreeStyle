package br.com.BarberShopFreeStyle.dtos.app;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Kevyn
 * @version 1.0 Created on 7 de jun de 2023
 */
public class ListagemAgendamentosApp
{

	public String getData()
	{
		return this.data;
	}

	public String getHora()
	{
		return this.hora;
	}

	public Long getId()
	{
		return this.id;
	}

	public String getNomeFuncionario()
	{
		return this.nomeFuncionario;
	}

	public List<String> getPedidos()
	{
		return this.pedidos;
	}

	public void setData( final String data )
	{
		this.data = data;
	}

	public void setHora( final String hora )
	{
		this.hora = hora;
	}

	public void setId( final Long id )
	{
		this.id = id;
	}

	public void setNomeFuncionario( final String nomeFuncionario )
	{
		this.nomeFuncionario = nomeFuncionario;
	}

	public void setPedidos( final List<String> pedidos )
	{
		this.pedidos = pedidos;
	}

	private String data;

	private String hora;

	private Long id;

	private String nomeFuncionario;

	private List<String> pedidos;

}
