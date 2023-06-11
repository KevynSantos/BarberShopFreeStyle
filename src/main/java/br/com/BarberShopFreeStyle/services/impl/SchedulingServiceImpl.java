package br.com.BarberShopFreeStyle.services.impl;

import br.com.BarberShopFreeStyle.daos.SchedulingDao;
import br.com.BarberShopFreeStyle.daos.ServiceDao;
import br.com.BarberShopFreeStyle.dtos.IntervalHoursScheduling;
import br.com.BarberShopFreeStyle.dtos.StatusCrudDto;
import br.com.BarberShopFreeStyle.dtos.app.ListagemAgendamentosApp;
import br.com.BarberShopFreeStyle.enums.IntervalStatus;
import br.com.BarberShopFreeStyle.enums.StatusCrudEnum;
import br.com.BarberShopFreeStyle.enums.StatusService;
import br.com.BarberShopFreeStyle.enums.UpdateProductStatus;
import br.com.BarberShopFreeStyle.models.Agendamento;
import br.com.BarberShopFreeStyle.models.Cliente;
import br.com.BarberShopFreeStyle.models.Pedido;
import br.com.BarberShopFreeStyle.models.PedidoServico;
import br.com.BarberShopFreeStyle.models.Servico;
import br.com.BarberShopFreeStyle.models.Usuario;
import br.com.BarberShopFreeStyle.services.ClientService;
import br.com.BarberShopFreeStyle.services.RequestService;
import br.com.BarberShopFreeStyle.services.SchedulingService;
import br.com.BarberShopFreeStyle.services.SimpleEmailService;
import br.com.BarberShopFreeStyle.utils.Conversion;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.html2pdf.HtmlConverter;

