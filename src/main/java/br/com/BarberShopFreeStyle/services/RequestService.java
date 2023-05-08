package br.com.BarberShopFreeStyle.services;

import br.com.BarberShopFreeStyle.dtos.StatusCrudDto;
import br.com.BarberShopFreeStyle.enums.TypeInService;
import br.com.BarberShopFreeStyle.models.Pedido;
import br.com.BarberShopFreeStyle.models.PedidoTipoPerfil;
import br.com.BarberShopFreeStyle.models.Usuario;

import java.io.OutputStream;
import java.sql.Time;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.Page;

public interface RequestService
	extends AbstractService<Pedido, Long>
{

	String add( String nameRequest, String time, String valor, Usuario user, String[] specialitys, TypeInService isProduct )
		throws ParseException;

	void downloadExcel( List<Pedido> listPedido, OutputStream outputStream )
		throws Exception;

	void downloadPdf( final List<Pedido> listPedido, final OutputStream outputStream, Locale locale )
		throws Exception;

	List<PedidoTipoPerfil> getByName( String name, Usuario user, TypeInService isProduct );

	List<Pedido> getListFromIds( String[] requests );

	List<Pedido> getRequestByService( Long idService );

	Page<Pedido> getRequests( Integer pageNumber, Integer pageSize );

	/**
	 * <p>
	 * </p>
	 *
	 * @param idRequest
	 * @param name
	 * @param time
	 * @throws ParseException
	 */
	List<StatusCrudDto> update( Long idRequest, String name, String time, String valor )
		throws ParseException;

	Time getSumTimeRequestByIds(List<String> ids, String hour) throws ParseException;

}
