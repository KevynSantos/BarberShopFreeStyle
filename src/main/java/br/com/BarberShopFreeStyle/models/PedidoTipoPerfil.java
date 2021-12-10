package br.com.BarberShopFreeStyle.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



@Entity
@Table(name = "pedido_tipo_perfil")
public class PedidoTipoPerfil {
	
	private Long id;
	
	private Pedido pedido;
	
	private TipoPerfil tipoPerfil;

	@Id
	@Column(name = "id_pedido_tipo_perfil")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@JoinColumn(name = "id_pedido")
	@ManyToOne
	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	@JoinColumn(name = "id_tipo_perfil")
	@ManyToOne
	public TipoPerfil getTipoPerfil() {
		return tipoPerfil;
	}

	public void setTipoPerfil(TipoPerfil tipoPerfil) {
		this.tipoPerfil = tipoPerfil;
	}

	
	
	

}