@Service
@EnableScheduling
public class SchedulingServiceImpl
	extends AbstractService
	implements SchedulingService
{

	private static final Log LOG = LogFactory.getLog( SchedulingServiceImpl.class );

	@Override
	@Transactional
	public List<StatusCrudDto> addScheduling(
		final String cpf,
		final String[] pedidos,
		final String descricao,
		final String data,
		final String hora,
		final Usuario usuario )
		throws ParseException
	{
		// TODO Auto-generated method stub

		final List<StatusCrudDto> result = new ArrayList<>();

		try
		{
			final List<Pedido> requests = this.requestService.getListFromIds( pedidos );

			if ( requests.stream().noneMatch( request -> !request.isProduto() ) )
			{

				result.add( new StatusCrudDto( StatusCrudEnum.ONLY_PRODUCT_IN_SCHEDULING, null ) );
				return result;

			}

			if ( !checkintervalRangeTimeRequests( data, hora, requests, usuario, IntervalStatus.INSERT, null ) )
			{
				result.add( new StatusCrudDto( StatusCrudEnum.SCHEDULING_INVALID, null ) );
				return result;
			}

			final Servico servico = new Servico();

			final Cliente client = this.clientService.getByCPF( cpf );

			final Agendamento agendamento = new Agendamento();
			agendamento.setData( Conversion.convertDateSql( data ) );
			agendamento.setHora( Conversion.convertTimeSql( hora ) );
			agendamento.setDtAbertura( new Date() );

			this.schedulingDao.insert( agendamento );

			servico.setAgendamento( agendamento );
			servico.setDescricaoServico( descricao );
			servico.setUsuario( usuario );
			servico.setCliente( client );

			this.serviceDao.insert( servico );

			this.service.addRequetsForService( servico, requests );

			result.add( new StatusCrudDto( StatusCrudEnum.SUCCESS, null ) );
		}
		catch ( final Exception e )
		{
			LOG.error( e,e );
			result.add( new StatusCrudDto( StatusCrudEnum.ERROR, e.getMessage() ) );
		}

		return result;

	}

	@Override
	public List<Agendamento> all()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @return
	 * @see br.com.BarberShopFreeStyle.services.SchedulingService#buildDtoForListSchedulesInApp()
	 */
	@Override
	public List<ListagemAgendamentosApp> buildDtoForListSchedulesInApp( final Page<Servico> page )
	{
		final List<ListagemAgendamentosApp> newList = new ArrayList<>();
		final List<Servico> list = page.getContent();

		list.forEach( servico -> {

			final ListagemAgendamentosApp dto = new ListagemAgendamentosApp();

			dto.setId( servico.getId() );
			dto.setData( Conversion.convertToDateString( servico.getAgendamento().getData() ) );
			dto.setHora( Conversion.convertToTimeString( servico.getAgendamento().getHora() ) );
			final String[] name = servico.getUsuario().getFuncionario().getNome().split( " " );
			dto.setNomeFuncionario( name[0] );

			final List<String> pedidos = new ArrayList<>();

			servico.getPedidoServico().forEach( pedido -> {

				pedidos.add( pedido.getPedido().getNome() );

			} );

			dto.setPedidos( pedidos );

			newList.add( dto );

		} );

		return newList;
	}

	private String buildTableRequest( final List<PedidoServico> list )
	{
		String table = "";

		for ( final PedidoServico pedidoServico : list )
		{
			final Pedido pedido = pedidoServico.getPedido();

			String tempoStr = "N/A";

			if ( pedido.getTempo() != null )
			{
				tempoStr = Conversion.convertToTimeStringEmail( pedido.getTempo() );
			}

			table += "<tr><td>" + pedido.getNome() + "</td><td>" + tempoStr + "</td></tr>";
		}

		return table;
	}

	@Override
	public String calculateTime( final String date )
	{
		// TODO Auto-generated method stub
		final LocalDate ld = LocalDate.parse( date );

		return ld.getDayOfWeek().toString();

	}

	private String calculateTotalTimeRequests(
		final String data,
		final String initialTime,
		final List<Pedido> listRequests )
	{
		Long totalMinutes = ( long ) 0;
		Long totalHours = ( long ) 0;

		for ( final Pedido pedido : listRequests )
		{

			if ( !pedido.isProduto() )
			{
				final TimeZone zone = TimeZone.getTimeZone( "GMT-03:00" );
				final Locale locale = new Locale( "pt", "BR" );
				final Calendar calendar = Calendar.getInstance( zone, locale );

				final java.sql.Time tempoDoPedido = pedido.getTempo();

				calendar.setTime( tempoDoPedido );

				totalMinutes = totalMinutes + ( calendar.get( Calendar.MINUTE ) );
				totalHours = totalHours + ( calendar.get( Calendar.HOUR_OF_DAY ) );
			}

		}

		LocalDateTime maxIntervalLocalDateTime = LocalDateTime
			.of(
				java.sql.Date.valueOf( data ).toLocalDate(),
				java.sql.Time.valueOf( initialTime + ":00" ).toLocalTime() );

		maxIntervalLocalDateTime = maxIntervalLocalDateTime.plusMinutes( totalMinutes );

		maxIntervalLocalDateTime = maxIntervalLocalDateTime.plusHours( totalHours );

		final Time maxTimeSql = Conversion.converToTimeSql( maxIntervalLocalDateTime );

		final String maxTimeStr = Conversion.convertToTimeString( maxTimeSql );

		return maxTimeStr;
	}

	@Override
	public void checkin( final Agendamento agendamento )
	{
		agendamento.setCheckin( new Date() );
		this.schedulingDao.update( agendamento );
	}

	@Override
	public boolean checkintervalRangeTimeRequests(
		final String data,
		final String initialTime,
		final List<Pedido> listRequests,
		final Usuario usuario,
		final IntervalStatus intervalStatus,
		final Servico servico )
		throws ParseException
	{
		// TODO Auto-generated method stub

		final String maxTimeStr = calculateTotalTimeRequests( data, initialTime, listRequests );

		List<UpdateProductStatus> updateProductStatus = null;

		if ( IntervalStatus.ADD_REQUEST.equals( intervalStatus ) )
		{
			updateProductStatus = checkUpdateRequestOrProduct( listRequests, servico );
		}

		final boolean result = this.schedulingDao
			.checkintervalRangeTimeRequests(
				data,
				initialTime,
				maxTimeStr,
				usuario,
				intervalStatus,
				updateProductStatus );

		return result;
	}

	private List<UpdateProductStatus> checkUpdateRequestOrProduct(
		final List<Pedido> newlistRequests,
		final Servico servico )
	{

		final List<Pedido> requestsContent = getRequestByRequestsServices( servico.getPedidoServico() );

		final List<UpdateProductStatus> notContent = new ArrayList<>();

		for ( final Pedido pedido : newlistRequests )
		{

			if ( requestsContent.stream().noneMatch( request -> request.getId().equals( pedido.getId() ) ) )
			{

				if ( pedido.isProduto() )
				{
					notContent.add( UpdateProductStatus.NEW_PRODUCT );
				}
				else
				{
					notContent.add( UpdateProductStatus.NEW_TIME );
				}

			}

		}

		return notContent;
	}

	@Override
	public void delete( final Agendamento entity )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById( final Long id )
	{
		// TODO Auto-generated method stub
		this.schedulingDao.delete( Integer.parseInt( id.toString() ) );

	}

	@Override
	public void downloadExcel( final List<Servico> schedulings, final ServletOutputStream outputStream )
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
		titles.add( "Data" );
		titles.add( "Hora" );
		boolean titleIsOK = false;
		XSSFSheet abaPrimaria = wb.createSheet( "Servicos" );

		for ( final Servico currentRequest : schedulings )
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
			linha.createCell( column ).setCellValue( calculateMoneyTotalRequests( currentRequest.getPedidoServico() ) );
			column++;
			linha.createCell( column ).setCellValue( currentRequest.getCliente().getCpf() );
			column++;
			linha
				.createCell( column )
				.setCellValue( Conversion.convertToDateString( currentRequest.getAgendamento().getData() ) );
			column++;
			linha
				.createCell( column )
				.setCellValue( Conversion.convertToTimeString( currentRequest.getAgendamento().getHora() ) );
			// quando tiver futuras colunas
			// column++;
		}

		wb.write( outputStream );
		wb.close();

		outputStream.flush();
		outputStream.close();
	}

	@Override
	public void downloadPdf( final List<Servico> services, final OutputStream outputStream, final Locale locale )
		throws IOException
	{

		final StringBuilder joinTextBody = new StringBuilder();

		final List<String> titles = new ArrayList<String>();
		titles.add( "NomeCliente" );
		titles.add( "Valor" );
		titles.add( "CPF" );
		titles.add( "Data" );
		titles.add( "Hora" );

		final String titlePdf = "Agendamentos";

		joinTextBody.append( buildHeadHtmlPdf( titlePdf, titles, locale ) );

		// columns
		for ( final Servico servico : services )
		{
			joinTextBody
				.append( this.messageSource.getMessage( "modelPdfBodyRow", new Object[]{}, locale ) )
				.append(
					this.messageSource
						.getMessage( "modelPdfBodyColumn", new Object[]{servico.getCliente().getNome()}, locale ) )
				.append(
					this.messageSource
						.getMessage(
							"modelPdfBodyColumn",
							new Object[]{calculateMoneyTotalRequests( servico.getPedidoServico() )},
							locale ) )
				.append(
					this.messageSource
						.getMessage( "modelPdfBodyColumn", new Object[]{servico.getCliente().getCpf()}, locale ) )
				.append(
					this.messageSource
						.getMessage(
							"modelPdfBodyColumn",
							new Object[]{Conversion.convertToDateString( servico.getAgendamento().getData() )},
							locale ) )
				.append(
					this.messageSource
						.getMessage(
							"modelPdfBodyColumn",
							new Object[]{Conversion.convertToTimeString( servico.getAgendamento().getHora() )},
							locale ) )
				.append( this.messageSource.getMessage( "modelPdfBodyRowClose", new Object[]{}, locale ) );
		}

		joinTextBody.append( this.messageSource.getMessage( "modelPdfBodyTableClose", new Object[]{}, locale ) );

		HtmlConverter.convertToPdf( joinTextBody.toString(), outputStream );
		outputStream.flush();
		outputStream.close();
	}

	@Override
	public Agendamento getById( final Long id )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IntervalHoursScheduling> getHoursByDate( final String date, final Usuario user )
	{
		// TODO Auto-generated method stub
		return this.schedulingDao.getHoursByDate( date, user );
	}

	private List<Pedido> getRequestByRequestsServices( final List<PedidoServico> list )
	{
		final List<Pedido> newList = new ArrayList<>();

		for ( final PedidoServico pedidoServico : list )
		{
			newList.add( pedidoServico.getPedido() );
		}

		return newList;
	}

	@Override
	public Agendamento insert( final Agendamento entity )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void scheduledFinal( final Servico servico )
	{
		servico.setStatus( StatusService.DONE );
		servico.setDataCriacao( new Date() );
		this.serviceDao.update( servico );

	}

	@Scheduled( cron = "${cron.scheduling.mail.day}" )
	public void sendMessageDayScheduling()
	{

		final List<Servico> list = this.schedulingDao.getSchedulingByDayNow();

		if ( list != null )
		{
			for ( final Servico servico : list )
			{
				final Cliente client = servico.getCliente();

				final String subject = "Hoje é o dia do atendimento " + client.getNome() + " [BarberShopFreeStyle]";

				final String bodyHtml = "<body>"
					+ client.getNome()
					+ ", o seu agendamento esta marcado para hoje, às "
					+ Conversion.convertToTimeStringEmail( servico.getAgendamento().getHora() )
					+ ".<br>"
					+ "<h4>Esse é o seu pedido:</h4>"
					+ "<table border=2>"
					+ "<tr><th>Pedido</th><th>Tempo</th></tr>"
					+

					buildTableRequest( servico.getPedidoServico() )

					+ "</table>"

					+ "<br> <p>Funcionário(a): "
					+ servico.getUsuario().getFuncionario().getNome()
					+ "</p>"

					+ "<p>Valor: "
					+ calculateMoneyTotalRequests( servico.getPedidoServico() )
					+ "</p>"

					+ "<br> Até logo! Abs. BarberShopFreeStyle</body>";

				final String[] contacts = new String[1];

				contacts[0] = client.getEmail();

				try
				{
					this.simpleEmailService.sendEmail( bodyHtml, subject, contacts );
					final Agendamento agendamento = servico.getAgendamento();
					agendamento.setNotificacao( true );
					this.schedulingDao.update( agendamento );
				}
				catch ( UnsupportedEncodingException | MessagingException e )
				{
					LOG
						.error(
							"Ocorreu um erro ao tentar enviar o email para esse destinatário:"
								+ client.getEmail()
								+ e.getMessage() );
				}

			}
		}

	}

	@Override
	public Agendamento update( final Agendamento entity )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatusCrudDto update(
		final Long serviceId,
		final String cpf,
		final String data,
		final String hora,
		final Usuario usuario )
		throws ParseException
	{
		StatusCrudDto status = null;
		try
		{

			final DateFormat formatDate = new SimpleDateFormat( "yyyy-MM-dd", Locale.US );
			formatDate.setTimeZone( TimeZone.getDefault() );

			final java.util.Date date = formatDate.parse( data );

			final java.sql.Date sqlDate = new java.sql.Date( date.getTime() );

			final DateFormat formatHour = new SimpleDateFormat( "HH:mm:ss", Locale.US );
			final DateFormat formatHourToVerification = new SimpleDateFormat( "HH:mm", Locale.US );
			formatHour.setTimeZone( TimeZone.getDefault() );

			final java.util.Date hour = formatHour.parse( hora );

			final java.sql.Time sqlHour = new java.sql.Time( hour.getTime() );

			final Servico service = this.service.getById( serviceId );

			final List<Pedido> requests = new ArrayList<>();

			for ( final PedidoServico pedidoServico : service.getPedidoServico() )
			{
				requests.add( pedidoServico.getPedido() );
			}

			final String maxIntervalLocalDateTime = calculateTotalTimeRequests(
				data,
				formatHourToVerification.format( formatHourToVerification.parse( hora ) ),
				requests );

			if ( !this.schedulingDao
				.checkintervalRangeTimeRequests(
					data,
					formatHourToVerification.format( formatHourToVerification.parse( hora ) ),
					maxIntervalLocalDateTime,
					usuario,
					IntervalStatus.UPDATE,
					null ) )
			{
				status = new StatusCrudDto( StatusCrudEnum.SCHEDULING_INVALID, "Agendamento indisponível" );
				return status;
			}

			final Cliente cliente = this.clientService.getByCPF( cpf );
			final Agendamento agendamento = this.schedulingDao.getById( service.getAgendamento().getId() );
			if ( !agendamento.getHora().equals( sqlHour ) || ( !agendamento.getData().equals( sqlDate ) ) )
			{
				agendamento.setNotificacao( false );
			}
			agendamento.setData( sqlDate );
			agendamento.setHora( sqlHour );
			service.setCliente( cliente );

			this.serviceDao.update( service );
			this.schedulingDao.update( agendamento );

			status = new StatusCrudDto( StatusCrudEnum.SUCCESS, null );
		}
		catch ( final Exception e )
		{
			status = new StatusCrudDto( StatusCrudEnum.ERROR, e.getMessage() );
		}

		return status;

	}

	@Autowired
	private ClientService clientService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private RequestService requestService;

	@Autowired
	private SchedulingDao schedulingDao;

	@Autowired
	private br.com.BarberShopFreeStyle.services.Service service;

	@Autowired
	private ServiceDao serviceDao;

	@Autowired
	private SimpleEmailService simpleEmailService;

}
