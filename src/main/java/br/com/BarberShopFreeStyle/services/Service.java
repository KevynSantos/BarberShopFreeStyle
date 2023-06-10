package br.com.BarberShopFreeStyle.services;

import br.com.BarberShopFreeStyle.enums.StatusService;
import br.com.BarberShopFreeStyle.models.Pedido;
import br.com.BarberShopFreeStyle.models.Servico;
import br.com.BarberShopFreeStyle.models.Usuario;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.Page;

public interface Service
	extends AbstractService<Servico, Long>
{

	Servico add( String[] pedidos, String cpf, final String status, final String descricao, final Usuario user );

	void addRequetsForService( Servico service, List<Pedido> requests );

	void delete( Long idService );

	void downloadExcel( final List<Servico> requests, final OutputStream outputStream )
		throws IOException;

	void downloadPdf( final List<Servico> requests, final OutputStream outputStream, Locale locale )
		throws IOException;

	List<br.com.BarberShopFreeStyle.models.Servico> getByIdClient( Long id );

	Page<Servico> getResultPage(
		Integer pageNumber,
		Integer pageSize,
		String pedido,
		String cpf,
		String dataInicial,
		String dataFinal,
		String status,
		String horaInicial,
		String horaFinal,
		Usuario user,
		String cpfEmployee,
		String speciality, boolean isClient )
		throws Exception;

	void update( Long serviceId, String cpf, StatusService status );


	
	

}
