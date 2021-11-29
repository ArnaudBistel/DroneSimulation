package utils.fileWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import solutionSolver.simulatedAnnealing.SimulatedAnnealingAnalyser;

public class ExcelFileWriter {
	
	static byte[] RGB_GREY_10_PCT = {(byte)230,(byte)230,(byte)230};
	static XSSFColor GREY_10_PERCENT = new XSSFColor(RGB_GREY_10_PCT);

	static String costHeader = "Cost (kw/h)";
	static String execTimeHeader = "Exec (ms)";
	static String tempHeader = "Temperature";
	static String alphaHeader = "Alpha";
	static String iterPalierHeader = "Nb iter palier";
	static String initialCostHeader = "Initial cost :";
	
	public static void printInFile(String fileName, double initialCost,
			double[] costResultTemp, double[] costResultAlpha, double[] costResultNbIterPalier,
			double[] timeResultTemp, double[] timeResultAlpha, double[] timeResultNbIterPalier) {
		
		Workbook workbook = new XSSFWorkbook();
		
		//create excel file
		Sheet sheet = workbook.createSheet("Simulated_Annealing");
		sheet.setColumnWidth(0, 1000); //empty
		sheet.setColumnWidth(1, 3000); //Temperature
		sheet.setColumnWidth(2, 3000); //cost
		sheet.setColumnWidth(3, 3000); //time
		sheet.setColumnWidth(4, 1000); //empty
		sheet.setColumnWidth(5, 3000); //Alpha
		sheet.setColumnWidth(6, 3000); //cost
		sheet.setColumnWidth(7, 3000); //time
		sheet.setColumnWidth(8, 1000); //empty
		sheet.setColumnWidth(9, 3000); //NbIterPalier
		sheet.setColumnWidth(10, 3000); //cost
		sheet.setColumnWidth(11, 3000); //time
		
		//style creation part

		DataFormat fmt = workbook.createDataFormat();
		
		XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		XSSFCellStyle TempAndIterPalierCellStyle = (XSSFCellStyle) workbook.createCellStyle();
		TempAndIterPalierCellStyle.setFillForegroundColor(GREY_10_PERCENT);
		TempAndIterPalierCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		TempAndIterPalierCellStyle.setDataFormat(fmt.getFormat("#,##0"));
		
		XSSFCellStyle AlphaCellStyle = (XSSFCellStyle) workbook.createCellStyle();
		AlphaCellStyle.setFillForegroundColor(GREY_10_PERCENT);
		AlphaCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		AlphaCellStyle.setDataFormat(fmt.getFormat("#,######0.000000"));

		XSSFCellStyle valueStyle = (XSSFCellStyle) workbook.createCellStyle();
		valueStyle.setDataFormat(fmt.getFormat("#,##0.00"));

		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);

		headerStyle.setFont(font);
		AlphaCellStyle.setFont(font);
		valueStyle.setFont(font);
		
		//header part
		createExcelHeader(sheet, headerStyle, initialCost);
		
		//content
		for(int i = 0; i < SimulatedAnnealingAnalyser.NB_STEP_TEST; i++) {
			addLine(sheet, TempAndIterPalierCellStyle, AlphaCellStyle, valueStyle, i,
					costResultTemp[i], costResultAlpha[i], costResultNbIterPalier[i],
					timeResultTemp[i], timeResultAlpha[i], timeResultNbIterPalier[i]);
		}
		
