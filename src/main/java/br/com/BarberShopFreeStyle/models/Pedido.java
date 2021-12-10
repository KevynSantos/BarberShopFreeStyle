package br.com.BarberShopFreeStyle.models;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "pedido" )
public class Pedido
{

	@Column( name = "data_exclusao" )
	public Date getDataExclusao()
	{
		return this.dataExclusao;
	}

	@Id
	@Column( name = "id_pedido" )
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	public Long getId()
	{
		return this.id;
	}

	@Column( name = "nome" )
	public String getNome()
	{
		return this.nome;
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @return Returns the preco.
	 * @see #preco
	 */
	@Column( name = "preco" )
	public String getPreco()
	{
		return this.preco;
	}

	@Column( name = "tempo" )
	public Time getTempo()
	{
		return this.tempo;
	}

	public void setDataExclusao( final Date dataExclusao )
	{
		this.dataExclusao = dataExclusao;
	}

	public void setId( final Long id )
	{
		this.id = id;
	}

	public void setNome( final String nome )
	{
		this.nome = nome;
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param preco
	 *            The preco to set.
	 * @see #preco
	 */
	public void setPreco( final String preco )
	{
		this.preco = preco;
	}

	public void setTempo( final Time tempo )
	{
		this.tempo = tempo;
	}

	private Date dataExclusao;

	private Long id;

	private String nome;

	private String preco;

	private Time tempo;
	
	private boolean produto;

	@Column(name = "produto", length = 1)
	public boolean isProduto() {
		return produto;
	}

	public void setProduto(boolean produto) {
		this.produto = produto;
	}
	
	

}
