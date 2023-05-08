package br.com.BarberShopFreeStyle.enums;

public enum FormatImages {
	
	JPG("jpg"),
	PNG("png"),
	ENCODING_IMAGE_BASE_64("data:image/png;base64,");
	
	FormatImages(String value)
	{
		this.value = value;
	}
	
	private String value;
	
	public String getValue()
	{
		return this.value;
	}

}