		//save file
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		String fileLocation = path.substring(0, path.length() - 1) + fileName + ".xlsx";

		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(fileLocation);
			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void createExcelHeader(Sheet sheet, CellStyle style, double initialCost){
		Row header = sheet.createRow(1);
		//temp
		Cell c = header.createCell(1);
		c.setCellValue(tempHeader);
		c.setCellStyle(style);
		CellUtil.setAlignment(c, HorizontalAlignment.LEFT);
		c = header.createCell(2);
		c.setCellValue(costHeader);
		c.setCellStyle(style);
		CellUtil.setAlignment(c, HorizontalAlignment.CENTER);
		c = header.createCell(3);
		c.setCellValue(execTimeHeader);
		c.setCellStyle(style);
		CellUtil.setAlignment(c, HorizontalAlignment.CENTER);
		//alpha
		c = header.createCell(5);
		c.setCellValue(alphaHeader);
		c.setCellStyle(style);
		CellUtil.setAlignment(c, HorizontalAlignment.LEFT);
		c = header.createCell(6);
		c.setCellValue(costHeader);
		c.setCellStyle(style);
		CellUtil.setAlignment(c, HorizontalAlignment.CENTER);
		c = header.createCell(7);
		c.setCellValue(execTimeHeader);
		c.setCellStyle(style);
		CellUtil.setAlignment(c, HorizontalAlignment.CENTER);
		//alpha
		c = header.createCell(9);
		c.setCellValue(iterPalierHeader);
		c.setCellStyle(style);
		CellUtil.setAlignment(c, HorizontalAlignment.LEFT);
		c = header.createCell(10);
		c.setCellValue(costHeader);
		c.setCellStyle(style);
		CellUtil.setAlignment(c, HorizontalAlignment.CENTER);
		c = header.createCell(11);
		c.setCellValue(execTimeHeader);
		c.setCellStyle(style);
		CellUtil.setAlignment(c, HorizontalAlignment.CENTER);
		
		//initial cost
		c = header.createCell(13);
		c.setCellValue(initialCostHeader);
		c.setCellStyle(style);
		CellUtil.setAlignment(c, HorizontalAlignment.LEFT);
		c = header.createCell(14);
		c.setCellValue(initialCost);
		CellUtil.setAlignment(c, HorizontalAlignment.LEFT);
	}
	
	private static void addLine(Sheet sheet, CellStyle tempAndIterPalierStyle, CellStyle alphaStyle, CellStyle valueStyle, int rowNum,
			double costResultTemp, double costResultAlpha, double costResultNbIterPalier,
			double timeResultTemp, double timeResultAlpha, double timeResultNbIterPalier) {
		Row row = sheet.createRow(rowNum + 2);
		
		//Temp
		Cell c = row.createCell(1);
		c.setCellValue(SimulatedAnnealingAnalyser.INIT_TEMP +
				rowNum * SimulatedAnnealingAnalyser.STEP_TEMP);
		c.setCellStyle(tempAndIterPalierStyle);
		c = row.createCell(2);
		c.setCellValue(costResultTemp);
		c.setCellStyle(valueStyle);
		c = row.createCell(3);
		c.setCellValue(timeResultTemp);
		c.setCellStyle(valueStyle);
		
		//alpha
		c = row.createCell(5);
		c.setCellValue(1 - (
				(1 - SimulatedAnnealingAnalyser.INIT_ALPHA) /
				(1 + rowNum * SimulatedAnnealingAnalyser.STEP_ALPHA)
				)
		);
		c.setCellStyle(alphaStyle);
		c = row.createCell(6);
		c.setCellValue(costResultAlpha);
		c.setCellStyle(valueStyle);
		c = row.createCell(7);
		c.setCellValue(timeResultAlpha);
		c.setCellStyle(valueStyle);
		
		//nbIterPalier
		c = row.createCell(9);
		c.setCellValue(SimulatedAnnealingAnalyser.INIT_NB_ITER_PALIER +
				rowNum * SimulatedAnnealingAnalyser.STEP_NB_ITER_PALIER);
		c.setCellStyle(tempAndIterPalierStyle);
		c = row.createCell(10);
		c.setCellValue(costResultTemp);
		c.setCellStyle(valueStyle);
		c = row.createCell(11);
		c.setCellValue(timeResultTemp);
		c.setCellStyle(valueStyle);
	}
}
