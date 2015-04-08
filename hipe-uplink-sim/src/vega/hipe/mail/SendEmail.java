package vega.hipe.mail;

//File Name SendEmail.java

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import vega.hipe.gui.xmlutils.HtmlDocument;
import vega.hipe.gui.xmlutils.HtmlEditorKit;

public class SendEmail
{
public static void main(String [] args)
{    
   	herschel.share.util.Configuration.setProperty("var.hcss.dir", "/Users/jarenas 1/Downloads/hcss-12.0.2524");
	herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "/Users/jarenas 1/Downloads/hcss-12.0.2524");
	herschel.share.util.Configuration.setProperty("vega.mib.location", "/Users/jarenas 1/Downloads/MAPPS/MIB");
	herschel.share.util.Configuration.setProperty("vega.hipe.mail.from", "javier.arenas@sciops.esa.int");
	herschel.share.util.Configuration.setProperty("vega.hipe.mail.smtp", "scimta01.esac.esa.int");
	
   /*// Recipient's email ID needs to be mentioned.
   String to = "javier.arenas@sciops.esa.int";

   // Sender's email ID needs to be mentioned
   String from = "javier.arenas@sciops.esa.int";

   // Assuming you are sending email from localhost
   String host = "scimta01.esac.esa.int";

   // Get system properties
   Properties properties = System.getProperties();

   // Setup mail server
   properties.setProperty("mail.smtp.host", host);

   // Get the default Session object.
   Session session = Session.getDefaultInstance(properties);

   try{
      // Create a default MimeMessage object.
      MimeMessage message = new MimeMessage(session);

      // Set From: header field of the header.
      message.setFrom(new InternetAddress(from));

      // Set To: header field of the header.
      message.addRecipient(Message.RecipientType.TO,
                               new InternetAddress(to));
      HtmlDocument doc = HtmlDocument.readFromFile("/Users/jarenas 1/Rosetta/hack 15/PTRM_update_alice_rpc_miro_virtis.ROS-nominal.html");
      message=sendHtmlDocument(doc,message);
      // Set Subject: header field
      //message.setSubject("This is the Subject Line!");

      // Now set the actual message
      
      //message.setText("This is actual message");
      //message.

      // Send message
      Transport.send(message);
      System.out.println("Sent message successfully....");
   }catch (Exception mex) {
      mex.printStackTrace();
   }*/
	try{
		//System.out.println(InetAddress.getLocalHost().getAddress());
		//System.out.println(System.getProperty("host.name"));
		//sendMessage("javier.arenas@sciops.esa.int","Hola","Hola Javier");
		//sendHtmlDocument("javier.arenas@sciops.esa.int,javier.arenas@gmail.com",HtmlDocument.readFromFile("/Users/jarenas 1/Rosetta/hack 15/PTRM_update_alice_rpc_miro_virtis.ROS-nominal.html"));
	}catch(Exception e){
		e.printStackTrace();
	}
}
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
	    // Set To: header field of the header.
	    	message.addRecipient(Message.RecipientType.TO,new InternetAddress(to[i]));
	    }
		/*MimeBodyPart bodyPart = new MimeBodyPart();
		//bodyPart.setHeader("Content-Type", "text/html");
		
		bodyPart.setText(text);
		 MimeMultipart mpart = new MimeMultipart();
		 mpart.addBodyPart(bodyPart);
		 message.setContent(mpart);*/
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
public static void sendHtmlDocument(String from,String[] to,String smtpServer,HtmlDocument doc) {
	try{
		//Properties properties = System.getProperties();
		Properties properties=new Properties();
		properties.setProperty("mail.smtp.host", smtpServer);
		properties.put("mail.smtp.localhost", "toto");
		Session session = Session.getDefaultInstance(properties);
		MimeMessage message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(from));
	    for (int i=0;i<to.length;i++){
	    // Set To: header field of the header.
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