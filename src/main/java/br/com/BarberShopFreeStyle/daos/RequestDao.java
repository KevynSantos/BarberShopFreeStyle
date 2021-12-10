package br.com.BarberShopFreeStyle.daos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import br.com.BarberShopFreeStyle.enums.TypeInService;
import br.com.BarberShopFreeStyle.models.Pedido;
import br.com.BarberShopFreeStyle.models.PedidoTipoPerfil;
import br.com.BarberShopFreeStyle.models.Usuario;

public interface RequestDao extends AbstractDao<Pedido,Long> {

	List<PedidoTipoPerfil> getByName(String name, Usuario user, TypeInService isProduct);
	
	Page<Pedido> listRequests(Specification specs, Integer pageNumber, Integer pageSize);
	
	
	
}
