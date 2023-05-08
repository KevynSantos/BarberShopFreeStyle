package br.com.BarberShopFreeStyle.services.impl;

import br.com.BarberShopFreeStyle.daos.EmployeeDao;
import br.com.BarberShopFreeStyle.daos.ServiceDao;
import br.com.BarberShopFreeStyle.daos.TipoPerfilDao;
import br.com.BarberShopFreeStyle.daos.UserDao;
import br.com.BarberShopFreeStyle.dtos.StatusCrudDto;
import br.com.BarberShopFreeStyle.enums.StatusChangedPassword;
import br.com.BarberShopFreeStyle.enums.StatusCrudEnum;
import br.com.BarberShopFreeStyle.enums.TypeProfile;
import br.com.BarberShopFreeStyle.models.Cliente;
import br.com.BarberShopFreeStyle.models.Funcionario;
import br.com.BarberShopFreeStyle.models.Pedido;
import br.com.BarberShopFreeStyle.models.PedidoServico;
import br.com.BarberShopFreeStyle.models.Servico;
import br.com.BarberShopFreeStyle.models.TipoPerfil;
import br.com.BarberShopFreeStyle.models.Usuario;
import br.com.BarberShopFreeStyle.services.UserService;
import br.com.BarberShopFreeStyle.utils.Conversion;
import br.com.BarberShopFreeStyle.utils.Validation;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.servlet.ServletOutputStream;

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
public class UserServiceImpl<T>
	extends AbstractService
	implements UserService
{

	private static final Logger LOG = LogManager.getLogger( "UserServiceImpl" );

	/**
	 * <p>
	 * </p>
	 *
	 * @param name
	 * @param cpf
	 * @param dateNasc
	 * @param email
	 * @param telefone
	 * @param adress
	 * @param tipoFuncionario
	 * @throws Exception
	 * @see br.com.BarberShopFreeStyle.services.UserService#add(java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<StatusCrudDto> add(
		final String name,
		final String cpf,
		final String dateNasc,
		final String email,
		final String telefone,
		final String adress,
		final String tipoFuncionario,
		final String login,
		final String password )
		throws Exception
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

			final Usuario usuario = this.userDao.getByLogin( login );

			if ( usuario != null )
			{
				listStatus.add( new StatusCrudDto( StatusCrudEnum.LOGIN_EXIST, null ) );
			}

			if ( !listStatus.isEmpty() )
			{
				return listStatus;
			}

			final Funcionario funcionario = new Funcionario();

			funcionario.setCpf( cpf );
			funcionario.setDtNascimento( Conversion.convertDateSql( dateNasc ) );
			funcionario.setEmail( email );
			funcionario.setEndereco( adress );
			funcionario.setTelefone( telefone );
			final TypeProfile type = TypeProfile.getEnum( tipoFuncionario );
			funcionario.setMarcaHora( TypeProfile.checkScheduling( type ) );
			funcionario.setNome( name );

			this.employeeDao.insert( funcionario );

			final Usuario user = new Usuario();
			user.setFuncionario( funcionario );
			user.setLogin( login );
			user.setPassword( password );

			final TipoPerfil tipo = this.tipoPerfilDao.getByType( type );

			user.setTipoPerfil( tipo );

			this.userDao.insert( user );

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
	public List<Usuario> all()
	{
		// TODO Auto-generated method stub
		return null;
	}

	private Specification<T> buildSpecification()
	{
		return ( root, query, c ) -> {

			query.distinct( true );

			final Join<Usuario, br.com.BarberShopFreeStyle.models.TipoPerfil> joinTipoPerfil = root
				.join( "tipoPerfil" );

			Predicate conditions = c.and( c.notEqual( joinTipoPerfil.get( "tipo" ), TypeProfile.ADMINISTRADOR ) );

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

		final Funcionario funcionario = this.employeeDao.getByCpf( cpf );

		if ( funcionario == null )
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

		final Funcionario funcionario = this.employeeDao.getByCpf( cpf );

		if ( funcionario != null )
		{
			return false;
		}

		return true;
	}

	@Override
	public void delete( final Usuario entity )
	{
		// TODO Auto-generated method stub
		entity.setDataExclusao( new Date() );
		this.userDao.update( entity );

	}

	@Override
	public void deleteById( final Long id )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void downloadExcel( final List<Usuario> listUsers, final ServletOutputStream outputStream )
		throws IOException
	{
		// TODO Auto-generated method stub
		int cell = 0;
		int column = 0;
		final XSSFWorkbook wb = new XSSFWorkbook();

		final List<String> titles = new ArrayList<String>();
		titles.add( "Login" );
		titles.add( "Senha" );
		titles.add( "Tipo_Funcionario" );
		titles.add( "Nome" );
		titles.add( "Endereco" );
		titles.add( "Pode_Agendar" );
		titles.add( "CPF" );
		titles.add( "E-mail" );
		titles.add( "Telefone" );
		titles.add( "Data_Nascimento" );
		titles.add( "Data_Exclusao" );

		boolean titleIsOK = false;
		XSSFSheet abaPrimaria = wb.createSheet( "Funcionarios" );

		for ( final Usuario usuario : listUsers )
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

			linha.createCell( column ).setCellValue( usuario.getLogin() );
			column++;
			linha.createCell( column ).setCellValue( usuario.getPassword() );
			column++;
			linha.createCell( column ).setCellValue( usuario.getTipoPerfil().getTipo().toString() );
			column++;
			linha.createCell( column ).setCellValue( usuario.getFuncionario().getNome() );
			column++;
			linha.createCell( column ).setCellValue( usuario.getFuncionario().getEndereco() );
			column++;
			linha.createCell( column ).setCellValue( usuario.getFuncionario().isMarcaHora() == true ? "SIM" : "NAO" );
			column++;
			linha.createCell( column ).setCellValue( usuario.getFuncionario().getCpf() );
			column++;
			linha.createCell( column ).setCellValue( usuario.getFuncionario().getEmail() );
			column++;
			linha.createCell( column ).setCellValue( usuario.getFuncionario().getTelefone() );
			column++;
			linha
				.createCell( column )
				.setCellValue( Conversion.convertToDateString( usuario.getFuncionario().getDtNascimento() ) );
			column++;
			linha.createCell( column ).setCellValue( Conversion.convertToDateTimeString( usuario.getDataExclusao() ) );
			// quando tiver futuras colunas
			// column++;
		}

		wb.write( outputStream );
		wb.close();

		outputStream.flush();
		outputStream.close();

	}

	@Override
	public void downloadPdf( final List<Usuario> listUsers, final OutputStream outputStream, Locale locale )
			throws IOException
		{
			

			StringBuilder joinTextBody = new StringBuilder();
			
			final List<String> titles = new ArrayList<String>();
			titles.add( "Nome" );
			titles.add( "Tipo Funcionario" );
			titles.add( "Pode Agendar" );
			titles.add( "CPF" );
			titles.add( "Data Nascimento" );
			
			String titlePdf = "Funcionários";
			
			joinTextBody.append(buildHeadHtmlPdf(titlePdf,titles,locale));
			
			// columns
			for ( final Usuario usuario : listUsers )
			{
				joinTextBody
				.append(messageSource.getMessage("modelPdfBodyRow", new Object[]{}, locale))
				.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{usuario.getFuncionario().getNome()}, locale))
				.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{usuario.getTipoPerfil().getTipo().toString()}, locale));
				final String marcaHora = usuario.getFuncionario().isMarcaHora() == true ? "SIM" : "NAO";
				joinTextBody.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{marcaHora}, locale))
				.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{usuario.getFuncionario().getCpf(),}, locale))
				.append(messageSource.getMessage("modelPdfBodyColumn", new Object[]{Conversion.convertToDateString( usuario.getFuncionario().getDtNascimento() )}, locale))
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
	public Usuario getById( final Long id )
	{
		// TODO Auto-generated method stub
		return this.userDao.getById( id );
	}

	@Override
	public Usuario getByLoginAndPassword( final String login, final String password )
	{
		// TODO Auto-generated method stub
		return this.userDao.getByLoginAndPassword( login, password );
	}

	@Override
	public Page<Usuario> getResultPage( final Integer pageNumber, final Integer pageSize )
	{
		// TODO Auto-generated method stub

		final Specification<T> specification = buildSpecification();
		return this.userDao.listEmployees( specification, pageNumber, pageSize );
	}

	@Override
	public Usuario insert( final Usuario entity )
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param idEmployee
	 * @param password
	 * @param confirmPassword
	 * @see br.com.BarberShopFreeStyle.services.UserService#tryChangedPassword(java.lang.Long,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public StatusChangedPassword tryChangedPassword(
		final Long idUser,
		final String password,
		final String confirmPassword )
	{
		try
		{
			final Usuario usuario = this.userDao.getById( idUser );

			if ( !password.equals( confirmPassword ) )
			{
				return StatusChangedPassword.PASSWORDS_NOT_EQUALS;
			}

			usuario.setPassword( password );
			this.userDao.update( usuario );

			return StatusChangedPassword.SUCCESS;

		}
		catch ( final Exception e )
		{
			LOG.error( e );
			return StatusChangedPassword.ERROR;
		}

	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param nome
	 * @param cpf
	 * @param dataNasc
	 * @param email
	 * @param telefone
	 * @param endereco
	 * @param idUser
	 * @throws Exception
	 * @see br.com.BarberShopFreeStyle.services.UserService#update(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.Long)
	 */
	@Override
	public List<StatusCrudDto> update(
		final String nome,
		final String cpf,
		final String dataNasc,
		final String email,
		final String telefone,
		final String endereco,
		final String typeEmployee,
		final Long idUser )
		throws Exception
	{
		final List<StatusCrudDto> listStatus = new ArrayList<>();
		try
		{
			if ( !Validation.validateTelefone( telefone ) )
			{
				listStatus.add( new StatusCrudDto( StatusCrudEnum.TELEPHONE_INVALID, null ) );
			}

			if ( checkCpfUpdate( cpf ) )
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

			final Usuario user = this.userDao.getById( idUser );
			final TipoPerfil tipoPerfil = user.getTipoPerfil();
			final Funcionario employee = user.getFuncionario();

			if ( ( employee.getCpf().equals( cpf ) )
				&& ( employee.getDtNascimento().equals( Conversion.convertDateSql( dataNasc ) ) )
				&& ( employee.getEmail().equals( email ) )
				&& ( endereco.equals( employee.getEndereco() ) )
				&& ( employee.getNome().equals( nome ) )
				&& ( employee.getTelefone().equals( telefone ) )
				&& (user.getTipoPerfil().getTipo().equals(TypeProfile.valueOf( typeEmployee ))))
			{
				listStatus.add( new StatusCrudDto( StatusCrudEnum.NO_CHANGE, null ) );
				return listStatus;
			}

			if ( !tipoPerfil.getTipo().equals( TypeProfile.valueOf( typeEmployee ) ) )
			{
				final TipoPerfil novoTipo = this.tipoPerfilDao.getByType( TypeProfile.valueOf( typeEmployee ) );
				final boolean checkScheduling = TypeProfile.checkScheduling( novoTipo.getTipo() );
				employee.setMarcaHora( checkScheduling );
				user.setTipoPerfil( novoTipo );
				this.userDao.update( user );
			}

			employee.setNome( nome );
			employee.setCpf( cpf );
			employee.setDtNascimento( Conversion.convertDateSql( dataNasc ) );
			employee.setEmail( email );
			employee.setTelefone( telefone );
			employee.setEndereco( endereco );

			this.employeeDao.update( employee );

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
	public Usuario update( final Usuario entity )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private TipoPerfilDao tipoPerfilDao;

	@Autowired
	private UserDao userDao;

	@Override
	public List<StatusCrudDto> changeData(String name, String cpf, String dateNasc, String email, String telefone, String adress,
			String password, Usuario user) {
		// TODO Auto-generated method stub
		
		final List<StatusCrudDto> listStatus = new ArrayList<>();
		
		try
		{
			if ( !Validation.validateTelefone( telefone ) )
			{
				listStatus.add( new StatusCrudDto( StatusCrudEnum.TELEPHONE_INVALID, null ) );
			}

			if ( checkCpfUpdate( cpf ) )
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
			
			final Funcionario employee = user.getFuncionario();

			if ( ( employee.getCpf().equals( cpf ) )
				&& ( employee.getDtNascimento().equals( Conversion.convertDateSql( dateNasc ) ) )
				&& ( employee.getEmail().equals( email ) )
				&& ( adress.equals( employee.getEndereco() ) )
				&& ( employee.getNome().equals( name ) )
				&& ( employee.getTelefone().equals( telefone ) )
				&& ((Objects.isNull(password)) || (password.equals(""))))
			{
				listStatus.add( new StatusCrudDto( StatusCrudEnum.NO_CHANGE, null ) );
				return listStatus;
			}
			
			if ( ( employee.getCpf().equals( cpf ) )
					&& ( employee.getDtNascimento().equals( Conversion.convertDateSql( dateNasc ) ) )
					&& ( employee.getEmail().equals( email ) )
					&& ( adress.equals( employee.getEndereco() ) )
					&& ( employee.getNome().equals( name ) )
					&& ( employee.getTelefone().equals( telefone )
					&& (Objects.nonNull(password))))
			{
					
				if((!password.equals("")) && password.equals(user.getPassword()))
				{
					listStatus.add( new StatusCrudDto( StatusCrudEnum.NO_CHANGE, null ) );
					return listStatus;
				}
					
			}
			
			employee.setNome( name );
			employee.setCpf( cpf );
			employee.setDtNascimento( Conversion.convertDateSql( dateNasc ) );
			employee.setEmail( email );
			employee.setTelefone( telefone );
			employee.setEndereco( adress );

			this.employeeDao.update( employee );
			
			if(Objects.nonNull(password))
			{
				if((!password.equals("")) && !password.equals(user.getPassword()))
				{
					user.setPassword(password);
					this.userDao.update(user);
				}
			}

			
			listStatus.add( new StatusCrudDto( StatusCrudEnum.SUCCESS, null ) );
			
		}
		
		catch(Exception e)
		{
			LOG.error( e );
			listStatus.add( new StatusCrudDto( StatusCrudEnum.ERROR, e.getMessage() ) );
		}
		
		
		return listStatus;
	}

	@Override
	public void getEmployeeRanking(Usuario usuario, ServletOutputStream outputStream) throws IOException, ParseException {
		// TODO Auto-generated method stub

		List<Servico> listServicos = this.serviceDao.listServicesByUserFor30Days(usuario);
		
		String nothing = "";
		
		int cell = 0;
		int column = 0;
		final XSSFWorkbook wb = new XSSFWorkbook();

		final List<String> titles = new ArrayList<String>();
		titles.add("ID_SERVICO" );
		titles.add("PEDIDOS");
		titles.add("VALOR_TOTAL_DOS_PEDIDOS");
		titles.add("DATA_DE_ABERTURA");
		titles.add("DATA_DO_AGENDAMENTO");
		titles.add("HORA_DO_AGENDAMENTO");
		titles.add("CHECKIN");
		titles.add("NOME_DO_CLIENTE");
		titles.add("CPF_DO_CLIENTE");
		titles.add("VALOR_TOTAL");
		boolean titleIsOK = false;
		XSSFSheet abaPrimaria = wb.createSheet( "Ranking do Funcionário "+usuario.getFuncionario().getNome() );
		
		if(listServicos == null)
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
		}
		else
		{
			XSSFRow linha = null;
			for ( final Servico servico : listServicos )
			{
				if ( !titleIsOK )
				{
					linha = abaPrimaria.createRow( cell );
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
				linha = abaPrimaria.createRow( cell );
				column = 0;
				if(servico.getAgendamento() != null)
				{
					
						
						linha.createCell( column ).setCellValue( servico.getId() );
						column++;
						linha.createCell( column ).setCellValue( getJsonNameRequests(servico.getPedidoServico()) );
						column++;
						linha.createCell( column ).setCellValue( calculateMoneyTotalRequests(servico.getPedidoServico()));
						column++;
						linha.createCell( column ).setCellValue(Conversion.convertToDateString(servico.getAgendamento().getDtAbertura()));
						column++;
						linha.createCell( column ).setCellValue(Conversion.convertToDateString(servico.getAgendamento().getData()));
						column++;
						linha.createCell( column ).setCellValue(Conversion.convertToTimeString(servico.getAgendamento().getHora()));
						column++;
						linha.createCell( column ).setCellValue(Conversion.convertToDateTimeString(servico.getAgendamento().getCheckin()));
						column++;
						linha.createCell( column ).setCellValue(servico.getCliente().getNome());
						column++;
						linha.createCell( column ).setCellValue(servico.getCliente().getCpf());
						
						if(cell == 1)
						{
							column++;
							linha.createCell( column ).setCellValue(calculateMoneyTotalService(listServicos));
						}
				}
				else
				{
						
						linha.createCell( column ).setCellValue( servico.getId() );
						column++;
						linha.createCell( column ).setCellValue( getJsonNameRequests(servico.getPedidoServico()) );
						column++;
						linha.createCell( column ).setCellValue( calculateMoneyTotalRequests(servico.getPedidoServico()));
						column++;
						linha.createCell( column ).setCellValue( Conversion.convertToDateString(servico.getDataCriacao()) );
						column++;
						linha.createCell( column ).setCellValue(nothing);
						column++;
						linha.createCell( column ).setCellValue(nothing);
						column++;
						linha.createCell( column ).setCellValue(nothing);
						column++;
						linha.createCell( column ).setCellValue(servico.getCliente().getNome());
						column++;
						linha.createCell( column ).setCellValue(servico.getCliente().getCpf());
						
						if(cell == 1)
						{
							column++;
							linha.createCell( column ).setCellValue(calculateMoneyTotalService(listServicos));
						}
				}
				
				
			
				// quando tiver futuras colunas
				// column++;
			}
			
			
		}

		wb.write( outputStream );
		wb.close();

		outputStream.flush();
		outputStream.close();

		
	}
	
	private String getJsonNameRequests(List<PedidoServico> listPedidoServico)
	{
		
		StringBuilder str = new StringBuilder();
		boolean first = true;
		for(PedidoServico pedidoServico : listPedidoServico )
		{
			if(!first)
			{
				str.append(",");
			}
			
			Pedido pedido = pedidoServico.getPedido();
			
			str.append(pedido.getNome());
			
			first = false;
		}
		
		return str.toString();
	}
	
	@Autowired
	private ServiceDao serviceDao;

}
