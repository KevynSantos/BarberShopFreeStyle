package br.com.BarberShopFreeStyle.services;

import br.com.BarberShopFreeStyle.dtos.ClienteDto;
import br.com.BarberShopFreeStyle.dtos.StatusCrudDto;
import br.com.BarberShopFreeStyle.models.Cliente;

import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.Page;

public interface ClientService
	extends AbstractService<Cliente, Long>
{

	List<StatusCrudDto> add(
		String nome,
		String sobrenome,
		String cpf,
		String dataNascimento,
		String endereco,
		String telefone,
		String email )
		throws ParseException;

	void downloadExcel( List<Cliente> listClient, OutputStream outputStream )
		throws Exception;

	void downloadPdf( final List<Cliente> listClient, final OutputStream outputStream, Locale locale )
		throws Exception;

	Cliente getByCPF( String cpf );
	
	ClienteDto getClientLikeCpf(String cpf);
	
	ClienteDto getClientLikeNome(String nome);

	Page<Cliente> getResultPage(
		Integer pageNumber,
		Integer pageSize,
		String nome,
		String cpf,
		String dataInicial,
		String dataFinal,
		String email,
		String telefone,
		String endereco );

	void preparedDeleteClient( Long idCliente );

	List<StatusCrudDto> update(
		String nome,
		String cpf,
		String dataNasc,
		String email,
		String telefone,
		String endereco,
		Long idClient,
		String sobrenome )
		throws ParseException;
}
