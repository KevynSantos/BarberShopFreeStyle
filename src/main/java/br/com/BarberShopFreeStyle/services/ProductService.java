package br.com.BarberShopFreeStyle.services;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;

import br.com.BarberShopFreeStyle.dtos.PedidoDto;
import br.com.BarberShopFreeStyle.models.PedidoTipoPerfil;

public interface ProductService extends AbstractService<PedidoTipoPerfil,Long> {

	void downloadExcel(List<PedidoDto> list, ServletOutputStream outputStream) throws IOException;

	void downloadPdf(List<PedidoDto> list, ServletOutputStream outputStream, Locale locale) throws IOException;
	
}
