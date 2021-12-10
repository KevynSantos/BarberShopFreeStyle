package br.com.BarberShopFreeStyle.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name = "pedido_servico")
public class PedidoServico {
	
	private Long id;
	private Long idServico;
	private Pedido pedido;
	private Date dataExclusao;
	
	@Id
	@Column(name = "id_pedido_servico")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column( name = "id_servico")
	public Long getIdServico() {
		return idServico;
	}
	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}
	@ManyToOne
	@JoinColumn( name = "id_pedido")
	public Pedido getPedido() {
		return pedido;
	}
	public void setPedido(Pedido idPedido) {
		this.pedido = idPedido;
	}
	
	@Column(name = "data_exclusao")
	public Date getDataExclusao() {
		return dataExclusao;
	}
	public void setDataExclusao(Date dataExclusao) {
		this.dataExclusao = dataExclusao;
	}
	
	
	
	

}
