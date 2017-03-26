
import java.util.Scanner;



public class Handler {


	public static void main(String[] args){
		
		OwnLogger.logging("Application was started.");
		String fileName = null;
		
		//Looking for the argument given from console
		try {
			fileName = args[0];
		} catch (IndexOutOfBoundsException e) {
			//in case no argument given from console, user is asked to specify.
			OwnLogger.logging("FileName was not specifies by user.");
			fileName = userInput(""); 
			
		}
		
		ExcelReader eR = new ExcelReader();
		
		//Is asking for a file till the user put the file which can be opened
		while(!eR.fileFound(fileName)){
			fileName = userInput("Soubor [" + fileName + "] nebyl nalezen.\n");
			OwnLogger.logging("FileName '" + fileName+ "' was not found.");
		}
		
		System.out.println("System nacetl soubor...");
		OwnLogger.logging("The required file '" +fileName + "' has been found.");
		
		
		if(fileName.matches("^.*\\.xls$")){
			OwnLogger.logging("Starting to read the file '" + fileName + "'.");
			eR.goThroughXLSSheets(fileName);
		} else
			try {
				OwnLogger.logging("Starting to read the file '" + fileName + "'.");
				if(fileName.matches("^.*\\.xlsx$")){
					eR.goThroughXLSXSheets(fileName);
				}else{
					System.out.println("Neni excel file");
					OwnLogger.logging("Specifies File '" + fileName + "' is not in format XLS or XLSX.");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		
		DatabaseInput.closeConnection();
		OwnLogger.logging("The program has ended.");
		OwnLogger.closeLogger();
		System.out.println("Konec.");
		OwnLogger.logging("The log file was not close correctly.");
		
	}

	//Static method which retrieves inputs from console And giving simple statement.
	private static String userInput(String s) {
		System.out.print(s);
		System.out.println("Specifikujte prosim excel soubor, ktery chcete kopirovat.[Example.xls(x)]");
		Scanner sc = new Scanner(System.in);
		String value = sc.nextLine();
		sc.close();
		return value;
		
	}

}