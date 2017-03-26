import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	public ExcelReader() {
		// empty
	}

	// XLS - opens the workbook and go through the sheets
	public void goThroughXLSSheets(String fileName) {
		DatabaseInput.connection = DatabaseInput.connection();
		try (HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(fileName))) {

			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				HSSFSheet sheet = wb.getSheetAt(i);
				if (DatabaseInput.connection != null) {
					if (checkColumns(wb, sheet)) {
						prepareStatments(sheet, wb);
					}
				}
			}

		} catch (FileNotFoundException e) {
			OwnLogger.logging("File was not found :(");
			e.printStackTrace();
		} catch (IOException e) {
			OwnLogger.logging("File could not have been opened :(");
			e.printStackTrace();
		}
	}

	// XLSX - opens the workbook and go through the sheets
	public void goThroughXLSXSheets(String fileName) {
		DatabaseInput.connection = DatabaseInput.connection();
		try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(fileName))) {

			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				XSSFSheet sheet = wb.getSheetAt(i);
				if (DatabaseInput.connection != null) {
					if (checkColumns(wb, sheet)) {
						prepareStatments(sheet, wb);
					}
				}
			}

		} catch (FileNotFoundException e) {
			OwnLogger.logging("File was not found :(");
			e.printStackTrace();
		} catch (IOException e) {
			OwnLogger.logging("File could not have been opened :(");
			e.printStackTrace();
		}
	}

	// XLS Check of columns
	private static boolean checkColumns(HSSFWorkbook wb, HSSFSheet sheet) {
		Row r;
		int countInExcel = 0;
		int countInDatabase = 0;
		String s;

		if (wb.getSheetIndex(sheet) == 0) {
			r = sheet.getRow(0);
		} else {
			r = sheet.getRow(1);
		}
		countInExcel = r.getLastCellNum();
		s = "SELECT * FROM " + PropertiesClass.provide(sheet.getSheetName().replaceAll("\\s", "_"));
		try {

			countInDatabase = DatabaseInput.getIntValues(s);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			OwnLogger.logging("Invalid sql query to get the number of columns in table: "
					+ PropertiesClass.provide(sheet.getSheetName().replaceAll("\\s", "_")));
			return false;
		}
		if (countInDatabase == countInExcel) {
			OwnLogger.logging("[Success]Columns of Excel " + sheet.getSheetName() + " (" + countInExcel + ") == "
					+ "columns of database " + PropertiesClass.provide(sheet.getSheetName().replaceAll("\\s", "_"))
					+ " (" + countInDatabase + ").");
			return true;
		} else {
			OwnLogger.logging("[Failed]Columns of Excel " + sheet.getSheetName() + " (" + countInExcel + ") != "
					+ "columns of database " + PropertiesClass.provide(sheet.getSheetName().replaceAll("\\s", "_"))
					+ " (" + countInDatabase + ").");
			OwnLogger.logging("Skipping to another sheet in Excel File");
			OwnLogger.logging("_________________________________________________________________");
			System.out.println("\nPozor! Pocet sloupcu " + sheet.getSheetName() + " (" + countInExcel + ") se nerovna "
					+ "poctu sloupcu v databazi " + PropertiesClass.provide(sheet.getSheetName().replaceAll("\\s", "_"))
					+ " (" + countInDatabase + "). Data nebyla vlozena do databaze.\n");
			return false;
		}
	}

	// XLSX Check of columns
	private static boolean checkColumns(XSSFWorkbook wb, XSSFSheet sheet) {
		Row r;
		int countInExcel = 0;
		int countInDatabase = 0;
		String s;

		if (wb.getSheetIndex(sheet) == 0) {
			r = sheet.getRow(0);
		} else {
			r = sheet.getRow(1);
		}
		countInExcel = r.getLastCellNum();
		s = "SELECT * FROM " + PropertiesClass.provide(sheet.getSheetName().replaceAll("\\s", "_"));
		try {

			countInDatabase = DatabaseInput.getIntValues(s);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			OwnLogger.logging("Invalid sql query to get the number of columns in table: "
					+ PropertiesClass.provide(sheet.getSheetName().replaceAll("\\s", "_")));
			return false;
		}
		if (countInDatabase == countInExcel) {
			OwnLogger.logging("[Success]Columns of Excel " + sheet.getSheetName() + " (" + countInExcel + ") == "
					+ "columns of database " + PropertiesClass.provide(sheet.getSheetName().replaceAll("\\s", "_"))
					+ " (" + countInDatabase + ").");
			return true;
		} else {
			OwnLogger.logging("[Failed]Columns of Excel " + sheet.getSheetName() + " (" + countInExcel + ") != "
					+ "columns of database " + PropertiesClass.provide(sheet.getSheetName().replaceAll("\\s", "_"))
					+ " (" + countInDatabase + ").");
			OwnLogger.logging("Skipping to another sheet in Excel File");
			OwnLogger.logging("_________________________________________________________________");
			System.out.println("\nPozor! Pocet sloupcu " + sheet.getSheetName() + " (" + countInExcel + ") se nerovna "
					+ "poctu sloupcu v databazi " + PropertiesClass.provide(sheet.getSheetName().replaceAll("\\s", "_"))
					+ " (" + countInDatabase + "). Data nebyla vlozena do databaze.\n");
			return false;
		}
	}

	// XLS - go through cells
	private static void prepareStatments(HSSFSheet sheet, HSSFWorkbook wb) {

		int rowStart = 1;
		// if (wb.getSheetIndex(sheet) > 0) {
		// rowStart = 2;
		// }

		int rowEnd = sheet.getLastRowNum() + 1;

		System.out.println("Nacitam data z listu[" + sheet.getSheetName() + "] do tabulky: "
				+ PropertiesClass.provide(sheet.getSheetName().replaceAll("\\s+?", "_")));
		OwnLogger.logging("_________________________________________________________________");
		OwnLogger.logging("Preparing to insert values from [" + sheet.getSheetName() + "] to database table:"
				+ PropertiesClass.provide(sheet.getSheetName().replaceAll("\\s+?", "_") + "."));

		for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {

			Row r = sheet.getRow(rowNum);
			// if row is empty, continue to the next row
			// if (r == null) {
			// continue;
			// }

			int lastColumn = r.getLastCellNum();

			ArrayList<String> line = new ArrayList<String>();

			// go through all columns
			for (int columnnNum = 0; columnnNum < lastColumn; columnnNum++) {
				Cell c = r.getCell(columnnNum, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
				if (c == null) {
					line.add("");
				} else {
					String value = String.valueOf(c);
					line.add(value);


				}

			}
			if (!lineIsEmpty(line)) {
				// System.out.println(sheet.getSheetName() + ": " +
				// handleDataTypes(line, sheet.getSheetName()));
				System.out.println(line);
				try {
					DatabaseInput.insertValues(handleDataTypes(line, sheet.getSheetName().replaceAll("\\s+?", "_")));
					// OwnLogger.logging("["+sheet.getSheetName()+ "] Row: "+
					// r.getRowNum() + " " + line + "[Success]");

					OwnLogger.logging("[Success][" + sheet.getSheetName() + "] Row: " + (r.getRowNum() + 1) + " {"
							+ line.get(0) + ", " + line.get(1) + ", " + line.get(2) + ", " + line.get(3) + "...}");
				} catch (Exception e) {

					OwnLogger.logging("[Failed][" + sheet.getSheetName() + "] Row: " + (r.getRowNum() + 1) + " "
							+ handleDataTypes(line, sheet.getSheetName()) + e.getMessage());
				}

			}

		}
	}

	// XLSX - go through cells
	private static void prepareStatments(XSSFSheet sheet, XSSFWorkbook wb) {

		int rowStart = 1;
		// if (wb.getSheetIndex(sheet) > 0) {
		// rowStart = 2;
		// }

		int rowEnd = sheet.getLastRowNum() + 1;

		System.out.println("Nacitam data z listu[" + sheet.getSheetName() + "] do tabulky: "
				+ PropertiesClass.provide(sheet.getSheetName().replaceAll("\\s+?", "_")));
		OwnLogger.logging("_________________________________________________________________");
		OwnLogger.logging("Preparing to insert values from [" + sheet.getSheetName() + "] to database table: "
				+ PropertiesClass.provide(sheet.getSheetName().replaceAll("\\s+?", "_")));

		for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {

			Row r = sheet.getRow(rowNum);
			// if row is empty, continue to the next row
			// if (r == null) {
			// continue;
			// }

			int lastColumn = r.getLastCellNum();

			ArrayList<String> line = new ArrayList<String>();

			// go through all columns
			for (int columnnNum = 0; columnnNum < lastColumn; columnnNum++) {
				Cell c = r.getCell(columnnNum, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
				if (c == null) {
					line.add("");
				} else {
					String value = String.valueOf(c);
					line.add(value);
				}

			}
			if (!lineIsEmpty(line)) {
				// System.out.println(sheet.getSheetName() + ": " +
				// handleDataTypes(line, sheet.getSheetName()));
				try {
					DatabaseInput.insertValues(handleDataTypes(line, sheet.getSheetName().replaceAll("\\s+?", "_")));
					// OwnLogger.logging("["+sheet.getSheetName()+ "] Row: "+
					// r.getRowNum() + " " + line + "[Success]");

					OwnLogger.logging("[Success][" + sheet.getSheetName() + "] Row: " + (r.getRowNum() + 1) + " {"
							+ line.get(0) + ", " + line.get(1) + ", " + line.get(2) + ", " + line.get(3) + "...}");
				} catch (Exception e) {

					OwnLogger.logging("[Failed][" + sheet.getSheetName() + "] Row: " + (r.getRowNum() + 1) + " "
							+ handleDataTypes(line, sheet.getSheetName()) + e.getMessage());
				}

			}

		}
	}

	// this function reads the array of values and return true if all the values
	// are empty.
	private static boolean lineIsEmpty(ArrayList<String> list) {

		for (String s : list) {
			if (s == "") {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	// general function - make a query from the values given in arrayList.
	private static String handleDataTypes(ArrayList<String> tempList, String sheetName) {
		String query = "";
		String value;
		for (int i = 0; i < tempList.size(); i++) {
			value = tempList.get(i);
			// matches (+ or - optional, any number, dot optional, any numbers
			// optional)
			// matches 23, 167.24
			// matches +728345678 as string, in case You want telephone number
			// insert as number
			// use this expression (!value.matches("^+?-?\\d+\\.?\\d*?$"))
			if (!value.matches("^-?\\d+\\.?\\d*?$")) {
				value = "'" + value + "'";
			}
			if (i < tempList.size() - 1) {
				query += value + ",";
			} else {
				query += value;
			}
		}
		query = "insert into " + String.valueOf(PropertiesClass.provide(sheetName)) + " values (" + query + ")";
		return query;
	}

	// Open the file and check whether exists, return true if exists, f
	public boolean fileFound(String fileName) {
		try (BufferedReader test = new BufferedReader(new FileReader(fileName))) {
			// empty, just checks
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			// For unexcpected error. Maybe should be print somewhere..
		}
		if (fileName.matches("^.*\\.xlsx?$")) {
			return true;
		} else {
			System.out.println("Neni excel file.");
			return false;
		}
	}
}
