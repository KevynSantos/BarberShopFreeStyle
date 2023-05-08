package br.com.BarberShopFreeStyle.models;

import br.com.BarberShopFreeStyle.enums.StatusService;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table( name = "servico" )
public class Servico
{

	@JoinColumn( name = "id_agendamento" )
	@OneToOne( cascade = CascadeType.MERGE )
	public Agendamento getAgendamento()
	{
		return this.agendamento;
	}

	@JoinColumn( name = "id_cliente" )
	@OneToOne
	public Cliente getCliente()
	{
		return this.cliente;
	}

	@Column( name = "data_criacao" )
	public Date getDataCriacao()
	{
		return this.dataCriacao;
	}

	@Column( name = "data_finalizacao" )
	public Date getDataFinalizacao()
	{
		return this.dataFinalizacao;
	}

	@Column( name = "descricao_servico" )
	public String getDescricaoServico()
	{
		return this.descricaoServico;
	}

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id_servico" )
	public Long getId()
	{
		return this.id;
	}

	@OneToMany( mappedBy = "idServico", cascade = CascadeType.ALL, fetch = FetchType.EAGER )
	public List<PedidoServico> getPedidoServico()
	{
		return this.pedidoServico;
	}
	

	@Column( name = "status" )
	@Enumerated( EnumType.STRING )
	public StatusService getStatus()
	{
		return this.status;
	}

	@JoinColumn( name = "id_usuario" )
	@ManyToOne
	public Usuario getUsuario()
	{
		return this.usuario;
	}

	public void setAgendamento( final Agendamento agendamento )
	{
		this.agendamento = agendamento;
	}

	public void setCliente( final Cliente cliente )
	{
		this.cliente = cliente;
	}

	public void setDataCriacao( final Date dataCriacao )
	{
		this.dataCriacao = dataCriacao;
	}

	public void setDataFinalizacao( final Date dataFinalizacao )
	{
		this.dataFinalizacao = dataFinalizacao;
	}

	public void setDescricaoServico( final String descricaoServico )
	{
		this.descricaoServico = descricaoServico;
	}

	public void setId( final Long id )
	{
		this.id = id;
	}

	public void setPedidoServico( final List<PedidoServico> pedidoServico )
	{
		this.pedidoServico = pedidoServico;
	}
	
	public void setStatus( final StatusService status )
	{
		this.status = status;
	}

	public void setUsuario( final Usuario usuario )
	{
		this.usuario = usuario;
	}

	private Agendamento agendamento;

	private Cliente cliente;

	private Date dataCriacao;

	private Date dataFinalizacao;

	private String descricaoServico;

	private Long id;

	private List<PedidoServico> pedidoServico;

	private StatusService status;

	private Usuario usuario;

}
