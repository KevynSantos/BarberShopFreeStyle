package br.com.BarberShopFreeStyle.daos;

import java.util.List;

import br.com.BarberShopFreeStyle.models.Pedido;
import br.com.BarberShopFreeStyle.models.PedidoServico;

public interface RequestServiceDao extends AbstractDao<PedidoServico,Long>{
	
	List<PedidoServico> getRequestByService(Long idService);

}
