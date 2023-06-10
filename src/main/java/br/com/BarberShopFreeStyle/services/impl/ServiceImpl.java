package br.com.BarberShopFreeStyle.services.impl;

import br.com.BarberShopFreeStyle.daos.ServiceDao;
import br.com.BarberShopFreeStyle.enums.FormatImages;
import br.com.BarberShopFreeStyle.enums.StatusService;
import br.com.BarberShopFreeStyle.enums.TypeProfile;
import br.com.BarberShopFreeStyle.models.Cliente;
import br.com.BarberShopFreeStyle.models.Pedido;
import br.com.BarberShopFreeStyle.models.PedidoServico;
import br.com.BarberShopFreeStyle.models.Servico;
import br.com.BarberShopFreeStyle.models.Usuario;
import br.com.BarberShopFreeStyle.services.ClientService;
import br.com.BarberShopFreeStyle.services.RequestService;
import br.com.BarberShopFreeStyle.services.RequestServiceService;
import br.com.BarberShopFreeStyle.utils.Conversion;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.persistence.criteria.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.html2pdf.HtmlConverter;

@Service
public class ServiceImpl<T>
	extends AbstractService
	implements br.com.BarberShopFreeStyle.services.Service
{

	private static final Logger LOG = LogManager.getLogger( "ServiceImpl" );

	@Override
	public Servico add(
		final String[] pedidos,
		final String cpf,
		final String status,
		final String descricao,
		final Usuario user )
	{
		// TODO Auto-generated method stub
		final Servico service = new Servico();

		final Cliente client = this.clientService.getByCPF( cpf );

		final List<Pedido> requests = this.requestService.getListFromIds( pedidos );

		service.setAgendamento( null );
		service.setDescricaoServico( descricao );
		service.setStatus( StatusService.valueOf( status ) );
		service.setUsuario( user );
		service.setCliente( client );
		service.setDataCriacao( new Date() );

		this.servicoDao.insert( service );

		addRequetsForService( service, requests );

		return service;
	}

	@Override
	@Transactional( propagation = Propagation.REQUIRED )
	public void addRequetsForService( final Servico service, final List<Pedido> requests )
	{
		for ( final Pedido request : requests )
		{
			final PedidoServico pedidoServico = new PedidoServico();

			pedidoServico.setPedido( request );
			pedidoServico.setIdServico( service.getId() );

			this.requestServiceService.insert( pedidoServico );

		}
	}

	@Override
	public List<Servico> all()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	private Specification<T> buildSpecificationApp(
		Cliente client,String dataInicial,String dataFinal,String horaInicial, String horaFinal)
	{
		return ( root, query, c ) -> {

			query.distinct( true );

			Predicate conditions = c.conjunction();
			
			conditions = c.and( conditions, c.equal( root.get( "cliente" ).get( "cpf" ), client.getCpf() ) );
			
			conditions = c.and( conditions, c.isNull( root.get( "dataFinalizacao" ) ) );

			if ( ( dataInicial != "" && dataInicial != null ) && ( dataFinal != "" && dataFinal != null ) )
			{
				try
				{
					final Date convertDate1 = Conversion.convertDate( dataInicial );
					final Date convertDate2 = Conversion.convertDate( dataFinal );
					final Calendar calendar = Conversion.dateToCalendar( convertDate2 );

					calendar.set( Calendar.HOUR_OF_DAY, 23 );
					calendar.set( Calendar.MINUTE, 59 );
					calendar.set( Calendar.SECOND, 59 );
					calendar.set( Calendar.MILLISECOND, 999 );

					final Date date2 = Conversion.calendarToDate( calendar );

					conditions = c
						.and(
							conditions,
							c.between( root.join( "agendamento" ).get( "data" ), convertDate1, date2 ) );
					

				}
				catch ( final ParseException e )
				{

					LOG.error( e );
				}
			}
			
			if ( ( ( horaInicial != "" ) && ( horaInicial != null ) )
							&& ( ( horaFinal != "" ) && ( horaFinal != null ) ) )
			{
				try
				{
					conditions = c
						.and(
							conditions,
							c
								.and(
									c
										.greaterThanOrEqualTo(
											root.join( "agendamento" ).get( "hora" ),
											Conversion.convertTimeSql( horaInicial ) ),
									c
										.lessThanOrEqualTo(
											root.join( "agendamento" ).get( "hora" ),
											Conversion.convertTimeSql( horaFinal ) ) ) );
				}
				catch ( final ParseException e )
				{
	
					LOG.error( e );
				}
	
			}

			return conditions;

		};
	}

	private Specification<T> buildSpecification(
		final String pedido,
		final String cpf,
		final String dataInicial,
		final String dataFinal,
		final String status,
		final String horaInicial,
		final String horaFinal,
		final Usuario user,
		final String cpfEmployee,
		final TypeProfile typeProfile )
	{
		return ( root, query, c ) -> {

			query.distinct( true );

			Predicate conditions = c.conjunction();

			if ( pedido != "" && pedido != null )
			{
				conditions = c
					.and( conditions, c.equal( root.join( "pedidoServico" ).get( "pedido" ).get( "nome" ), pedido ) );
			}

			if ( cpf != "" && cpf != null )
			{
				conditions = c.and( conditions, c.equal( root.get( "cliente" ).get( "cpf" ), cpf ) );
			}

			if ( ( dataInicial != "" && dataInicial != null ) && ( dataFinal != "" && dataFinal != null ) )
			{
				try
				{
					final Date convertDate1 = Conversion.convertDate( dataInicial );
					final Date convertDate2 = Conversion.convertDate( dataFinal );
					final Calendar calendar = Conversion.dateToCalendar( convertDate2 );

					calendar.set( Calendar.HOUR_OF_DAY, 23 );
					calendar.set( Calendar.MINUTE, 59 );
					calendar.set( Calendar.SECOND, 59 );
					calendar.set( Calendar.MILLISECOND, 999 );

					final Date date2 = Conversion.calendarToDate( calendar );

					if ( status == null || status == "" )
					{
						conditions = c
							.and(
								conditions,
								c.between( root.join( "agendamento" ).get( "data" ), convertDate1, date2 ) );
					}

					else
					{
						conditions = c.and( conditions, c.between( root.get( "dataCriacao" ), convertDate1, date2 ) );
					}

				}
				catch ( final ParseException e )
				{

					LOG.error( e );
				}
			}

			if ( ( status != "" ) && ( status != null ) )
			{
				conditions = c.and( conditions, c.equal( root.get( "status" ), StatusService.valueOf( status ) ) );
			}

			if ( status == null || status == "" )
			{
				query
					.orderBy(
						c.desc( root.join( "agendamento" ).get( "data" ) ),
						c.desc( root.join( "agendamento" ).get( "hora" ) ) );

				conditions = c.and( conditions, c.isNull( root.get( "dataCriacao" ) ) );

				conditions = c.and( conditions, c.isNull( root.get( "status" ) ) );
			}

			else
			{
				query.orderBy( c.desc( root.get( "dataCriacao" ) ) );
				conditions = c.and( conditions, c.isNotNull( root.get( "status" ) ) );
				conditions = c.and( conditions, c.isNotNull( root.get( "dataCriacao" ) ) );
			}

			if ( ( ( horaInicial != "" ) && ( horaInicial != null ) )
				&& ( ( horaFinal != "" ) && ( horaFinal != null ) ) )
			{
				try
				{
					conditions = c
						.and(
							conditions,
							c
								.and(
									c
										.greaterThanOrEqualTo(
											root.join( "agendamento" ).get( "hora" ),
											Conversion.convertTimeSql( horaInicial ) ),
									c
										.lessThanOrEqualTo(
											root.join( "agendamento" ).get( "hora" ),
											Conversion.convertTimeSql( horaFinal ) ) ) );
				}
				catch ( final ParseException e )
				{

					LOG.error( e );
				}

			}

			conditions = c.and( conditions, c.isNull( root.get( "dataFinalizacao" ) ) );

			final TypeProfile profileType = user.getTipoPerfil().getTipo();

			if ( !TypeProfile.ADMINISTRADOR.equals( profileType ) )
			{
				conditions = c.and( conditions, c.equal( root.get( "usuario" ).get( "id" ), user.getId() ) );
			}

			if ( typeProfile != null )
			{
				conditions = c
					.and(
						conditions,
						c.equal( root.join( "usuario" ).join( "tipoPerfil" ).get( "tipo" ), typeProfile ) );
			}

			if ( ( cpfEmployee != null ) && ( cpfEmployee != "" ) )
			{
				conditions = c
					.and(
						conditions,
						c.equal( root.join( "usuario" ).join( "funcionario" ).get( "cpf" ), cpfEmployee ) );
			}

			return conditions;

		};
	}

	@Override
	public void delete( final Long idService )
	{
		// TODO Auto-generated method stub

		final Servico servico = this.servicoDao.getById( idService );
		servico.setDataFinalizacao( new Date() );
		this.servicoDao.update( servico );

		// this.servicoDao.deleteById(idService);

	}

	@Override
	public void delete( final Servico entity )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById( final Long id )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void downloadExcel( final List<Servico> requests, final OutputStream outputStream )
		throws IOException
	{
		// TODO Auto-generated method stub

		int cell = 0;
		int column = 0;
		final XSSFWorkbook wb = new XSSFWorkbook();

		final List<String> titles = new ArrayList<String>();
		titles.add( "NomeCliente" );
		titles.add( "Valor" );
		titles.add( "CPF" );
		titles.add( "DataAbertura" );
		titles.add( "Status" );

		boolean titleIsOK = false;
		XSSFSheet abaPrimaria = wb.createSheet( "Servicos" );

		for ( final Servico currentRequest : requests )
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

			linha.createCell( column ).setCellValue( currentRequest.getCliente().getNome() );
			column++;
			linha.createCell( column ).setCellValue( calculateMoneyTotalRequests(currentRequest.getPedidoServico()) );
			column++;
			linha.createCell( column ).setCellValue( currentRequest.getCliente().getCpf() );
			column++;
			linha
				.createCell( column )
				.setCellValue( Conversion.convertToDateTimeString( currentRequest.getDataCriacao() ) );
			column++;
			linha.createCell( column ).setCellValue( translateStatus( currentRequest.getStatus() ) );
			// quando tiver futuras colunas
			// column++;
		}

		wb.write( outputStream );
		wb.close();

		outputStream.flush();
		outputStream.close();

	}

	@Override
	public void downloadPdf( final List<Servico> services, final OutputStream outputStream, Locale locale )
		throws IOException
	{
		

		StringBuilder joinTextBody = new StringBuilder();
		
		final List<String> titles = new ArrayList<String>();
		titles.add( "NomeCliente" );
		titles.add( "Valor" );
		titles.add( "CPF" );
		titles.add( "DataAbertura" );
		titles.add( "Status" );
		
		String titlePdf = "Serviços";
		
		joinTextBody.append(buildHeadHtmlPdf(titlePdf,titles,locale));
		
		// columns
		for ( final Servico servico : services )
		{
			joinTextBody
			.append(messageSource.getMessage("modelPdfBodyRow", new Object[]{}, locale))
			.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{servico.getCliente().getNome()}, locale))
			.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{calculateMoneyTotalRequests(servico.getPedidoServico())}, locale))
			.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{servico.getCliente().getCpf()}, locale))
			.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{Conversion.convertToDateTimeString( servico.getDataCriacao() )}, locale))
			.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{translateStatus( servico.getStatus() )}, locale))
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
	public Servico getById( final Long id )
	{
		// TODO Auto-generated method stub
		return this.servicoDao.getById( id );
	}

	@Override
	public List<br.com.BarberShopFreeStyle.models.Servico> getByIdClient( final Long id )
	{
		// TODO Auto-generated method stub
		return this.servicoDao.getByIdClient( id );
	}

	@Override
	public Page<Servico> getResultPage(
		final Integer pageNumber,
		final Integer pageSize,
		final String pedido,
		final String cpf,
		final String dataInicial,
		final String dataFinal,
		final String status,
		final String horaInicial,
		final String horaFinal,
		final Usuario user,
		final String cpfEmployee,
		final String speciality,
		final boolean isClient)
		throws Exception
	{

		final TypeProfile typeProfile = TypeProfile.getEnum( speciality );
		final Specification<T> specification;
		
		if(isClient)
		{
			specification = buildSpecificationApp(user.getCliente(),dataInicial,dataFinal,horaInicial,horaFinal);
		}
		else
		{
			specification = buildSpecification(
				pedido,
				cpf,
				dataInicial,
				dataFinal,
				status,
				horaInicial,
				horaFinal,
				user,
				cpfEmployee,
				typeProfile );
		}
		
		// TODO Auto-generated method stub

		return this.servicoDao.listServices( specification, pageNumber, pageSize );
	}

	@Override
	public Servico insert( final Servico entity )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update( final Long serviceId, final String cpf, final StatusService status )
	{
		// TODO Auto-generated method stub

		final Servico servico = this.servicoDao.getById( serviceId );
		final Cliente cliente = this.clientService.getByCPF( cpf );

		servico.setCliente( cliente );
		servico.setStatus( status );

		this.servicoDao.update( servico );

	}

	@Override
	public Servico update( final Servico entity )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Autowired
	private ClientService clientService;

	@Autowired
	private RequestService requestService;

	@Autowired
	private RequestServiceService requestServiceService;

	@Autowired
	private ServiceDao servicoDao;
	


	

	

}


