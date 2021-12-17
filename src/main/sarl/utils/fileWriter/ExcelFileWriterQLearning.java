package utils.fileWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

import solutionSolver.qLearning.QLearningAnalyser;

public class ExcelFileWriterQLearning {
	
	static byte[] RGB_GREY_10_PCT = {(byte)230,(byte)230,(byte)230};
	static XSSFColor GREY_10_PERCENT = new XSSFColor(RGB_GREY_10_PCT);

	static String costHeader = "Cost (kw/h)";
	static String execTimeHeader = "Exec (ms)";
	static String alphaHeader = "Alpha";
	static String gammaHeader = "Gamma";
	static String epsilonHeader = "Epsilon";
	static String nbEpisodeHeader = "Nb episode";
	static String initialCostHeader = "Initial cost :";
	
	public static void printInFile(String fileName, double initialCost,
			double[] costResultAlpha, double[] costResultGamma, double[] costResultEpsilon, double[] costResultNbEpisode,
			double[] timeResultAlpha, double[] timeResultGamma, double[] timeResultEpsilon, double[] timeResultNbEpisode) {
		
		Workbook workbook = new XSSFWorkbook();
		
		//create excel file
		Sheet sheet = workbook.createSheet("QLearning");
		sheet.setColumnWidth(0, 1000); //empty
		sheet.setColumnWidth(1, 3000); //Alpha
		sheet.setColumnWidth(2, 3000); //cost
		sheet.setColumnWidth(3, 3000); //time
		sheet.setColumnWidth(4, 1000); //empty
		sheet.setColumnWidth(5, 3000); //Gamma
		sheet.setColumnWidth(6, 3000); //cost
		sheet.setColumnWidth(7, 3000); //time
		sheet.setColumnWidth(8, 1000); //empty
		sheet.setColumnWidth(9, 3000); //Epsilon
		sheet.setColumnWidth(10, 3000); //cost
		sheet.setColumnWidth(11, 3000); //time
		sheet.setColumnWidth(12, 1000); //empty
		
		//style creation part

		DataFormat fmt = workbook.createDataFormat();
		
		XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		XSSFCellStyle AlphaGammaEpsilonCellStyle = (XSSFCellStyle) workbook.createCellStyle();
		AlphaGammaEpsilonCellStyle.setFillForegroundColor(GREY_10_PERCENT);
		AlphaGammaEpsilonCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		AlphaGammaEpsilonCellStyle.setDataFormat(fmt.getFormat("#,######0.000000"));
		
		XSSFCellStyle valueStyle = (XSSFCellStyle) workbook.createCellStyle();
		valueStyle.setDataFormat(fmt.getFormat("#,##0.00"));

		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);

		headerStyle.setFont(font);
		AlphaGammaEpsilonCellStyle.setFont(font);
		valueStyle.setFont(font);
		
		//header part
		createExcelHeader(sheet, headerStyle, initialCost);
		
		//content
		for(int i = 0; i < QLearningAnalyser.NB_STEP_TEST; i++) {
			addLine(sheet, AlphaGammaEpsilonCellStyle, valueStyle, i,
					costResultAlpha[i], costResultGamma[i], costResultEpsilon[i],
					timeResultAlpha[i], timeResultGamma[i], timeResultEpsilon[i]);
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
		//alpha
		Cell c = header.createCell(1);
		c.setCellValue(alphaHeader);
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
		//gamma
		c = header.createCell(5);
		c.setCellValue(gammaHeader);
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
		//epsilon
		c = header.createCell(9);
		c.setCellValue(epsilonHeader);
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
	
	private static void addLine(Sheet sheet, CellStyle AlphaGammaEpsilonCellStyle, CellStyle valueStyle, int rowNum,
			double costResultAlpha, double costResultGamma, double costResultEpsilon,
			double timeResultAlpha, double timeResultGamma, double timeResultEpsilon) {
		
		Row row = sheet.createRow(rowNum + 2);
		
		//alpha
		Cell c = row.createCell(1);
		c.setCellValue(1 - (
				(1 - QLearningAnalyser.INIT_ALPHA) /
				(1 + rowNum * QLearningAnalyser.STEP_ALPHA)
				)
		);
		c.setCellStyle(AlphaGammaEpsilonCellStyle);
		c = row.createCell(2);
		c.setCellValue(costResultAlpha);
		c.setCellStyle(valueStyle);
		c = row.createCell(3);
		c.setCellValue(timeResultAlpha);
		c.setCellStyle(valueStyle);
		
		//gamma
		c = row.createCell(5);
		c.setCellValue(1 - (
				(1 - QLearningAnalyser.INIT_ALPHA) /
				(1 + rowNum * QLearningAnalyser.STEP_ALPHA)
				)
		);
		c.setCellStyle(AlphaGammaEpsilonCellStyle);
		c = row.createCell(6);
		c.setCellValue(costResultGamma);
		c.setCellStyle(valueStyle);
		c = row.createCell(7);
		c.setCellValue(timeResultGamma);
		c.setCellStyle(valueStyle);
		
		//epsilon
		c = row.createCell(9);
		c.setCellValue(1 - (
				(1 - QLearningAnalyser.INIT_EPSILON) /
				(1 + rowNum * QLearningAnalyser.STEP_EPSILON)
				)
		);
		c.setCellStyle(AlphaGammaEpsilonCellStyle);
		c = row.createCell(10);
		c.setCellValue(costResultEpsilon);
		c.setCellStyle(valueStyle);
		c = row.createCell(11);
		c.setCellValue(timeResultEpsilon);
		c.setCellStyle(valueStyle);
	}
}
