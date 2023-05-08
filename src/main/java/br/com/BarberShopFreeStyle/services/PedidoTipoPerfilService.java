package br.com.BarberShopFreeStyle.services;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;

import org.springframework.data.domain.Page;

import br.com.BarberShopFreeStyle.dtos.PedidoDto;
import br.com.BarberShopFreeStyle.models.PedidoTipoPerfil;

public interface PedidoTipoPerfilService 
	extends AbstractService<PedidoTipoPerfil,Long> {

	Page<PedidoDto> getRequests(Integer pageNumber, Integer pageSize, boolean isProduct);

	void downloadExcel(List<PedidoDto> list, ServletOutputStream outputStream) throws IOException;

	void downloadPdf(List<PedidoDto> list, ServletOutputStream outputStream, Locale locale) throws IOException;

	List<PedidoTipoPerfil> getTypeProfileByIdRequest(Long idRequest);

}
