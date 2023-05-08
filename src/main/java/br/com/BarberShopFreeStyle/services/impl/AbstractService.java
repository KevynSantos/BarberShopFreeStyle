package br.com.BarberShopFreeStyle.services.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.ParseException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.text.MaskFormatter;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;

import br.com.BarberShopFreeStyle.daos.DescontoDao;
import br.com.BarberShopFreeStyle.daos.RequestServiceDao;
import br.com.BarberShopFreeStyle.daos.ServiceDao;
import br.com.BarberShopFreeStyle.enums.FormatImages;
import br.com.BarberShopFreeStyle.enums.StatusService;
import br.com.BarberShopFreeStyle.models.Cliente;
import br.com.BarberShopFreeStyle.models.PedidoServico;
import br.com.BarberShopFreeStyle.models.Servico;
import br.com.BarberShopFreeStyle.services.ClientService;

public class AbstractService {
	

	
	
	protected XSSFSheet adaptColumnsExcel( final int column, final XSSFSheet sheet )
	{
		// Adapta o texto dentro da coluna do excel
		for ( int i = 0; i < column; i++ )
		{
			sheet.autoSizeColumn( i );
		}

		return sheet;
	}
	
	public static String getDayOfWeek(int value){
	    String day = "";
	    switch(value){
	    case 1:
	        day="Sunday";
	        break;
	    case 2:
	        day="Monday";
	        break;
	    case 3:
	        day="Tuesday";
	        break;
	    case 4:
	        day="Wednesday";
	        break;
	    case 5:
	        day="Thursday";
	        break;
	    case 6:
	        day="Friday";
	        break;
	    case 7:
	        day="Saturday";
	        break;
	    }
	    return day;
	}
	
	protected String pathImageToEcondingImageHtml(String path, String formatImage ) throws IOException
	{
		URL url = ClassLoader.getSystemResource( path.substring(1) );
		
		if(url == null)
		{
			url = getClass().getResource(path);
		}
		
		BufferedImage image = ImageIO.read( url );
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, formatImage, baos);
		byte[] imageBytes = baos.toByteArray();
		
		Base64.Encoder encoder = Base64.getEncoder();

		return FormatImages.ENCODING_IMAGE_BASE_64.getValue() + encoder.encodeToString(imageBytes);
	}
	
	public String calculateDiscount(String value, Cliente client) {
		
		if(!this.descontoDao.getDiscount().isAtivo())
		{
			return value;
		}
		
		String newValue = null;
		
		Date now = new Date();
		
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(now);
		
		int monthNow = cal.get(Calendar.MONTH);
		
		cal.setTime(client.getDtNascimento());
		
		int monthOfClient = cal.get(Calendar.MONTH);
		
		if(monthNow == monthOfClient)
		{
			//entende-se que o cliente deve receber o desconto.
			
			BigDecimal total = new BigDecimal( value.replace( ",", "." ) ).setScale( 2, RoundingMode.DOWN );
			
			double discount = Float.parseFloat(this.descontoDao.getDiscount().getValor())/100;
			
			BigDecimal totalDesc = new BigDecimal(String.valueOf(total.doubleValue() * discount).replace( ",", "." )).setScale( 2, RoundingMode.DOWN );
			
			totalDesc = total.subtract( totalDesc );
			
			newValue = String.valueOf(totalDesc).replace(".", ",");
			
		}
		
		else
		{
			newValue = value;
		}
		
		return newValue;
	}
	
	
	
	
	
	@Autowired
	private DescontoDao descontoDao;
	
	protected String translateStatus(StatusService status)
	{
		if(status.equals(StatusService.DONE))
		{
			return "Finalizado";
		}
		
		if(status.equals(StatusService.IN_PROGRESS))
		{
			return "Em Progresso";
		}
		
		return "null";
	}
	
	protected String calculateMoneyTotalRequests(List<PedidoServico> pedidoServico)
	{
		BigDecimal total = new BigDecimal("0").setScale( 2, RoundingMode.DOWN );
		
		for(PedidoServico requestService : pedidoServico)
		{
			BigDecimal value = new BigDecimal( requestService.getPedido().getPreco().replace(",", ".").replace("R$", "") ).setScale( 2, RoundingMode.DOWN );
			total = total.add( value );
		}
		
		Servico servico = this.servicoDao.getById( pedidoServico.get(0).getIdServico());
		Cliente cliente = servico.getCliente();
		
		total = new BigDecimal(calculateDiscount(String.valueOf(total.doubleValue()),cliente).replace(",", ".")).setScale( 2, RoundingMode.DOWN );
		
		String totalStr = "R$" + String.valueOf(total.doubleValue()).replace(".0", "").replace(".", ",");
		
		return totalStr;
	}
	
	@Autowired
	private ServiceDao servicoDao;
	
	protected String calculateMoneyTotalService(List<Servico> servicos)
	{
		
		BigDecimal total = new BigDecimal("0").setScale( 2, RoundingMode.DOWN );
		
		for(Servico servico : servicos)
		{
			List<PedidoServico> listPedidoServico = this.requestServiceDao.getRequestByService(servico.getId());
			
			Cliente cliente = servico.getCliente();
			
			for(PedidoServico pedidoServico : listPedidoServico)
			{
				BigDecimal value = new BigDecimal(pedidoServico.getPedido().getPreco().replace(",", ".").replace("R$", "")).setScale( 2, RoundingMode.DOWN );
				value = new BigDecimal((calculateDiscount(value.toString(),cliente).replace(",", "."))).setScale( 2, RoundingMode.DOWN );
				total = total.add( value );
			}
		}
		
		
		String totalStr = "R$" + String.valueOf(total.doubleValue()).replace(".", ",");
		
		return totalStr;
	}
	
	@Autowired
	private RequestServiceDao requestServiceDao;
	
	public String buildHeadHtmlPdf(String titlePdf, List<String> titlesTable, Locale locale) throws IOException
	{
		String background = pathImageToEcondingImageHtml("/static/img/barbearia-fundo-index-orig-to-pdf.png",FormatImages.PNG.getValue());
		
		String head = messageSource.getMessage("modelPdfHead", new Object[]{background,titlePdf}, locale);
		
		// titles
		for ( final String item : titlesTable )
		{

			head += messageSource.getMessage("modelPdfHeadTable", new Object[]{item}, locale);
		}
		
		head += messageSource.getMessage("modelPdfHeadTableClose", new Object[]{}, locale);
		
		head += messageSource.getMessage("modelPdfBody", new Object[]{}, locale);
		
		
		return head;
	
	}
	
	 public static String formatString(String value, String pattern) {
	        MaskFormatter mf;
	        try {
	            mf = new MaskFormatter(pattern);
	            mf.setValueContainsLiteralCharacters(false);
	            return mf.valueToString(value);
	        } catch (ParseException ex) {
	            return value;
	        }
	    }
	
	@Autowired
	private MessageSource messageSource;

}
