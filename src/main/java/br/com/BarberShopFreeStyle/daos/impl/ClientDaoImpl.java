package br.com.BarberShopFreeStyle.daos.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.BarberShopFreeStyle.daos.ClientDao;
import br.com.BarberShopFreeStyle.dtos.ClienteDto;
import br.com.BarberShopFreeStyle.dtos.IntervalHoursScheduling;
import br.com.BarberShopFreeStyle.models.Cliente;
import br.com.BarberShopFreeStyle.models.Servico;

@Repository
public class ClientDaoImpl extends AbstractDaoImpl<Cliente> implements ClientDao {

	@Override
	public List<Cliente> all() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cliente getById(Long id) {
		
		 CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		 
		 CriteriaQuery<Cliente> q = cb.createQuery(Cliente.class);
		 
		 Root<Cliente> c = q.from(Cliente.class);
		 
		 q.where(cb.equal(c.get("id"), id));
		 
		 q.select(c);
		 
		 List<Cliente> requests = entityManager.createQuery(q).getResultList();
		
		if(requests.isEmpty())
		{
			return null;
		}
		
		return requests.get(0);
	}

	@Override
	public Cliente insert(Cliente entity) {
		// TODO Auto-generated method stub
		return save(entity);
	}

	@Override
	public Cliente update(Cliente entity) {
		// TODO Auto-generated method stub
		return updateObject(entity);
	}

	@Override
	public void delete(Cliente entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
		String sql = "DELETE FROM cliente WHERE id_cliente = :id";
		
		EntityManager entityManager = this.em.createEntityManager();
		
		entityManager.getTransaction().begin();

		Query query = entityManager.createNativeQuery(sql, Cliente.class);
		query.setParameter("id", id);
		
		query.executeUpdate();
		entityManager.flush();
		
		entityManager.getTransaction().commit();		
		entityManager.close();
		
	}

	@Override
	protected Class<? extends Cliente> getBeanClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cliente getByCPF(String cpf) {
		// TODO Auto-generated method stub
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		 
		 CriteriaQuery<Cliente> q = cb.createQuery(Cliente.class);
		 
		 Root<Cliente> c = q.from(Cliente.class);
		 
		 q.where(cb.equal(c.get("cpf"), cpf));
		 
		 q.select(c);
		 
		 List<Cliente> requests = entityManager.createQuery(q).getResultList();
		
		if(requests.isEmpty())
		{
			return null;
		}
		
		return requests.get(0);
	}
	
	@Transactional
	@Override
	public Page<Cliente> listClients(@SuppressWarnings("rawtypes") Specification specs, Integer pageNumber, Integer pageSize) 
	{
		return listPages(specs,pageNumber,pageSize,Cliente.class);
	}

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private EntityManagerFactory em;

	@Override
	public ClienteDto getClientLikeCpf(String cpf) {
		// TODO Auto-generated method stub
		
		EntityManager entityManager = em.createEntityManager();
		String sql = "select nome,cpf from cliente where cpf like '%"+cpf+"%' and data_exclusao is null ";
		
		entityManager.getTransaction().begin();

		Query query = entityManager.createNativeQuery(sql);
		query.unwrap(org.hibernate.query.Query.class)
		.setResultTransformer(Transformers.aliasToBean(ClienteDto.class));
		
		List<ClienteDto> result = query.getResultList();
		entityManager.flush();
		entityManager.getTransaction().commit();	
		entityManager.close();
		
		if(result.isEmpty())
		{
			return null;
		}
		
		return result.get(0);
		
	}


	@Override
	public ClienteDto getClientLikeNome(String nome) {
		// TODO Auto-generated method stub
	
		EntityManager entityManager = em.createEntityManager();
		String sql = "select nome,cpf from cliente where nome like '%"+nome+"%' and data_exclusao is null ";
		
		entityManager.getTransaction().begin();

		Query query = entityManager.createNativeQuery(sql);
		query.unwrap(org.hibernate.query.Query.class)
		.setResultTransformer(Transformers.aliasToBean(ClienteDto.class));
		
		List<ClienteDto> result = query.getResultList();
		entityManager.flush();
		entityManager.getTransaction().commit();	
		entityManager.close();
		
		if(result.isEmpty())
		{
			return null;
		}
		
		return result.get(0);
	}
	


}
