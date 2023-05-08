package br.com.BarberShopFreeStyle.daos;

import br.com.BarberShopFreeStyle.models.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

public interface UserDao
	extends AbstractDao<Usuario, Long>
{

	Usuario getByLogin( String login );

	Usuario getByLoginAndPassword( String login, String password );

	Page<Usuario> listEmployees( Specification specs, Integer pageNumber, Integer pageSize );
}
