import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class OwnLogger{
	public static Logger logger;
	public OwnLogger(){
		
	}
	
	public static FileHandler fh;
	public static void logging(String message){
		
		     

		    try {  
		    	
		    	logger = Logger.getLogger("AppPaja"); 
		    	//System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
		    	System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF: %5$s%6$s%n");
		        // This block configure the logger with handler and formatter  
		        if(fh == null){
		        	fh = new FileHandler(PropertiesClass.provide("logFile"));
		        	logger.addHandler(fh);
		        }
		    	
		        
		        
		        logger.setUseParentHandlers(false);
		        SimpleFormatter formatter = new SimpleFormatter(); 
		        
		        if (fh!= null){
		        	fh.setFormatter(formatter);
		        }
		          
		        logger.info(message);  

		    } catch (SecurityException e) {  
		          
		    } catch (IOException e) {  
		          
		    }  

	}
	
	public static void closeLogger(){
		fh.close();
	}
	
	
	
}
