package br.com.BarberShopFreeStyle.services.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.BarberShopFreeStyle.daos.RequestDao;
import br.com.BarberShopFreeStyle.daos.RequestServiceDao;
import br.com.BarberShopFreeStyle.dtos.StatusCrudDto;
import br.com.BarberShopFreeStyle.enums.IntervalStatus;
import br.com.BarberShopFreeStyle.enums.StatusCrudEnum;
import br.com.BarberShopFreeStyle.enums.StatusService;
import br.com.BarberShopFreeStyle.models.Pedido;
import br.com.BarberShopFreeStyle.models.PedidoServico;
import br.com.BarberShopFreeStyle.models.Servico;
import br.com.BarberShopFreeStyle.models.Usuario;
import br.com.BarberShopFreeStyle.services.RequestServiceService;
import br.com.BarberShopFreeStyle.services.SchedulingService;
import br.com.BarberShopFreeStyle.utils.Conversion;

@Service
public class RequestServiceServiceImpl implements RequestServiceService{

	@Override
	public List<PedidoServico> all() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PedidoServico getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PedidoServico insert(PedidoServico entity) {
		// TODO Auto-generated method stub
		return this.requestServiceDao.insert(entity);
	}

	@Override
	public PedidoServico update(PedidoServico entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(PedidoServico entity) {
		// TODO Auto-generated method stub
		this.requestServiceDao.delete(entity);
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}
	
	@Autowired
	private RequestServiceDao requestServiceDao;

	@Override
	public StatusCrudDto update(Long idService, List<Long> idRequests, Usuario user) throws ParseException {
		// TODO Auto-generated method stub
		List<PedidoServico> pedidoServicos = this.requestServiceDao.getRequestByService(idService);
		
		Servico servico = this.service.getById(pedidoServicos.get(0).getIdServico());
		
		List<Pedido> pedidos = new ArrayList<>();
		for(Long idpedido : idRequests)
		{
			pedidos.add(this.requestDao.getById(idpedido));
		}
		
		if(servico.getAgendamento() != null)
		{
			if(servico.getStatus() == null)
			{

				if(pedidos.size() > pedidoServicos.size())
				{
					
					if(!this.schedulingService.checkintervalRangeTimeRequests(Conversion.convertToDateSqlString(servico.getAgendamento().getData()), Conversion.convertToTimeStringEmail(servico.getAgendamento().getHora()), pedidos, user, IntervalStatus.ADD_REQUEST,servico))
					{
						return new StatusCrudDto(StatusCrudEnum.SCHEDULE_UNAVAILABLE_WITH_REQUEST,null);
					}
				}
				
			}
		}
		
		if(servico.getAgendamento() != null)
		{
			if(pedidos.stream().noneMatch(request -> !request.isProduto()))
			{
				return new StatusCrudDto( StatusCrudEnum.ONLY_PRODUCT_IN_SCHEDULING, null );	
			}
		}
		
		updateRequest(pedidoServicos,idRequests,idService);
		return new StatusCrudDto(StatusCrudEnum.SUCCESS,null);
	}
	
	@Autowired
	private br.com.BarberShopFreeStyle.services.Service service;
	
	@Autowired
	private RequestDao requestDao;
	
	private void updateRequest(List<PedidoServico> list,List<Long> idRequests, Long idService)
	{
		
		for(Long idRequest : idRequests)
		{
			
			boolean notFoundRequest = false;
		
			
			for(PedidoServico pedidoServico : list)
			{
				if(pedidoServico.getPedido().getId().equals(idRequest))
				{
					notFoundRequest = true;
				}
			}
			
			if(!notFoundRequest)
			{
				//caso não tenha o pedido serviço
				PedidoServico newPedidoServico = new PedidoServico();
				newPedidoServico.setIdServico(idService);
				Pedido pedido = requestDao.getById(idRequest);
				newPedidoServico.setPedido(pedido);
				requestServiceDao.insert(newPedidoServico);
				
			}
			
			
		}
		
		List<Long> idsCompare = new ArrayList<>();
		
		Map<Long,Object> requests = new HashMap<>();
		
		for(PedidoServico pedidoServico : list)
		{
			idsCompare.add(pedidoServico.getPedido().getId());
			requests.put(pedidoServico.getPedido().getId(), pedidoServico);
		}
		
		
		
		
		for(Long compare : idsCompare)
		{
			if(!idRequests.contains(compare))
			{
				//caso o usuário tenha removido o pedido serviço
				PedidoServico removeRequestService = (PedidoServico) requests.get(compare);
				this.delete(removeRequestService);
				
			}
		}
			
		
		
		
			
		
		
		
	}
	
	@Autowired
	private SchedulingService schedulingService;

}
