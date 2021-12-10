package br.com.BarberShopFreeStyle.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.BarberShopFreeStyle.enums.TypeProfile;

@Entity
@Table( name = "tipo_perfil")
public class TipoPerfil {
	
	
	private Long id;
	private TypeProfile tipo;
	
	@Id
	@Column( name = "id_tipo_perfil")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long tipoPerfil) {
		this.id = tipoPerfil;
	}
	@Enumerated( EnumType.STRING )
	@Column( name = "tipo")
	public TypeProfile getTipo() {
		return tipo;
	}
	public void setTipo(TypeProfile tipo) {
		this.tipo = tipo;
	}
	
	
}
