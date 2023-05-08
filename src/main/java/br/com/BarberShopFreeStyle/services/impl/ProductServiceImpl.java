package br.com.BarberShopFreeStyle.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.itextpdf.html2pdf.HtmlConverter;

import br.com.BarberShopFreeStyle.dtos.PedidoDto;
import br.com.BarberShopFreeStyle.models.PedidoTipoPerfil;
import br.com.BarberShopFreeStyle.services.PedidoTipoPerfilService;
import br.com.BarberShopFreeStyle.services.ProductService;
import br.com.BarberShopFreeStyle.utils.Conversion;


@Service
public class ProductServiceImpl extends AbstractService
implements ProductService {

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
	public void downloadExcel(List<PedidoDto> content, ServletOutputStream outputStream) throws IOException {
		int cell = 0;
		int column = 0;
		final XSSFWorkbook wb = new XSSFWorkbook();

		final List<String> titles = new ArrayList<String>();
		titles.add( "Nome" );
		titles.add( "Preço" );

		boolean titleIsOK = false;
		XSSFSheet abaPrimaria = wb.createSheet( "Produtos" );

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
			linha.createCell( column ).setCellValue( pedidoTipoPerfil.getPreco() );
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
		
		
		String titlePdf = "Produtos";
		
		joinTextBody.append(buildHeadHtmlPdf(titlePdf,titles,locale));
		
		// columns
		for ( final PedidoDto pedidoUsuario : content )
		{
			joinTextBody
			.append(messageSource.getMessage("modelPdfBodyRow", new Object[]{}, locale))
			.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{pedidoUsuario.getNome()}, locale))
			.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{pedidoUsuario.getPreco()}, locale))
			.append(messageSource.getMessage("modelPdfBodyRowClose", new Object[]{}, locale));
		}

		joinTextBody.append(messageSource.getMessage("modelPdfBodyTableClose", new Object[]{}, locale));

		HtmlConverter.convertToPdf( joinTextBody.toString(), outputStream );
		outputStream.flush();
		outputStream.close();
		
	}

	
	
	@Autowired
	private MessageSource messageSource;

	

	

}
