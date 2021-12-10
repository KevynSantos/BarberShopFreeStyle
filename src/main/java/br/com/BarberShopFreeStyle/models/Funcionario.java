package br.com.BarberShopFreeStyle.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "funcionario")
public class Funcionario {
	private Long id;
	private String nome;
	private String endereco;
	private boolean marcaHora;
	private String cpf;
	private String email;
	private String telefone;
	private java.sql.Date dtNascimento;
	
	@Id
	@Column( name = "id_funcionario")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
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
	@Column( name = "endereco")
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	@Column( name = "marca_hora",  length = 1)
	public boolean isMarcaHora() {
		return marcaHora;
	}
	public void setMarcaHora(boolean marcaHora) {
		this.marcaHora = marcaHora;
	}
	@Column( name = "cpf")
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	@Column( name = "email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Column( name = "telefone")
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@Column(name = "data_nascimento")
	public java.sql.Date getDtNascimento() {
		return dtNascimento;
	}
	public void setDtNascimento(java.sql.Date dtNascimento) {
		this.dtNascimento = dtNascimento;
	}
	
	
	
	
}
