package br.com.BarberShopFreeStyle.daos.impl;

import br.com.BarberShopFreeStyle.daos.RequestDao;
import br.com.BarberShopFreeStyle.enums.TypeInService;
import br.com.BarberShopFreeStyle.models.Pedido;
import br.com.BarberShopFreeStyle.models.PedidoTipoPerfil;
import br.com.BarberShopFreeStyle.models.TipoPerfil;
import br.com.BarberShopFreeStyle.models.Usuario;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RequestDaoImpl
	extends AbstractDaoImpl<Pedido>
	implements RequestDao
{

	@Override
	public List<Pedido> all()
	{
		// TODO Auto-generated method stub

		final CriteriaBuilder cb = this.em.getCriteriaBuilder();

		final CriteriaQuery<Pedido> q = cb.createQuery( Pedido.class );

		final Root<Pedido> c = q.from( Pedido.class );

		q.select( c );

		final List<Pedido> requests = this.em.createQuery( q ).getResultList();

		return requests;
	}

	@Override
	public void delete( final Pedido entity )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById( final Long id )
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected Class< ? extends Pedido> getBeanClass()
	{
		// TODO Auto-generated method stub
		return Pedido.class;
	}

	@Override
	public Pedido getById( final Long id )
	{
		// TODO Auto-generated method stub
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();

		final CriteriaQuery<Pedido> q = cb.createQuery( Pedido.class );

		final Root<Pedido> c = q.from( Pedido.class );

		q.where( cb.equal( c.get( "id" ), id ) );

		q.select( c );

		final List<Pedido> requests = this.em.createQuery( q ).getResultList();

		if ( requests.isEmpty() )
		{
			return null;
		}

		return requests.get( 0 );

	}

	@Override
	public List<PedidoTipoPerfil> getByName( final String name, Usuario user, TypeInService productOrRequest )
	{
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		
		TipoPerfil tipo = user.getTipoPerfil();

		final CriteriaQuery<PedidoTipoPerfil> q = cb.createQuery( PedidoTipoPerfil.class );

		final Root<PedidoTipoPerfil> c = q.from( PedidoTipoPerfil.class );
		
		Join<PedidoTipoPerfil,Pedido> joinPedido = c.join("pedido");
		Join<PedidoTipoPerfil,TipoPerfil> joinTipoPerfil = c.join("tipoPerfil");

		Predicate conditions = cb.and( cb.equal(joinTipoPerfil.get("id"), tipo.getId()),
				cb.like( joinPedido.get( "nome" ), name ), 
				cb.isNull( joinPedido.get( "dataExclusao" ) ) );
		
		boolean isProduct = false; 
		
		if(TypeInService.PRODUCT.equals(productOrRequest))
		{
			isProduct = true;
			conditions = cb.and(conditions,cb.equal(joinPedido.get("produto"), isProduct));
		}
		if(TypeInService.REQUEST.equals(productOrRequest))
		{
			conditions = cb.and(conditions,cb.equal(joinPedido.get("produto"), isProduct));
		}
		
		q.where( conditions );
		

		q.select( c );

		final List<PedidoTipoPerfil> requests = this.em.createQuery( q ).getResultList();

		return requests;
	}

	@Override
	public Pedido insert( final Pedido entity )
	{
		// TODO Auto-generated method stub
		return save( entity );
	}

	@Override
	@Transactional
	public Page<Pedido> listRequests( final Specification specs, final Integer pageNumber, final Integer pageSize )
	{
		// TODO Auto-generated method stub

		return listPages( specs, pageNumber, pageSize, Pedido.class );
	}

	@Override
	public Pedido update( final Pedido entity )
	{
		// TODO Auto-generated method stub
		return updateObject( entity );
	}

	@Autowired
	private EntityManager em;

}
