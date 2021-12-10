package br.com.BarberShopFreeStyle.daos.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import br.com.BarberShopFreeStyle.daos.ServiceDao;
import br.com.BarberShopFreeStyle.models.Cliente;
import br.com.BarberShopFreeStyle.models.Servico;
import br.com.BarberShopFreeStyle.models.Usuario;
import br.com.BarberShopFreeStyle.utils.Conversion;

@Repository
public class ServiceDaoImpl extends AbstractDaoImpl<Servico> implements ServiceDao {

	@Override
	protected Class<? extends Servico> getBeanClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Servico> all() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Servico getById(Long id) {
		// TODO Auto-generated method stub
		 CriteriaBuilder cb = em.getCriteriaBuilder();

		  CriteriaQuery<Servico> q = cb.createQuery(Servico.class);
		  Root<Servico> c = q.from(Servico.class);
		  
		  Predicate conditions = cb.conjunction();
		  
		  conditions = cb.and(conditions,cb.equal(c.get("id"), id));
		  
		  q.where(conditions);
		  
		  q.select(c);
		  
		  EntityManager entityManager = this.em.createEntityManager();
		  
		  List<Servico> list = entityManager.createQuery(q).getResultList();
		  
		  entityManager.close();
		  
		  if(list.isEmpty())
		  {
			  return null;
		  }
		  
		  
		  
		  return list.get(0);
	}
	
	
	
	@Override
	@Transactional
	public Servico insert(Servico entity) {
		// TODO Auto-generated method stub
		
		return save(entity);
	}

	@Override
	public Servico update(Servico entity) {
		// TODO Auto-generated method stub
		return updateObject(entity);
	}

	@Override
	public void delete(Servico entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
		String sql = "DELETE FROM servico WHERE id_servico = :id";
		
		EntityManager entityManager = this.em.createEntityManager();
		
		entityManager.getTransaction().begin();

		Query query = entityManager.createNativeQuery(sql, Servico.class);
		query.setParameter("id", id);
		
		query.executeUpdate();
		entityManager.flush();
		
		entityManager.getTransaction().commit();		
		entityManager.close();
		
		
		
	}
	
	@Override
	@Transactional
	public Page<Servico> listServices(@SuppressWarnings("rawtypes") Specification specs, Integer pageNumber, Integer pageSize) 
	{
		return listPages(specs,pageNumber,pageSize,Servico.class);
	}
	
	@Autowired
	private EntityManagerFactory em;

	@Override
	public List<br.com.BarberShopFreeStyle.models.Servico> getByIdClient(Long id) {
		// TODO Auto-generated method stub
		CriteriaBuilder cb = em.getCriteriaBuilder();

		  CriteriaQuery<br.com.BarberShopFreeStyle.models.Servico> q = cb.createQuery(br.com.BarberShopFreeStyle.models.Servico.class);
		  Root<br.com.BarberShopFreeStyle.models.Servico> c = q.from(br.com.BarberShopFreeStyle.models.Servico.class);
		  
		  Join<Servico,Cliente> joinClient = c.join("cliente");
		  
		  Predicate conditions = cb.conjunction();
		  
		  conditions = cb.and(conditions,cb.equal(joinClient.get("id"), id));
		  
		  q.where(conditions);
		  
		  q.select(c);
		  
		  EntityManager entityManager = this.em.createEntityManager();
		  
		  List<br.com.BarberShopFreeStyle.models.Servico> list = entityManager.createQuery(q).getResultList();
		  
		  entityManager.close();
		  
		  if(list.isEmpty())
		  {
			  return null;
		  }
		  
		  return list;
	}

	@Override
	public List<Servico> listServicesByUserFor30Days(Usuario usuario) throws ParseException {
		// TODO Auto-generated method stub
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -30);
		cal.set( Calendar.HOUR_OF_DAY, 00 );
		cal.set( Calendar.MINUTE, 00 );
		cal.set( Calendar.SECOND, 00 );
		cal.set( Calendar.MILLISECOND, 00 );
	    Date dateInit = cal.getTime();
	    
	    Date now = new Date();
	    
		CriteriaBuilder cb = em.getCriteriaBuilder();

		  CriteriaQuery<Servico> q = cb.createQuery(Servico.class);
		  Root<Servico> c = q.from(Servico.class);
		  
		  Predicate conditions = cb.and(
				  						cb.equal(c.join("usuario").get("id"), usuario.getId()));
		  
		  conditions = cb.and(conditions,cb.or(cb.between(c.get("dataCriacao"), dateInit, now),
					cb.between(c.join("agendamento",JoinType.LEFT).get("data"), Conversion.convertDateSql(sdf.format(dateInit)), Conversion.convertDateSql(sdf.format(now)))));

		  
		  q.where(conditions);
		  
		  q.select(c);
		  
		  EntityManager entityManager = this.em.createEntityManager();
		  
		  List<Servico> list = entityManager.createQuery(q).getResultList();
		  
		  entityManager.close();
		  
		  if(list.isEmpty())
		  {
			  return null;
		  }
		  
		  return list;
	}

}
