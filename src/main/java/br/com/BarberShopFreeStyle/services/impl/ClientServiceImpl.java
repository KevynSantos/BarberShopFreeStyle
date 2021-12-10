package br.com.BarberShopFreeStyle.services.impl;

import br.com.BarberShopFreeStyle.daos.ClientDao;
import br.com.BarberShopFreeStyle.dtos.ClienteDto;
import br.com.BarberShopFreeStyle.dtos.StatusCrudDto;
import br.com.BarberShopFreeStyle.enums.StatusCrudEnum;
import br.com.BarberShopFreeStyle.models.Cliente;
import br.com.BarberShopFreeStyle.models.Servico;
import br.com.BarberShopFreeStyle.services.ClientService;
import br.com.BarberShopFreeStyle.services.SchedulingService;
import br.com.BarberShopFreeStyle.utils.Conversion;
import br.com.BarberShopFreeStyle.utils.Validation;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.criteria.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class ClientServiceImpl<T>
	extends AbstractService
	implements ClientService
{

	private static final Logger LOG = LogManager.getLogger( "ClientServiceImpl" );

	@Override
	public List<StatusCrudDto> add(
		final String nome,
		final String sobrenome,
		final String cpf,
		final String dataNascimento,
		final String endereco,
		final String telefone,
		final String email )
		throws ParseException
	{

		final List<StatusCrudDto> listStatus = new ArrayList<>();

		try
		{

			if ( !Validation.validateTelefone( telefone ) )
			{
				listStatus.add( new StatusCrudDto( StatusCrudEnum.TELEPHONE_INVALID, null ) );
			}

			if ( !checkCpfAdd( cpf ) )
			{
				listStatus.add( new StatusCrudDto( StatusCrudEnum.CPF_INVALID, null ) );
			}

			if ( !Validation.validateEmail( email ) )
			{
				listStatus.add( new StatusCrudDto( StatusCrudEnum.EMAIL_INVALID, null ) );
			}

			if ( !listStatus.isEmpty() )
			{
				return listStatus;
			}

			final Cliente cliente = new Cliente();

			cliente.setNome( nome+" "+sobrenome );
			cliente.setCpf( cpf );
			cliente.setDtNascimento( Conversion.convertDateSql( dataNascimento ) );
			cliente.setEndereco( endereco );
			cliente.setTelefone( telefone );
			cliente.setEmail( email );

			this.clientDao.insert( cliente );

			listStatus.add( new StatusCrudDto( StatusCrudEnum.SUCCESS, null ) );
		}
		catch ( final Exception e )
		{
			LOG.error( e );
			listStatus.add( new StatusCrudDto( StatusCrudEnum.ERROR, e.getMessage() ) );
		}

		return listStatus;
	}

	@Override
	public List<Cliente> all()
	{
		// TODO Auto-generated method stub
		return null;
	}

	private Specification<T> buildSpecification(
		final String nome,
		final String cpf,
		final String dataInicial,
		final String dataFinal,
		final String email,
		final String telefone,
		final String endereco )
	{
		return ( root, query, c ) -> {

			query.distinct( true );

			Predicate conditions = c.conjunction();

			if ( ( !"".equals( nome ) ) && ( nome != null ) )
			{
				conditions = c.and( conditions, c.equal( root.get( "nome" ), nome ) );
			}

			if ( ( !"".equals( cpf ) ) && ( cpf != null ) )
			{
				conditions = c.and( conditions, c.equal( root.get( "cpf" ), cpf ) );
			}

			if ( ( !"".equals( dataInicial ) )
				&& ( dataInicial != null )
				&& ( !"".equals( dataFinal ) )
				&& ( dataFinal != null ) )
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

					conditions = c.and( conditions, c.between( root.get( "dtNascimento" ), convertDate1, date2 ) );
				}
				catch ( final ParseException e )
				{

					LOG.error( e );
				}
			}

			if ( ( !"".equals( email ) ) && ( email != null ) )
			{
				conditions = c.and( conditions, c.equal( root.get( "email" ), email ) );
			}

			if ( ( !"".equals( telefone ) ) && ( telefone != null ) )
			{
				conditions = c.and( conditions, c.equal( root.get( "telefone" ), telefone ) );
			}

			if ( ( !"".equals( endereco ) ) && ( endereco != null ) )
			{
				conditions = c.and( conditions, c.like( root.get( "endereco" ), endereco ) );
			}

			conditions = c.and( conditions, c.isNull( root.get( "dataExclusao" ) ) );

			return conditions;

		};
	}

	private boolean checkCpfAdd( final String cpf )
	{
		if ( !Validation.validateCpf( cpf, true ) )
		{
			return false;
		}

		final Cliente client = this.clientDao.getByCPF( cpf );

		if ( client == null )
		{
			return true;
		}

		return false;
	}

	private boolean checkCpfUpdate( final String cpf )
	{
		if ( !Validation.validateCpf( cpf, true ) )
		{
			return false;
		}

		final Cliente cliente = this.clientDao.getByCPF( cpf );

		if ( cliente != null )
		{
			return false;
		}

		return true;
	}

	@Override
	public void delete( final Cliente entity )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById( final Long id )
	{
		// TODO Auto-generated method stub
		final Cliente cliente = this.clientDao.getById( id );
		cliente.setDataExclusao( new Date() );
		this.clientDao.update( cliente );
		
	}

	@Override
	public void downloadExcel( final List<Cliente> listClient, final OutputStream outputStream )
		throws Exception
	{
		// TODO Auto-generated method stub
		int cell = 0;
		int column = 0;
		final XSSFWorkbook wb = new XSSFWorkbook();

		final List<String> titles = new ArrayList<String>();
		titles.add( "Nome" );
		titles.add( "CPF" );
		titles.add( "Telefone" );
		titles.add( "Endereço" );
		titles.add( "E-mail" );

		boolean titleIsOK = false;
		XSSFSheet abaPrimaria = wb.createSheet( "Servicos" );

		for ( final Cliente cliente : listClient )
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

			linha.createCell( column ).setCellValue( cliente.getNome() );
			column++;
			linha.createCell( column ).setCellValue( cliente.getCpf() );
			column++;
			linha.createCell( column ).setCellValue( cliente.getTelefone() );
			column++;
			linha.createCell( column ).setCellValue( cliente.getEndereco() );
			column++;
			linha.createCell( column ).setCellValue( cliente.getEmail() );
			// quando tiver futuras colunas
			// column++;
		}

		wb.write( outputStream );
		wb.close();

		outputStream.flush();
		outputStream.close();

	}

	@Override
	public void downloadPdf( final List<Cliente> listClient, final OutputStream outputStream, Locale locale )
			throws IOException
		{
			

			StringBuilder joinTextBody = new StringBuilder();
			
			final List<String> titles = new ArrayList<String>();
			titles.add( "Nome" );
			titles.add( "CPF" );
			titles.add( "Telefone" );
			titles.add( "Endereço" );
			titles.add( "E-mail" );
			
			String titlePdf = "Clientes";
			
			joinTextBody.append(buildHeadHtmlPdf(titlePdf,titles,locale));
			
			// columns
			for ( final Cliente cliente : listClient )
			{
				joinTextBody
				.append(messageSource.getMessage("modelPdfBodyRow", new Object[]{}, locale))
				.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{cliente.getNome()}, locale))
				.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{cliente.getCpf()}, locale))
				.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{formatString(cliente.getTelefone(),"(##) ####-####")}, locale))
				.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{cliente.getEndereco(),}, locale))
				.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{cliente.getEmail()}, locale))
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
	public Cliente getByCPF( final String cpf )
	{
		// TODO Auto-generated method stub

		return this.clientDao.getByCPF( cpf );
	}

	@Override
	public Cliente getById( final Long id )
	{
		// TODO Auto-generated method stub
		return this.clientDao.getById( id );
	}

	@Override
	public Page<Cliente> getResultPage(
		final Integer pageNumber,
		final Integer pageSize,
		final String nome,
		final String cpf,
		final String dataInicial,
		final String dataFinal,
		final String email,
		final String telefone,
		final String endereco )
	{

		final Specification<T> specs = buildSpecification(
			nome,
			cpf,
			dataInicial,
			dataFinal,
			email,
			telefone,
			endereco );

		return this.clientDao.listClients( specs, pageNumber, pageSize );
	}

	@Override
	public Cliente insert( final Cliente entity )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void preparedDeleteClient( final Long idCliente )
	{
		// TODO Auto-generated method stub

		final List<br.com.BarberShopFreeStyle.models.Servico> listService = this.service.getByIdClient( idCliente );

		if ( listService != null )
		{
			for ( final br.com.BarberShopFreeStyle.models.Servico servico : listService )
			{
				if ( servico.getAgendamento() != null )
				{
					
					this.service.delete( servico.getId() );
				}
				else
				{

					this.service.delete( servico.getId() );
				}
			}
		}

		this.clientService.deleteById( idCliente );

	}

	@Override
	public Cliente update( final Cliente entity )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StatusCrudDto> update(
		final String nome,
		final String cpf,
		final String dataNasc,
		final String email,
		final String telefone,
		final String endereco,
		final Long idClient,
		final String sobrenome )
		throws ParseException
	{
		final List<StatusCrudDto> listStatus = new ArrayList<>();

		try
		{

			final Cliente client = this.clientDao.getById( idClient );

			if ( client.getCpf().equals( cpf )
				&& client.getDtNascimento().equals( Conversion.convertDateSql( dataNasc ) )
				&& client.getEmail().equals( email )
				&& endereco.equals( client.getEndereco() )
				&& client.getNome().equals( nome+" "+sobrenome )
				&& client.getTelefone().equals( telefone ) )
			{
				listStatus.add( new StatusCrudDto( StatusCrudEnum.NO_CHANGE, null ) );
				return listStatus;
			}

			if ( !Validation.validateTelefone( telefone ) )
			{
				listStatus.add( new StatusCrudDto( StatusCrudEnum.TELEPHONE_INVALID, null ) );
			}

			if ( !checkCpfUpdate( cpf ) && !client.getCpf().equals( cpf ) )
			{
				listStatus.add( new StatusCrudDto( StatusCrudEnum.CPF_INVALID, null ) );
			}

			if ( !Validation.validateEmail( email ) )
			{
				listStatus.add( new StatusCrudDto( StatusCrudEnum.EMAIL_INVALID, null ) );
			}

			if ( !listStatus.isEmpty() )
			{
				return listStatus;
			}

			client.setNome( nome+" "+sobrenome );
			client.setCpf( cpf );
			client.setDtNascimento( Conversion.convertDateSql( dataNasc ) );
			client.setEmail( email );
			client.setTelefone( telefone );
			client.setEndereco( endereco );

			this.clientDao.update( client );

			listStatus.add( new StatusCrudDto( StatusCrudEnum.SUCCESS, null ) );
		}

		catch ( final Exception e )
		{
			LOG.error( e );
			listStatus.add( new StatusCrudDto( StatusCrudEnum.ERROR, e.getMessage() ) );
		}

		return listStatus;

	}

	@Autowired
	private ClientDao clientDao;

	@Autowired
	private ClientService clientService;

	@Autowired
	private SchedulingService schedulingService;

	@Autowired
	private br.com.BarberShopFreeStyle.services.Service service;

	@Override
	public ClienteDto getClientLikeCpf(String cpf) {
		// TODO Auto-generated method stub
		return this.clientDao.getClientLikeCpf( cpf );
	}

	@Override
	public ClienteDto getClientLikeNome(String nome) {
		// TODO Auto-generated method stub
		return this.clientDao.getClientLikeNome( nome );
	}

}
