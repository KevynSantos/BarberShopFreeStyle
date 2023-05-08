package br.com.BarberShopFreeStyle.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.itextpdf.html2pdf.HtmlConverter;

import br.com.BarberShopFreeStyle.daos.PedidoTipoPerfilDao;
import br.com.BarberShopFreeStyle.dtos.PedidoDto;
import br.com.BarberShopFreeStyle.models.Pedido;
import br.com.BarberShopFreeStyle.models.PedidoTipoPerfil;
import br.com.BarberShopFreeStyle.services.PedidoTipoPerfilService;
import br.com.BarberShopFreeStyle.utils.Conversion;

@Service
public class PedidoTipoPerfilServiceImpl<T> extends AbstractService
	implements PedidoTipoPerfilService
{

	@Override
	public List<PedidoTipoPerfil> all() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PedidoTipoPerfil getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PedidoTipoPerfil insert(PedidoTipoPerfil entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PedidoTipoPerfil update(PedidoTipoPerfil entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(PedidoTipoPerfil entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Page<PedidoDto> getRequests(Integer pageNumber, Integer pageSize, boolean isProduct) {
		// TODO Auto-generated method stub
		
		String query = buildQuery(isProduct);
		
		return pedidoTipoPerfilDao.getRequests(pageNumber,pageSize,query);
	}
	
	private String buildQuery(boolean isProduct)
	{
		String sql = "select p.nome as nome,p.tempo as tempo,p.preco as preco,(select count(*) from pedido_tipo_perfil where id_pedido = p.id_pedido) as tiposPerfis, p.id_pedido as idpedido " + 
				"	  from pedido_tipo_perfil ptp" + 
				"				" + 
				"	  join pedido p on " + 
				"	  p.id_pedido = ptp.id_pedido " + 
				"					" + 
				"	  join tipo_perfil tp on " + 
				"	  tp.id_tipo_perfil = ptp.id_tipo_perfil " + 
				"					" + 
				"	  where p.data_exclusao is null " + 
				"     and p.produto is "+String.valueOf(isProduct)+
				"		" + 
				"	  group by p.id_pedido ";
		
		return sql;
	}
	
	@Autowired
	private PedidoTipoPerfilDao pedidoTipoPerfilDao;

	@Override
	public void downloadExcel(List<PedidoDto> content, ServletOutputStream outputStream) throws IOException {
	
		int cell = 0;
		int column = 0;
		final XSSFWorkbook wb = new XSSFWorkbook();

		final List<String> titles = new ArrayList<String>();
		titles.add( "Nome" );
		titles.add( "Tempo" );
		titles.add( "Preço" );
		titles.add( "Quant. TipoPerfis" );

		boolean titleIsOK = false;
		XSSFSheet abaPrimaria = wb.createSheet( "Pedidos" );

		for ( final PedidoDto pedidoTipoPerfil : content )
		{
			if ( !titleIsOK )
			{
				final XSSFRow linha = abaPrimaria.createRow( cell );
				for ( final String title : titles )
				{
					final CellStyle style = wb.createCellStyle();
					style.setFillForegroundColor( HSSFColor.GREEN.index );
					linha.createCell( column ).setCellValue( title );
					linha.getCell( column ).setCellStyle( style );
					column++;
				}
				titleIsOK = true;

				abaPrimaria = adaptColumnsExcel( column, abaPrimaria );

				column = 0;
			}

			cell++;
			final XSSFRow linha = abaPrimaria.createRow( cell );
			column = 0;

			linha.createCell( column ).setCellValue( pedidoTipoPerfil.getNome() );
			column++;
			linha.createCell( column ).setCellValue( Conversion.convertToTimeString( pedidoTipoPerfil.getTempo()) );
			column++;
			linha.createCell( column ).setCellValue( pedidoTipoPerfil.getPreco() );
			column++;
			linha.createCell( column ).setCellValue( pedidoTipoPerfil.getTiposPerfis().toString() );
			// quando tiver futuras colunas
			// column++;
		}

		wb.write( outputStream );
		wb.close();

		outputStream.flush();
		outputStream.close();
	}

	@Override
	public void downloadPdf(List<PedidoDto> content, ServletOutputStream outputStream, Locale locale) throws IOException {
		
		StringBuilder joinTextBody = new StringBuilder();
		
		final List<String> titles = new ArrayList<String>();
		titles.add( "Nome" );
		titles.add( "Preço" );
		titles.add( "Tempo" );
		titles.add( "Quant. TipoPerfis" );
		
		
		String titlePdf = "Pedidos";
		
		joinTextBody.append(buildHeadHtmlPdf(titlePdf,titles,locale));
		
		// columns
		for ( final PedidoDto pedidoUsuario : content )
		{
			joinTextBody
			.append(messageSource.getMessage("modelPdfBodyRow", new Object[]{}, locale))
			.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{pedidoUsuario.getNome()}, locale))
			.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{pedidoUsuario.getPreco()}, locale))
			.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{Conversion.convertToTimeString( pedidoUsuario.getTempo() )}, locale))
			.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{pedidoUsuario.getTiposPerfis().toString()}, locale))
			.append(messageSource.getMessage("modelPdfBodyRowClose", new Object[]{}, locale));
		}

		joinTextBody.append(messageSource.getMessage("modelPdfBodyTableClose", new Object[]{}, locale));

		HtmlConverter.convertToPdf( joinTextBody.toString(), outputStream );
		outputStream.flush();
		outputStream.close();
	}
	
	@Autowired
	private MessageSource messageSource;

	@Override
	public List<PedidoTipoPerfil> getTypeProfileByIdRequest(Long idRequest) {
		// TODO Auto-generated method stub
		return this.pedidoTipoPerfilDao.getTypeProfileByIdRequest(idRequest);
	}

}
