package br.com.BarberShopFreeStyle.enums;

public enum TypeProfile
{

	ADMINISTRADOR, BARBEIRO, CABELEIREIRO, MANICURE, PEDICURE, CLIENTE;

	public static boolean checkScheduling( final TypeProfile type )
	{
		if ( type.equals( TypeProfile.CABELEIREIRO ) || type.equals( TypeProfile.MANICURE ) )
		{
			return true;
		}

		return false;
	}

	public static TypeProfile getEnum( final String parseStr )
		throws Exception
	{
		try
		{
			return TypeProfile.valueOf( parseStr );
		}

		catch ( final Exception e )
		{
			return null;
		}
	}

}
