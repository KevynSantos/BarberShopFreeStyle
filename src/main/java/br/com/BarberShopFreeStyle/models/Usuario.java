package br.com.BarberShopFreeStyle.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table( name = "usuario" )
public class Usuario
{

	@JoinColumn( name = "id_cliente" )
	@OneToOne
	public Cliente getCliente()
	{
		return this.cliente;
	}

	@Column( name = "data_exclusao" )
	public Date getDataExclusao()
	{
		return this.dataExclusao;
	}

	@JoinColumn( name = "id_funcionario" )
	@OneToOne
	public Funcionario getFuncionario()
	{
		return this.funcionario;
	}

	@Id
	@Column( name = "id_usuario" )
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	public Long getId()
	{
		return this.id;
	}

	@Column( name = "login" )
	public String getLogin()
	{
		return this.login;
	}

	@Column( name = "senha" )
	public String getPassword()
	{
		return this.password;
	}

	@JoinColumn( name = "id_tipo_perfil" )
	@OneToOne
	public TipoPerfil getTipoPerfil()
	{
		return this.tipoPerfil;
	}

	public void setCliente( final Cliente cliente )
	{
		this.cliente = cliente;
	}

	public void setDataExclusao( final Date dataExclusao )
	{
		this.dataExclusao = dataExclusao;
	}

	public void setFuncionario( final Funcionario funcionario )
	{
		this.funcionario = funcionario;
	}

	public void setId( final Long id )
	{
		this.id = id;
	}

	public void setLogin( final String login )
	{
		this.login = login;
	}

	public void setPassword( final String password )
	{
		this.password = password;
	}

	public void setTipoPerfil( final TipoPerfil tipoPerfil )
	{
		this.tipoPerfil = tipoPerfil;
	}

	private Cliente cliente;

	private Date dataExclusao;

	private Funcionario funcionario;

	private Long id;

	private String login;

	private String password;

	private TipoPerfil tipoPerfil;

}
