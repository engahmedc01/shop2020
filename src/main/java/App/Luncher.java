package App;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Luncher {
	static Logger log = Logger.getLogger(org.springframework.beans.factory.xml.XmlBeanDefinitionReader.class);
 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		BasicConfigurator.configure();
 			
		log.setLevel(Level.WARN);
		log.getRootLogger().setLevel(Level.WARN);
		App.main(args);
	}
 

}
