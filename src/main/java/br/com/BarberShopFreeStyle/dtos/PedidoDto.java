package br.com.BarberShopFreeStyle.dtos;

import java.math.BigInteger;

public class PedidoDto {
	
	private String nome;
	
	private java.sql.Time tempo;
	
	private String preco;
	
	private BigInteger tiposPerfis;
	
	private Integer idpedido;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public java.sql.Time getTempo() {
		return tempo;
	}

	public void setTempo(java.sql.Time tempo) {
		this.tempo = tempo;
	}

	public String getPreco() {
		return preco;
	}

	public void setPreco(String preco) {
		this.preco = preco;
	}

	public BigInteger getTiposPerfis() {
		return tiposPerfis;
	}

	public void setTiposPerfis(BigInteger tiposPerfis) {
		this.tiposPerfis = tiposPerfis;
	}

	public Integer getIdPedido() {
		return idpedido;
	}

	public void setIdPedido(Integer idPedido) {
		this.idpedido = idPedido;
	}
	
	

}
