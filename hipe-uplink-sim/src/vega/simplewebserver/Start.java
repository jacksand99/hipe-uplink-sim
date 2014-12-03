package vega.simplewebserver;

import java.io.File;

public class Start {

    public static void main(String[] args) {
    	try{
	    	String root=args[0];
	    	int port=Integer.parseInt(args[1]);

			SimpleWebServer server = new SimpleWebServer(new File(root), port);
		}catch (Exception e){
			usage();
			e.printStackTrace();
		}        
		System.out.println("Web server started");
    }
    
    public static void usage(){
    	System.out.println("Usage: vega.simplewebserver.Start [root directory] [port number]");
    }

}

