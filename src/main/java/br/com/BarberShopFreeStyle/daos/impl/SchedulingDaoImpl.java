package br.com.BarberShopFreeStyle.daos.impl;
import java.sql.Time;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.BarberShopFreeStyle.daos.SchedulingDao;
import br.com.BarberShopFreeStyle.dtos.IntervalHoursScheduling;
import br.com.BarberShopFreeStyle.enums.IntervalStatus;
import br.com.BarberShopFreeStyle.enums.UpdateProductStatus;
import br.com.BarberShopFreeStyle.models.Agendamento;
import br.com.BarberShopFreeStyle.models.Servico;
import br.com.BarberShopFreeStyle.models.Usuario;
import br.com.BarberShopFreeStyle.services.impl.AbstractService;
import br.com.BarberShopFreeStyle.utils.Conversion;



@Repository
public class SchedulingDaoImpl<R> extends AbstractDaoImpl<Agendamento> implements SchedulingDao {

	@Override
	public List<Agendamento> all() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Agendamento getById(Long id) {
		// TODO Auto-generated method stub
		CriteriaBuilder cb = em.getCriteriaBuilder();

		  CriteriaQuery<Agendamento> q = cb.createQuery(Agendamento.class);
		  Root<Agendamento> c = q.from(Agendamento.class);
		  
		  Predicate conditions = cb.conjunction();
		  
		  conditions = cb.and(conditions,cb.equal(c.get("id"), id));
		  
		  q.where(conditions);
		  
		  q.select(c);
		  
		  EntityManager entityManager = this.em.createEntityManager();
		  
		  List<Agendamento> list = entityManager.createQuery(q).getResultList();
		  
		  entityManager.close();
		  
		  if(list.isEmpty())
		  {
			  return null;
		  }
		  
		  return list.get(0);
	}

	@Override
	@Transactional
	public Agendamento insert(Agendamento entity) {
		// TODO Auto-generated method stub
		return save(entity);
	}

	@Override
	public Agendamento update(Agendamento entity) {
		// TODO Auto-generated method stub
		return updateObject(entity);
	}

