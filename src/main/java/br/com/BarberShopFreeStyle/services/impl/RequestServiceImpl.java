package br.com.BarberShopFreeStyle.services.impl;

import br.com.BarberShopFreeStyle.daos.PedidoTipoPerfilDao;
import br.com.BarberShopFreeStyle.daos.RequestDao;
import br.com.BarberShopFreeStyle.daos.RequestServiceDao;
import br.com.BarberShopFreeStyle.daos.TipoPerfilDao;
import br.com.BarberShopFreeStyle.dtos.StatusCrudDto;
import br.com.BarberShopFreeStyle.enums.StatusCrudEnum;
import br.com.BarberShopFreeStyle.enums.TypeInService;
import br.com.BarberShopFreeStyle.enums.TypeProfile;
import br.com.BarberShopFreeStyle.models.Pedido;
import br.com.BarberShopFreeStyle.models.PedidoServico;
import br.com.BarberShopFreeStyle.models.PedidoTipoPerfil;
import br.com.BarberShopFreeStyle.models.TipoPerfil;
import br.com.BarberShopFreeStyle.models.Usuario;
import br.com.BarberShopFreeStyle.services.RequestService;
import br.com.BarberShopFreeStyle.utils.Conversion;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.criteria.Predicate;

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

@Service
public class RequestServiceImpl<T>
	extends AbstractService
	implements RequestService
{

	/**
	 * <p>
	 * </p>
	 *
	 * @param nameRequest
	 * @param time
	 * @throws ParseException
	 * @see br.com.BarberShopFreeStyle.services.RequestService#add(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String add( final String nameRequest, final String time, final String valor, Usuario user, String[] specialitys, TypeInService isProduct )
			throws ParseException
		{

			final boolean notExists = this.pedidoDao.getByName( nameRequest, user,isProduct ).isEmpty();

			if ( !notExists )
			{
				return "exists";
			}

			final Pedido pedido = new Pedido();

			pedido.setNome( nameRequest );
			if(time != null)
			{
				pedido.setTempo( Conversion.convertTimeSql( time ) );
			}
			pedido.setPreco( valor );
			if(TypeInService.PRODUCT.equals(isProduct))
			{
				pedido.setProduto(true);
			}
			else
			{
				pedido.setProduto(false);
			}

			this.pedidoDao.insert( pedido );
			
			for(String type : specialitys)
			{
				TipoPerfil tipo = this.tipoPerfilDao.getByType(TypeProfile.valueOf(type));
				
				final PedidoTipoPerfil pedidoTipoPerfil = new PedidoTipoPerfil();
				
				pedidoTipoPerfil.setPedido(pedido);
				pedidoTipoPerfil.setTipoPerfil(tipo);
			
				pedidoTipoPerfilDao.insert(pedidoTipoPerfil);
			}

			return "success";

		}
	
	@Autowired
	private TipoPerfilDao tipoPerfilDao;
	
	@Autowired
	private PedidoTipoPerfilDao pedidoTipoPerfilDao;

	@Override
	public List<Pedido> all()
	{
		// TODO Auto-generated method stub
		return this.pedidoDao.all();
	}

	private Specification<T> buildSpecification()
	{
		return ( root, query, c ) -> {

			query.distinct( true );

			final Predicate conditions = c.and( c.isNull( root.get( "dataExclusao" ) ) );

			return conditions;

		};
	}

	@Override
	public void delete( final Pedido entity )
	{
		// TODO Auto-generated method stub
		entity.setDataExclusao( new Date() );
		this.pedidoDao.update( entity );

	}

	@Override
	public void deleteById( final Long id )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void downloadExcel( final List<Pedido> listPedido, final OutputStream outputStream )
		throws Exception
	{
		// TODO Auto-generated method stub
		int cell = 0;
		int column = 0;
		final XSSFWorkbook wb = new XSSFWorkbook();

		final List<String> titles = new ArrayList<String>();
		titles.add( "Nome" );
		titles.add( "Tempo" );

		boolean titleIsOK = false;
		XSSFSheet abaPrimaria = wb.createSheet( "Servicos" );

		for ( final Pedido pedido : listPedido )
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

			linha.createCell( column ).setCellValue( pedido.getNome() );
			column++;
			linha.createCell( column ).setCellValue( Conversion.convertToTimeString( pedido.getTempo() ) );
			// quando tiver futuras colunas
			// column++;
		}

		wb.write( outputStream );
		wb.close();

		outputStream.flush();
		outputStream.close();

	}

	@Override
	public void downloadPdf( final List<Pedido> listPedido, final OutputStream outputStream, Locale locale )
			throws IOException
		{
			

			StringBuilder joinTextBody = new StringBuilder();
			
			final List<String> titles = new ArrayList<String>();
			titles.add( "Nome" );
			titles.add( "Preço" );
			titles.add( "Tempo" );
			
			
			String titlePdf = "Pedidos";
			
			joinTextBody.append(buildHeadHtmlPdf(titlePdf,titles,locale));
			
			// columns
			for ( final Pedido pedido : listPedido )
			{
				joinTextBody
				.append(messageSource.getMessage("modelPdfBodyRow", new Object[]{}, locale))
				.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{pedido.getNome()}, locale))
				.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{pedido.getPreco()}, locale))
				.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{Conversion.convertToTimeString( pedido.getTempo() )}, locale))
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
	public Pedido getById( final Long id )
	{
		// TODO Auto-generated method stub
		return this.pedidoDao.getById( id );
	}

	@Override
	public List<PedidoTipoPerfil> getByName( final String name, final Usuario user,TypeInService isProduct )
	{
		return this.pedidoDao.getByName( name,user,isProduct );
	}

	@Override
	public List<Pedido> getListFromIds( final String[] requests )
	{
		// TODO Auto-generated method stub
		final List<Pedido> listRequest = new ArrayList<>();
		for ( final String idRequest : requests )
		{
			listRequest.add( this.pedidoDao.getById( Long.parseLong( idRequest.toString() ) ) );
		}

		return listRequest;
	}

	@Override
	public List<Pedido> getRequestByService( final Long idService )
	{
		// TODO Auto-generated method stub

		final List<PedidoServico> pedidoServicos = this.pedidoServiceDao.getRequestByService( idService );

		final List<Pedido> pedidos = new ArrayList<>();

		for ( final PedidoServico pedidoServico : pedidoServicos )
		{
			pedidos.add( pedidoServico.getPedido() );
		}

		return pedidos;
	}

	@Override
	public Page<Pedido> getRequests( final Integer pageNumber, final Integer pageSize )
	{
		// TODO Auto-generated method stub

		final Specification<T> specification = buildSpecification();

		return this.pedidoDao.listRequests( specification, pageNumber, pageSize );
	}

	@Override
	public Pedido insert( final Pedido entity )
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param idRequest
	 * @param name
	 * @param time
	 * @throws ParseException
	 * @see br.com.BarberShopFreeStyle.services.RequestService#update(java.lang.Long,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public List<StatusCrudDto> update( final Long idRequest, final String name, final String time, final String valor )
		throws ParseException
	{

		final List<StatusCrudDto> listStatus = new ArrayList<>();

		try
		{

			final Pedido pedido = this.pedidoDao.getById( idRequest );

			if(!pedido.isProduto())
			{
			
				if ( pedido.getNome().equals( name )
					&& pedido.getTempo().equals( Conversion.convertTimeSql( time ) )
					&& valor.equals( pedido.getPreco() ) )
				{
					listStatus.add( new StatusCrudDto( StatusCrudEnum.NO_CHANGE, null ) );
	
					return listStatus;
				}
			}
			else
			{
				if ( pedido.getNome().equals( name )
						&& valor.equals( pedido.getPreco() ) )
					{
						listStatus.add( new StatusCrudDto( StatusCrudEnum.NO_CHANGE, null ) );
		
						return listStatus;
					}
			}

			pedido.setNome( name );
			if(time != null)
			{
				pedido.setTempo( Conversion.convertTimeSql( time ) );
			}
			pedido.setPreco( valor );
			this.pedidoDao.update( pedido );

			listStatus.add( new StatusCrudDto( StatusCrudEnum.SUCCESS, null ) );
		}
		catch ( final Exception e )
		{
			listStatus.add( new StatusCrudDto( StatusCrudEnum.ERROR, e.getMessage() ) );
		}

		return listStatus;

	}

	@Override
	public Pedido update( final Pedido entity )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Autowired
	private RequestDao pedidoDao;

	@Autowired
	private RequestServiceDao pedidoServiceDao;

	@Override
	public Time getSumTimeRequestByIds(List<String> ids, String hourStr) throws ParseException {
		
		java.sql.Time initHour = Conversion.convertTimeSql(hourStr);
		
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(initHour);
		
		for(String id : ids)
		{
			Pedido pedido = this.pedidoDao.getById(Long.parseLong(id));
			if(!pedido.isProduto())
			{
				java.sql.Time timeByRequest = pedido.getTempo();
				
				Calendar cal2 = Calendar.getInstance();
				
				cal2.setTime(timeByRequest);
			
				cal.add(Calendar.HOUR_OF_DAY,cal2.get(Calendar.HOUR_OF_DAY));
				
				cal.add(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
			}
			
		}
		
		return new java.sql.Time(cal.getTime().getTime());
	}

}
