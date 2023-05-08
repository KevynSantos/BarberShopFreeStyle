package br.com.BarberShopFreeStyle.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "cliente")
public class Cliente {
	private Long id;
	private String nome;
	private String cpf;
	private Date dtNascimento;
	private String endereco;
	private String telefone;
	private String email;
	
	@Id
	@Column( name = "id_cliente")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	
	@Column( name = "data_nascimento")
	public Date getDtNascimento() {
		return dtNascimento;
	}
	public void setDtNascimento(Date dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column( name = "nome")
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	@Column( name = "cpf")
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	@Column(name = "endereco")
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	@Column( name = "telefone")
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	@Column( name = "email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	private java.util.Date dataExclusao;

	@Column(name = "data_exclusao")
	public java.util.Date getDataExclusao() {
		return dataExclusao;
	}

	public void setDataExclusao(java.util.Date dataExclusao) {
		this.dataExclusao = dataExclusao;
	}
	
	
}
