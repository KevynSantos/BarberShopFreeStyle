package br.com.BarberShopFreeStyle.daos.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.BarberShopFreeStyle.daos.RequestServiceDao;
import br.com.BarberShopFreeStyle.models.Agendamento;
import br.com.BarberShopFreeStyle.models.Pedido;
import br.com.BarberShopFreeStyle.models.PedidoServico;

@Repository
public class RequestServiceDaoImpl extends AbstractDaoImpl<PedidoServico> implements RequestServiceDao {

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
		return save(entity);
	}

	@Override
	public PedidoServico update(PedidoServico entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(PedidoServico entity) {
		// TODO Auto-generated method stub
		
		entity.setDataExclusao(new Date());
		updateObject(entity);
		
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Class<? extends PedidoServico> getBeanClass() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Autowired
	private EntityManagerFactory em;

	@Override
	public List<PedidoServico> getRequestByService(Long idService) {
		// TODO Auto-generated method stub
		
		EntityManager entityManager = this.em.createEntityManager();
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<PedidoServico> q = cb.createQuery(PedidoServico.class);
	    Root<PedidoServico> c = q.from(PedidoServico.class);
	    
	    Predicate conditions = cb.conjunction();
	    
	    conditions = cb.and(conditions, cb.equal( c.get("idServico"),idService));
	    
	    conditions = cb.and(conditions,cb.isNull(c.join("pedido").get( "dataExclusao" )));
	    
	    conditions = cb.and(conditions, cb.isNull(c.get("dataExclusao")));
	    
	    q.where(conditions);
	    
		q.select(c);
		
		
		List<PedidoServico> result = entityManager.createQuery( q ).getResultList();
		
		
		entityManager.close();
		
		return result;
	}

	
}
