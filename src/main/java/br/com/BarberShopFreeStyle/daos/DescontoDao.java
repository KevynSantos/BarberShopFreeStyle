package br.com.BarberShopFreeStyle.daos;

import br.com.BarberShopFreeStyle.models.Desconto;

public interface DescontoDao extends AbstractDao<Desconto,Long> {
	
	void activeOrActive(boolean isActive);
	
	void updateValueDiscount(String value);

	Desconto getDiscount();

}
