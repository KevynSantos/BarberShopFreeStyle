package br.com.BarberShopFreeStyle.daos.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import br.com.BarberShopFreeStyle.daos.PedidoTipoPerfilDao;
import br.com.BarberShopFreeStyle.dtos.IntervalHoursScheduling;
import br.com.BarberShopFreeStyle.dtos.PedidoDto;
import br.com.BarberShopFreeStyle.models.Pedido;
import br.com.BarberShopFreeStyle.models.PedidoTipoPerfil;

@Repository
public class PedidoTipoPerfilDaoImpl extends AbstractDaoImpl<PedidoTipoPerfil> implements PedidoTipoPerfilDao {

	@Override
	public List<PedidoTipoPerfil> all() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PedidoTipoPerfil getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PedidoTipoPerfil insert(PedidoTipoPerfil entity) {
		// TODO Auto-generated method stub
		return save(entity);
	}

	@Override
	public PedidoTipoPerfil update(PedidoTipoPerfil entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(PedidoTipoPerfil entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Class<? extends PedidoTipoPerfil> getBeanClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<PedidoDto> getRequests(Integer pageNumber, Integer pageSize, String sql) {
		// TODO Auto-generated method stub
		
		
		if(pageNumber == null)
		{
			pageNumber = 0;
		}
		
		else
		{
			pageNumber = pageNumber-1;
		}
		
		EntityManager entityManager = this.em.createEntityManager();

		Query query = entityManager.createNativeQuery(sql);
		query
		.unwrap(org.hibernate.query.Query.class)
		.setResultTransformer(Transformers.aliasToBean(PedidoDto.class));
		List<PedidoDto> list = query.getResultList();
		PagedListHolder page = new PagedListHolder(list);
		//page.setPageSize(pageSize); // number of items per page
		//page.setPage(pageNumber);      // set to first page

		// Retrieval
		// page.getPageCount(); // number of pages 
		
		entityManager.close();
		
		Pageable paging = PageRequest.of(pageNumber, pageSize);
		
		int start = Math.min((int)paging.getOffset(), list.size());
		int end = Math.min((start + paging.getPageSize()), list.size());
		
		return new PageImpl<>(list.subList(start, end), paging, list.size());
	}
	
	@Autowired
	private EntityManagerFactory em;

	@Override
	public List<PedidoTipoPerfil> getTypeProfileByIdRequest(Long idRequest) {
		// TODO Auto-generated method stub
		
		 CriteriaBuilder cb = em.getCriteriaBuilder();
		 EntityManager entityManager = this.em.createEntityManager();

		  CriteriaQuery<PedidoTipoPerfil> q = cb.createQuery(PedidoTipoPerfil.class);
		  Root<PedidoTipoPerfil> c = q.from(PedidoTipoPerfil.class);
		  
		  Join<PedidoTipoPerfil, Pedido> joinPedido = c.join("pedido");
		  
		  Predicate conditions = cb.and( cb.equal(joinPedido.get("id"), idRequest) );
		  q.where(conditions);
		  q.select(c);
		  
		  List<PedidoTipoPerfil> result = entityManager.createQuery(q).getResultList();
		  
		  entityManager.close();
		  
		return result;
	}

}
