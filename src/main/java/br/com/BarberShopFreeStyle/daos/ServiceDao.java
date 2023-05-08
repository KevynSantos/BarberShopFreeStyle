package br.com.BarberShopFreeStyle.daos;

import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import br.com.BarberShopFreeStyle.models.Servico;
import br.com.BarberShopFreeStyle.models.Usuario;

public interface ServiceDao extends AbstractDao<Servico,Long> {
	
	Page<Servico> listServices(Specification specs, Integer pageSize, Integer pageNumber);
	
	List<br.com.BarberShopFreeStyle.models.Servico> getByIdClient(Long id);
	
	List<Servico> listServicesByUserFor30Days(Usuario usuario) throws ParseException;
	

}
