package br.com.BarberShopFreeStyle.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import br.com.BarberShopFreeStyle.dtos.ClienteDto;
import br.com.BarberShopFreeStyle.models.Cliente;

public interface ClientDao extends AbstractDao<Cliente,Long> {
	
	Cliente getByCPF(String cpf);
	
	Page<Cliente> listClients(Specification specs, Integer pageNumber, Integer pageSize);

	ClienteDto getClientLikeCpf(String cpf);

	ClienteDto getClientLikeNome(String nome);

}
