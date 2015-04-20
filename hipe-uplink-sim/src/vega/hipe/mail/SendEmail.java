package vega.hipe.mail;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.text.ParseException;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;


import vega.hipe.gui.xmlutils.HtmlDocument;
import vega.hipe.gui.xmlutils.HtmlEditorKit;

public class SendEmail
{

/**
 * Send an HTML document via email
 * @param to Email addresses to send the HTML document. Comma separated
 * @param doc The Html document to send
 */
public static void sendHtmlDocument(String to,HtmlDocument doc) {
	String from=null;
	String server=null;
	try{
		from = vega.uplink.Properties.getProperty("vega.hipe.mail.from");
		server=vega.uplink.Properties.getProperty("vega.hipe.mail.smtp");
	}catch (Exception e){
		throw new IllegalArgumentException("Could not get send mail properties:"+e.getMessage());
	}
	if (from==null || to==null){
		throw new IllegalArgumentException("Could not get send mail properties.");
	}
	sendHtmlDocument(from,to,server,doc);
}
/**
 * Send an HTML document via email
 * @param to Email addresses to send the HTML document. Comma separated
 * @param doc The Html document to send
 * @param from The email address to send from
 * @param smtpServer The SMTP server to use
 */
public static void sendHtmlDocument(String from,String to,String smtpServer,HtmlDocument doc) {
	StringTokenizer tokenizer=new StringTokenizer(to,",");
	String add[]=new String[tokenizer.countTokens()];
	int count=0;
	while (tokenizer.hasMoreElements()){
		add[count]=tokenizer.nextToken();
		count++;
	}
	sendHtmlDocument(from,add,smtpServer,doc);
}
/**
 * Send a single email message
 * @param to Email addresses to send the HTML document. Comma separated 
 * @param subject The subject to use for the email
 * @param text The body of the message
 */
public static void sendMessage(String to,String subject,String text){
	String from=null;
	String server=null;
	try{
		from = vega.uplink.Properties.getProperty("vega.hipe.mail.from");
		server=vega.uplink.Properties.getProperty("vega.hipe.mail.smtp");
	}catch (Exception e){
		throw new IllegalArgumentException("Could not get send mail properties:"+e.getMessage());
	}
	if (from==null || to==null){
		throw new IllegalArgumentException("Could not get send mail properties.");
	}
	sendMessage(from,to,server,subject,text);
}
/**
 * Send a single email message
 * @param to Email addresses to send the message. Comma separated 
 * @param subject The subject to use for the email
 * @param text The body of the message
 * @param from The email address to send from
 * @param smtpServer The SMTP server to use
 */
public static void sendMessage(String from,String to,String smtpServer,String subject,String text){
	StringTokenizer tokenizer=new StringTokenizer(to,",");
	String add[]=new String[tokenizer.countTokens()];
	int count=0;
	while (tokenizer.hasMoreElements()){
		add[count]=tokenizer.nextToken();
		count++;
	}
	sendMessage(from,add,smtpServer,subject,text);
}
/**
 * Send a single email message
 * @param to Array with Email addresses to send the message
 * @param subject The subject to use for the email
 * @param text The body of the message
 * @param from The email address to send from
 * @param smtpServer The SMTP server to use
 */
public static void sendMessage(String from,String[] to,String smtpServer,String subject,String text){
	try{
		//Properties properties = System.getProperties();
		Properties properties=new Properties();
		properties.setProperty("mail.smtp.host", smtpServer);
		properties.put("mail.smtp.localhost", "toto");
		Session session = Session.getDefaultInstance(properties);
		MimeMessage message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(from));
	    for (int i=0;i<to.length;i++){
	    	message.addRecipient(Message.RecipientType.TO,new InternetAddress(to[i]));
	    }
	    message.setText(text);
		 message.setHeader("Content-Type", "text/html");
		 message.setSubject(subject);
		 Transport.send(message);
	}catch (Exception e){
		IllegalArgumentException iae = new IllegalArgumentException ("Could dnot send mail:"+e.getMessage());
		iae.initCause(e);
		throw(iae);
	}

}
/**
 * Send an HTML document via email
 * @param to Array with Email addresses to send the message
 * @param doc The Html document to send
 * @param from The email address to send from
 * @param smtpServer The SMTP server to use
 */
public static void sendHtmlDocument(String from,String[] to,String smtpServer,HtmlDocument doc) {
	try{
		Properties properties=new Properties();
		properties.setProperty("mail.smtp.host", smtpServer);
		properties.put("mail.smtp.localhost", "toto");
		Session session = Session.getDefaultInstance(properties);
		MimeMessage message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(from));
	    for (int i=0;i<to.length;i++){
	    	message.addRecipient(Message.RecipientType.TO,new InternetAddress(to[i]));
	    }
		MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setHeader("Content-Type", "text/html");
		String text = doc.getHtmlString();
		text=text.replace("<html>", "<html>\n"+HtmlEditorKit.getRulesHTML());
		bodyPart.setText(text);
		 MimeMultipart mpart = new MimeMultipart();
		 mpart.addBodyPart(bodyPart);
		 message.setContent(mpart);
		 message.setHeader("Content-Type", "text/html");
		 message.setSubject(doc.getTitle());
		 Transport.send(message);
	}catch (Exception e){
		IllegalArgumentException iae = new IllegalArgumentException ("Could dnot send mail:"+e.getMessage());
		iae.initCause(e);
		throw(iae);
	}

	
}
protected static MimeMessage sendHtmlDocument(HtmlDocument doc,MimeMessage mess) throws MessagingException{
	MimeBodyPart bodyPart = new MimeBodyPart();
	bodyPart.setHeader("Content-Type", "text/html");
	bodyPart.setText(doc.getHtmlString());
	 MimeMultipart mpart = new MimeMultipart();
	 mpart.addBodyPart(bodyPart);
	mess.setContent(mpart);
	mess.setHeader("Content-Type", "text/html");
	mess.setSubject(doc.getTitle());
	return mess;
}
protected static String readFile(File f) throws IOException, ParseException{
	BufferedReader br = null;
	
			br = new BufferedReader(new FileReader(f));

				return readFile(br);

}
protected static String readFile(BufferedReader br) throws IOException, ParseException{
	String line = "";
	StringBuilder result=new StringBuilder();;
	//java.util.Vector<String> lines=new java.util.Vector<String>();
	
		while ((line = br.readLine()) != null){
			result.append(line);
		}
	
		return result.toString();
}

}