package com.cdhy.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;


public class CreateWorkbook {
	
	/**
	 * 创建一个空白的Excel工作薄
	 * @param sheetName sheet页名称
	 * @param titleName 当前页的第一行名称
	 * @param 数据总条数
	 * @return
	 */
	public static HSSFWorkbook createWorkbook(String sheetName,String[] titleName,int count){
		/* 创建一个EXCEL文档对象 */
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建第一个sheet（页），并命名
		Sheet sheet = wb.createSheet(sheetName);
		// 创建第一行(标题)
		Row row1 = sheet.createRow((short) 0);
		// 创建第二行(数据统计与导出日期)
		Row row2 = sheet.createRow((short) 1);
		// 创建第三行(列名)
		Row row3 = sheet.createRow((short) 2);
		// 创建两种单元格格式
		CellStyle cs = wb.createCellStyle();
		CellStyle cs2 = wb.createCellStyle();
		CellStyle cs3 = wb.createCellStyle();//用于标题
		
		// 创建两种字体
		Font f = wb.createFont();
		Font f2 = wb.createFont();
		Font f3 = wb.createFont();//用于标题

		// 创建第一种字体样式（用于列名）
		f.setFontHeightInPoints((short) 10);
		f.setColor(IndexedColors.BLACK.getIndex());
		f.setBoldweight(Font.BOLDWEIGHT_BOLD);

		// 创建第二种字体样式（用于日期行）
		f2.setFontHeightInPoints((short) 10);
		f2.setColor(IndexedColors.BLACK.getIndex());
		
		// 创建第三种字体样式（用于标题）
		f3.setFontHeightInPoints((short) 15);
		f3.setColor(IndexedColors.BLACK.getIndex());
		f3.setBoldweight(Font.BOLDWEIGHT_BOLD);

		// 设置第一种单元格的样式（用于列名）
		cs.setFont(f);
		cs.setBorderLeft(CellStyle.BORDER_THIN);
		cs.setBorderRight(CellStyle.BORDER_THIN);
		cs.setBorderTop(CellStyle.BORDER_THIN);
		cs.setBorderBottom(CellStyle.BORDER_THIN);
		cs.setAlignment(CellStyle.ALIGN_CENTER);
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// 设置第二种单元格的样式（用于日期行）
		cs2.setFont(f2);
		cs2.setAlignment(CellStyle.ALIGN_CENTER);
		
		// 设置第三种单元格的样式（用于标题）
		cs3.setFont(f3);
		cs3.setAlignment(CellStyle.ALIGN_CENTER);
		
		//设置第一行(标题)
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (int) titleName.length-1));
		row1.setHeight((short)500);
		Cell cell = row1.createCell((short) 0);
		cell.setCellValue(sheetName);
		cell.setCellStyle(cs3);
		//设置第二行(数据统计与日期 )
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, (int) titleName.length-1));
		Cell cell1 = row2.createCell((short) 0);
		cell1.setCellValue("导出日期：" + DateUtil.getCurrentTimeString()+"              "+"数据总条数：" + count + "条");
		cell1.setCellStyle(cs2);
		// 设置第三行(列名)
		for (int i = 0; i < titleName.length; i++) {
			row3.setHeight((short)500);
			cell = row3.createCell((short) i);
			cell.setCellValue(titleName[i]);
			cell.setCellStyle(cs);
		}
		return wb;
	}
	public static void createHead(HSSFWorkbook wb,Sheet sheet,String sheetName,String[] titleName,int count){
		
		// 创建第一行(标题)
		Row row1 = sheet.createRow((short) 0);
		// 创建第二行(数据统计与导出日期)
		Row row2 = sheet.createRow((short) 1);
		// 创建第三行(列名)
		Row row3 = sheet.createRow((short) 2);
		// 创建两种单元格格式
		CellStyle cs = wb.createCellStyle();
		CellStyle cs2 = wb.createCellStyle();
		CellStyle cs3 = wb.createCellStyle();//用于标题
		
		// 创建两种字体
		Font f = wb.createFont();
		Font f2 = wb.createFont();
		Font f3 = wb.createFont();//用于标题

		// 创建第一种字体样式（用于列名）
		f.setFontHeightInPoints((short) 10);
		f.setColor(IndexedColors.BLACK.getIndex());
		f.setBoldweight(Font.BOLDWEIGHT_BOLD);

		// 创建第二种字体样式（用于日期行）
		f2.setFontHeightInPoints((short) 10);
		f2.setColor(IndexedColors.BLACK.getIndex());
		
		// 创建第三种字体样式（用于标题）
		f3.setFontHeightInPoints((short) 15);
		f3.setColor(IndexedColors.BLACK.getIndex());
		f3.setBoldweight(Font.BOLDWEIGHT_BOLD);

		// 设置第一种单元格的样式（用于列名）
		cs.setFont(f);
		cs.setBorderLeft(CellStyle.BORDER_THIN);
		cs.setBorderRight(CellStyle.BORDER_THIN);
		cs.setBorderTop(CellStyle.BORDER_THIN);
		cs.setBorderBottom(CellStyle.BORDER_THIN);
		cs.setAlignment(CellStyle.ALIGN_CENTER);
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		// 设置第二种单元格的样式（用于日期行）
		cs2.setFont(f2);
		cs2.setAlignment(CellStyle.ALIGN_CENTER);
		
		// 设置第三种单元格的样式（用于标题）
		cs3.setFont(f3);
		cs3.setAlignment(CellStyle.ALIGN_CENTER);
		
		//设置第一行(标题)
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (int) titleName.length-1));
		row1.setHeight((short)500);
		Cell cell = row1.createCell((short) 0);
		cell.setCellValue(sheetName);
		cell.setCellStyle(cs3);
		//设置第二行(数据统计与日期 )
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, (int) titleName.length-1));
		Cell cell1 = row2.createCell((short) 0);
		cell1.setCellValue("导出日期：" + DateUtil.getCurrentTimeString()+"              "+"数据总条数：" + count + "条");
		cell1.setCellStyle(cs2);
		// 设置第三行(列名)
		for (int i = 0; i < titleName.length; i++) {
			row3.setHeight((short)500);
			cell = row3.createCell((short) i);
			cell.setCellValue(titleName[i]);
			cell.setCellStyle(cs);
		}
		
	}
}