	@Override
	public void delete(Agendamento entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Class<? extends Agendamento> getBeanClass() {
		// TODO Auto-generated method stub
		return Agendamento.class;
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
		String sql = "DELETE FROM agendamento WHERE id_agendamento = :id";
		
		EntityManager entityManager = this.em.createEntityManager();
		
		entityManager.getTransaction().begin();

		Query query = entityManager.createNativeQuery(sql, Agendamento.class);
		query.setParameter("id", id);
		
		query.executeUpdate();
		entityManager.flush();
		
		entityManager.getTransaction().commit();		
		entityManager.close();
		
	}
	
	
	
	@Autowired
	private EntityManagerFactory em;



	@Override
	public List<Servico> getSchedulingByDayNow() {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		 CriteriaQuery<Servico> q = cb.createQuery(Servico.class);
		  Root<Servico> c = q.from(Servico.class);
		  
		  Join<Agendamento,Servico> joinAgendamento = c.join("agendamento");
		  
		  Predicate conditions = cb.equal(joinAgendamento.get("data"), new Date());
		  
		  conditions = cb.and(conditions, cb.isFalse(joinAgendamento.get("notificacao")));
		  
		  conditions = cb.and( conditions, cb.isNull( c.get( "dataFinalizacao" ) ) );
		  
		  conditions = cb.and( conditions, cb.isNull( c.get( "dataCriacao" ) ) );

		  conditions = cb.and( conditions, cb.isNull( c.get( "status" ) ) );
		  
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

	
	@SuppressWarnings("deprecation")
	@Override
	public boolean checkintervalRangeTimeRequests(String date, String time, String maxIntervalLocalDateTime, Usuario usuario, IntervalStatus intervalStatus,List<UpdateProductStatus> updateProductStatus) throws ParseException {
		
		Date now = new Date();
		
		LocalDate dtFromLocalDateNow = now.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
		
		java.sql.Date dateSql = Conversion.convertDateSql(date);
		if(now.after( dateSql ))
		{
			return false;
		}
		
		LocalDate dtFromLocalDate = dateSql.toLocalDate();
		
		if(dtFromLocalDateNow.equals(dtFromLocalDate))
		{
			java.sql.Time timeCompare = Conversion.convertTimeSql(time);
			
			LocalTime localTimeCompare = timeCompare.toLocalTime();
			
			LocalTime nowCompare = now.toInstant()
				      .atZone(ZoneId.systemDefault()).toLocalTime();
			
			if(localTimeCompare.isBefore(nowCompare))
			{
				return false;
			}
		}
		
		Long idUser = usuario.getId();
		
		Calendar c = Calendar.getInstance();
		c.setTime(Conversion.convertDateSql(date));
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		
		String dayOfWeekStr = AbstractService.getDayOfWeek(dayOfWeek);
		
		String finalTime = null;
		
		if((dayOfWeekStr.equals("Saturday")) || (dayOfWeekStr.equals("Sunday")))
		{
			finalTime = "13:00:00";
		}
		else
		{
			finalTime = "19:00:00";
		}
		
		String caseHour = "";
		
		if(intervalStatus.equals(IntervalStatus.INSERT))
		{
			caseHour = " when ((:hour between result1.agendamento_anterior_hora_inicio and result1.agendamentoAnterior) OR (:maxInterval between result1.agendamento_anterior_hora_inicio and result1.agendamentoAnterior)) then false  " + 
					"    when ((:hour between result2.agendamento_posterior_hora_inicio and result2.agendamentoPosterior ) OR (:maxInterval between result2.agendamento_posterior_hora_inicio and result2.agendamentoPosterior )) then false  ";
		}
		
		else if(intervalStatus.equals(IntervalStatus.ADD_REQUEST))
		{
			if((updateProductStatus.contains(UpdateProductStatus.NEW_TIME) && !updateProductStatus.contains(UpdateProductStatus.NEW_PRODUCT)) || (updateProductStatus.contains(UpdateProductStatus.NEW_TIME) && updateProductStatus.contains(UpdateProductStatus.NEW_PRODUCT)))
			{
			
				caseHour = " when (:maxInterval between result1.agendamento_anterior_hora_inicio and result1.agendamentoAnterior) then false      " + 
					"    when (:maxInterval between result2.agendamento_posterior_hora_inicio and result2.agendamentoPosterior ) then false      ";
			}
			else
			{
				caseHour = " when (1=1) then true ";
			}
			
		}
		
		else
		{
			caseHour = " when (:hour between result1.agendamento_anterior_hora_inicio and result1.agendamentoAnterior) then false      " + 
					"    when (:hour between result2.agendamento_posterior_hora_inicio and result2.agendamentoPosterior ) then false      ";
		}
		
		EntityManager entityManager = this.em.createEntityManager();
		  
		Session session = entityManager.unwrap(Session.class);
		  
		@SuppressWarnings("rawtypes")
		final SQLQuery sql;
		
		sql = session.createSQLQuery("select  " + 
				" " + 
				"case " + 
					caseHour +
				"    else case "
				+ "			when"
				+ "				(:hour>'08:00:00') AND (:hour<:finalTime) AND (:maxInterval>'08:00:00') AND (:maxInterval<=:finalTime) then true " +
				"			else false "
				+ "			end "+
				" end as resultado " + 
				" " + 
				"	 " + 
				" " + 
				" " + 
				" from agendamento agenda " + 
				" " + 
				"		join(select  " + 
				" " + 
				"				SEC_TO_TIME(SUM(TIME_TO_SEC(count.countTimeRequests) + TIME_TO_SEC( a.hora ))) as agendamentoAnterior, a.hora as agendamento_anterior_hora_inicio, a.data agendamento_anterior_data_inicio " + 
				" " + 
				"			from agendamento a " + 
				" " + 
				"			join(select ag.id_agendamento as id_agendamento, SEC_TO_TIME( SUM( TIME_TO_SEC( p.tempo ) ) ) AS countTimeRequests, ag.hora, ag.data " + 
				" " + 
				"						from agendamento ag " + 
				" " + 
				"						join servico service on " + 
				" " + 
				"						service.id_agendamento = ag.id_agendamento " + 
				" " + 
				"						join pedido_servico pc on " + 
				" " + 
				"						pc.id_servico = service.id_servico " + 
				" " + 
				"						join pedido p on " + 
				" " + 
				"						p.id_pedido = pc.id_pedido "
				
				+ "						join servico ser on " + 
				
				" 						ser.id_agendamento = ag.id_agendamento " +
				
				"						join usuario u on "
				+ "						u.id_usuario = ser.id_usuario " + 
				
				"						where data = :date and hora <= :hour "
				
				+ "						and ser.status is null "
				+ " 					and ser.data_criacao is null " + 
				" 						and service.data_finalizacao is null " +
				 " 						and u.id_usuario = :idUser " +
				"						and pc.data_exclusao is null "+
				"                       and p.tempo is not null "+
				"                       group by ag.hora " +
				" " + 
				"						order by ag.data,ag.hora desc " + 
				" " + 
				"						limit 1) count on " + 
				"						 " + 
				"			count.id_agendamento = a.id_agendamento) result1 " + 
				"             " + 
				"		join(select  " + 
				" " + 
				" " + 
				"				SEC_TO_TIME(SUM(TIME_TO_SEC(count.countTimeRequests) + TIME_TO_SEC( a.hora ))) as agendamentoPosterior, a.hora as agendamento_posterior_hora_inicio, a.data agendamento_posterior_data_inicio " + 
				" " + 
				"			from agendamento a " + 
				" " + 
				"			join(select ag.id_agendamento as id_agendamento, SEC_TO_TIME( SUM( TIME_TO_SEC( p.tempo ) ) ) AS countTimeRequests, ag.hora, ag.data " + 
				" " + 
				"						from agendamento ag " + 
				" " + 
				"						join servico service on " + 
				" " + 
				"						service.id_agendamento = ag.id_agendamento " + 
				" " + 
				"						join pedido_servico pc on " + 
				" " + 
				"						pc.id_servico = service.id_servico " + 
				" " + 
				"						join pedido p on " + 
				" " + 
				"						p.id_pedido = pc.id_pedido "
				
				+ "						join servico ser on " + 
				
				" 						ser.id_agendamento = ag.id_agendamento "
				
				+ "						join usuario u on "
				+ "						u.id_usuario = ser.id_usuario " + 
				" " + 
				"						where data = :date and hora >= :hour " + 
				
				"						and ser.status is null "
				+ " 					and ser.data_criacao is null " + 
				 " 						and service.data_finalizacao is null " + 
				 " 						and u.id_usuario = :idUser " +
				 "						and pc.data_exclusao is null "+
				 "                      and p.tempo is not null "+
				"                       group by ag.hora "+
				" " + 
				"						order by ag.data,ag.hora asc " + 
				" " + 
				"						limit 1) count on " + 
				"						 " + 
				"			count.id_agendamento = a.id_agendamento) result2 " + 
				"           " + 
				" limit 1 ");
		sql.setString( "hour", time+":59" );
		sql.setString( "date", date );
		sql.setString("maxInterval", maxIntervalLocalDateTime);
		sql.setString("finalTime", finalTime);
		sql.setLong("idUser", idUser);
		List<R> res = sql.list();
		
		if(res.isEmpty())
		{
			return true;
		}
		
		final long result = ( ( Integer ) res.get( 0 ) ).longValue();
		
		entityManager.close();
		
		return result == 1 ? true : false;
		
		
	}

	@Override
	public List<IntervalHoursScheduling> getHoursByDate(String date, Usuario user) {
		
		Long idUser = user.getId();
		
		String sql = "select agen.hora as horaInicial,SEC_TO_TIME(SUM(TIME_TO_SEC(count.countTimeRequests) + TIME_TO_SEC( agen.hora ))) as horaFinal " + 
				" " + 
				"from agendamento agen " + 
				" " + 
				"join(select ag.id_agendamento as id_agendamento, SEC_TO_TIME( SUM( TIME_TO_SEC( p.tempo ) ) ) AS countTimeRequests  " + 
				"				    " + 
				"										from agendamento ag    " + 
				"				    " + 
				"										join servico service on    " + 
				"				    " + 
				"										service.id_agendamento = ag.id_agendamento    " + 
				"				    " + 
				"										join pedido_servico pc on    " + 
				"				    " + 
				"										pc.id_servico = service.id_servico    " + 
				"				    " + 
				"										join pedido p on    " + 
				"				    " + 
				"										p.id_pedido = pc.id_pedido    " + 
				
				"										join usuario u on "
				+ "										u.id_usuario = service.id_usuario " + 
				"				    " + 
				"										where data = :date   " + 
				"										and service.status is null "
				+ " 									and service.data_criacao is null " + 
				 " 										and service.data_finalizacao is null " +
				 " 										and u.id_usuario = :idUser " +
				 "										and pc.data_exclusao is null "+
				 "                      				and p.tempo is not null "+
				"				 " + 
				"				                       group by ag.hora  " + 
				"				    " + 
				"										order by ag.hora asc    " + 
				"				    " + 
				"										) count on    " + 
				"										    " + 
				"count.id_agendamento = agen.id_agendamento " + 
				" " + 
				"where data = :date " + 
				" " + 
				"group by agen.hora " + 
				" " + 
				"order by agen.hora asc;";
		
		EntityManager entityManager = this.em.createEntityManager();
		
		entityManager.getTransaction().begin();

		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("date", date)
		.setParameter("idUser", idUser)
		.unwrap(org.hibernate.query.Query.class)
		.setResultTransformer(Transformers.aliasToBean(IntervalHoursScheduling.class));
		
		List<IntervalHoursScheduling> result = query.getResultList();
		entityManager.flush();
		
		entityManager.getTransaction().commit();		
		entityManager.close();
		return result;
	}
	
	

	
}
