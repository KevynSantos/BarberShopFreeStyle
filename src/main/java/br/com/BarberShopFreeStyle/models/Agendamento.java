package br.com.BarberShopFreeStyle.models;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "agendamento")
public class Agendamento{
	private Long id;
	private Time hora;
	private Date data;
	private java.util.Date checkin;
	private java.util.Date dtAbertura;
	
	@Column(name = "dt_abertura")
	public java.util.Date getDtAbertura() {
		return dtAbertura;
	}

	public void setDtAbertura(java.util.Date dtAbertura) {
		this.dtAbertura = dtAbertura;
	}
	private boolean notificacao;
	
	@Column(name = "notificacao", length = 1)
	public boolean getNotificacao() {
		return notificacao;
	}
	
	public void setNotificacao(boolean notificacao) {
		this.notificacao = notificacao;
	}
	
	@Column(name = "checkin", length = 1)
	public java.util.Date getCheckin() {
		return checkin;
	}
	public void setCheckin(java.util.Date checkin) {
		this.checkin = checkin;
	}
	@Id
	@Column( name = "id_agendamento")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column( name = "hora")
	public Time getHora() {
		return hora;
	}
	public void setHora(Time hora) {
		this.hora = hora;
	}
	
	@Column( name = "data")
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	
	
}
