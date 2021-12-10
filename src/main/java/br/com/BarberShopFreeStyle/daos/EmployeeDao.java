package br.com.BarberShopFreeStyle.daos;

import br.com.BarberShopFreeStyle.models.Funcionario;

/**
 * <p>
 * </p>
 *
 * @author kevyn.santos
 * @version 1.0 Created on Sep 13, 2021
 */
public interface EmployeeDao
	extends AbstractDao<Funcionario, Long>
{

	/**
	 * <p>
	 * </p>
	 * 
	 * @param cpf
	 * @return
	 */
	Funcionario getByCpf( String cpf );

}
