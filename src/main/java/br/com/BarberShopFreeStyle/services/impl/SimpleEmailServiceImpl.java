package br.com.BarberShopFreeStyle.services.impl;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.BarberShopFreeStyle.services.SimpleEmailService;

@Service
public class SimpleEmailServiceImpl implements SimpleEmailService {
	
	private static final Log LOG = LogFactory.getLog( SimpleEmailService.class );
	
	private static final String CONFIGSET = "ConfigSet";

	@Override
	public void sendEmail(String bodyHtml, String subject, String[] contacts) throws UnsupportedEncodingException,
	MessagingException {
		
		if ( ( contacts != null ) && ( contacts.length != 0 ) )
		{
			final String bodyToEmail = String.join( System.getProperty( "line.separator" ), bodyHtml );

			System.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
			
			final Properties props = System.getProperties();
			props.put( "mail.transport.protocol", "smtp" );
			props.put( "mail.smtp.port", this.port );
			props.put( "mail.smtp.auth", this.auth );
			props.put( "mail.smtp.starttls.enable", this.ssl );
			if ( this.ssl )
			{
				props.put( "mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory" );
				props.put( "mail.smtp.ssl.checkserveridentity", "true" );
			}

			final Session session = Session.getDefaultInstance( props );

			final MimeMessage msg = new MimeMessage( session );
			msg.setFrom( new InternetAddress( this.fromAddress, this.fromPersonal ) );
			final Address[] recipients = contactsToRecipients( contacts );
			// se email for para "varios" destinatarios entao envia como copia oculta para nao expor
			// endereco entre os destinatarios.
			final RecipientType recipientType = recipients.length > 1
				? Message.RecipientType.BCC
				: Message.RecipientType.TO;
			msg.setRecipients( recipientType, recipients );
			msg.setSubject( subject );
			msg.setContent( bodyToEmail, "text/html" );
			msg.setHeader( "X-SES-CONFIGURATION-SET", CONFIGSET );

			final Transport transport = session.getTransport();
			try
			{
				transport.connect( this.host, this.user, this.pass );
				// Send the email.
				transport.sendMessage( msg, msg.getAllRecipients() );
			}
			catch ( final Exception e )
			{
				LOG.error( "Error sending email.", e );
			}
		}
		
		// TODO Auto-generated method stub
		
	}
	
	private Address[] contactsToRecipients( final String[] contacts )
	{
		return Arrays.stream( contacts ).map( contact -> {
			try
			{
				return new InternetAddress( contact );
			}
			catch ( final AddressException e )
			{
				if ( LOG.isErrorEnabled() )
				{
					LOG.error( String.format( "Error creating recipient %s", contact ), e );
				}
				return null;
			}
		} ).filter( Objects::nonNull ).toArray( Address[]::new );
	}
	
	@Value( "${mail.from.address}" )
	private String fromAddress;

	@Value( "${mail.from.personal}" )
	private String fromPersonal;

	@Value( "${mail.smtp.host}" )
	private String host;

	@Value( "${mail.smtp.password}" )
	private String pass;

	@Value( "${mail.smtp.port}" )
	private int port;

	@Value( "${mail.smtp.ssl}" )
	private boolean ssl;
	
	@Value( "${mail.smtp.auth}" )
	private boolean auth;
	
	@Value( "${mail.smtp.user}" )
	private String user;

}
