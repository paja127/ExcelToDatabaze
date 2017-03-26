import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesClass {

	public static String provide(String property) {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("config.properties");

			prop.load(input);
			
		} catch (IOException e) {
			System.out.println("Configuracni soubor 'config.properties' nebyl nalezen :(");
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					
				}
			}
		}
		//Replace white space by underscore
		return prop.getProperty(property.replaceAll("\\s+?", "_"));
	}
}