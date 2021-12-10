package br.com.BarberShopFreeStyle.daos;

import br.com.BarberShopFreeStyle.enums.TypeProfile;
import br.com.BarberShopFreeStyle.models.TipoPerfil;

/**
 * <p>
 * </p>
 *
 * @author kevyn.santos
 * @version 1.0 Created on Sep 13, 2021
 */
public interface TipoPerfilDao
	extends AbstractDao<TipoPerfil, Long>
{

	TipoPerfil getByType( TypeProfile type );
}
