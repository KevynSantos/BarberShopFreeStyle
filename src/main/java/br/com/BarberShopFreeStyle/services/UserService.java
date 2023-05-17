package br.com.BarberShopFreeStyle.services;

import br.com.BarberShopFreeStyle.dtos.StatusCrudDto;
import br.com.BarberShopFreeStyle.enums.StatusChangedPassword;
import br.com.BarberShopFreeStyle.models.Usuario;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;

import org.springframework.data.domain.Page;

public interface UserService
	extends AbstractService<Usuario, Long>
{

	List<StatusCrudDto> add(
		String name,
		String cpf,
		String dateNasc,
		String email,
		String telefone,
		String adress,
		String tipoFuncionario,
		String login,
		String password )
		throws ParseException,
			Exception;

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
	 * @param login
	 * @param password
	 * @return
	 * @throws Exception
	 */
	List<StatusCrudDto> addClientInApp(
		final String name,
		final String cpf,
		final String dateNasc,
		final String email,
		final String telefone,
		final String adress,
		final String password )
		throws Exception;

	List<StatusCrudDto> changeData(
		String name,
		String cpf,
		String dateNasc,
		String email,
		String telefone,
		String adress,
		String password,
		Usuario user );

	void downloadExcel( List<Usuario> listUsers, ServletOutputStream outputStream )
		throws IOException;

	void downloadPdf( final List<Usuario> listUsers, final OutputStream outputStream, Locale locale )
		throws Exception;

	Usuario getByLoginAndPassword( String login, String password );

	void getEmployeeRanking( Usuario usuario, ServletOutputStream outputStream )
		throws IOException,
			ParseException;

	Page<Usuario> getResultPage( Integer pageNumber, Integer pageSize );

	/**
	 * <p>
	 * </p>
	 *
	 * @param idEmployee
	 * @param password
	 * @param confirmPassword
	 * @return
	 */
	StatusChangedPassword tryChangedPassword( Long idEmployee, String password, String confirmPassword );

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
	 * @throws ParseException
	 * @throws Exception
	 */
	List<StatusCrudDto> update(
		String nome,
		String cpf,
		String dataNasc,
		String email,
		String telefone,
		String endereco,
		String typeEmployee,
		Long idUser )
		throws ParseException,
			Exception;

}
