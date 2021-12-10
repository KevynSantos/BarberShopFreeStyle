package br.com.BarberShopFreeStyle.services;

import java.text.ParseException;
import java.util.List;

import br.com.BarberShopFreeStyle.dtos.StatusCrudDto;
import br.com.BarberShopFreeStyle.models.PedidoServico;
import br.com.BarberShopFreeStyle.models.Usuario;

public interface RequestServiceService extends AbstractService<PedidoServico, Long> {
	
	StatusCrudDto update(Long idService, List<Long> idRequests, Usuario user ) throws ParseException;

}
