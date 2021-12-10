package br.com.BarberShopFreeStyle.daos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import br.com.BarberShopFreeStyle.dtos.PedidoDto;
import br.com.BarberShopFreeStyle.models.PedidoTipoPerfil;

public interface PedidoTipoPerfilDao extends AbstractDao<PedidoTipoPerfil,Long> {

	Page<PedidoDto> getRequests(Integer pageNumber, Integer pageSize,String query);

	List<PedidoTipoPerfil> getTypeProfileByIdRequest(Long idRequest);

}
