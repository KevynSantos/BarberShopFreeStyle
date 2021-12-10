package br.com.BarberShopFreeStyle.daos.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.BarberShopFreeStyle.daos.DescontoDao;
import br.com.BarberShopFreeStyle.models.Desconto;

@Repository
public class DescontoImpl extends AbstractDaoImpl<Desconto> implements DescontoDao {

	@Override
	public List<Desconto> all() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Desconto getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Desconto insert(Desconto entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Desconto update(Desconto entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Desconto entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Class<? extends Desconto> getBeanClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void activeOrActive(boolean isActive) {
		// TODO Auto-generated method stub
		
		 CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		 
		 CriteriaQuery<Desconto> q = cb.createQuery(Desconto.class);
		 
		 Root<Desconto> c = q.from(Desconto.class);
		 
		 q.select(c);
		 
		 Desconto desconto = entityManager.createQuery(q).getSingleResult();
		
		 desconto.setAtivo(isActive);
		 
		 updateObject(desconto);
		
	}
	
	@Autowired
	private EntityManager entityManager;

	@Override
	public void updateValueDiscount(String value) {
		
		 CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		 
		 CriteriaQuery<Desconto> q = cb.createQuery(Desconto.class);
		 
		 Root<Desconto> c = q.from(Desconto.class);
		 
		 q.select(c);
		 
		 Desconto desconto = entityManager.createQuery(q).getSingleResult();
		 
		 desconto.setValor(value);
		 
		 updateObject(desconto);
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public Desconto getDiscount() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		 
		 CriteriaQuery<Desconto> q = cb.createQuery(Desconto.class);
		 
		 Root<Desconto> c = q.from(Desconto.class);
		 
		 q.select(c);
		 
		 Desconto desconto = entityManager.createQuery(q).getSingleResult();
		 
		return desconto;
	}
	


}
