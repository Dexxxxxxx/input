package com.cdhy.service.invoiceStatistics.impl;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.cdhy.util.CreateWorkbook;
import com.cdhy.util.ExportExcelUtil;




public class ExportInvoiceInfo {

	
	/**
	 * 导出的字段名称
	 */
	public final String[] INVOICECHECK = {"录入人员","录入部门","手机号码","发票代码","发票号码","购买方","销售方","开票日期","查验日期","查验结果","商品名称","金额","税率","税额","价税合计"};
	public final String[] INVOICEDETAIL = {"发票代码","发票号码","商品名称","金额","税率","税额","价税合计"};


	

	
public String exportExcel(String sheetName, String excelName,List<Map<String, Object>> list) {
		
		HSSFWorkbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("发票商品信息");
		CreateWorkbook.createHead(wb,sheet, "发票商品信息", INVOICECHECK, list.size());
			//设置列宽
		for (int i = 0; i < 14; i++) {
			sheet.setColumnWidth(i, 30 * 256);
		}
		CellStyle cs2 = wb.getCellStyleAt((short) 1);
		
		// 设置每行每列的值
		String str = "";
		for (short i = 0; i < list.size(); i++) {
			int j = 0;
			// Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
			// 创建一行，在页sheet上
			Row row = sheet.createRow((short) i + 3);
			// 在row行上创建一个方格
			Map<String, Object> map = list.get(i);
			
			str = map.get("NAME") == null ? "" : (String) map.get("NAME");
			Cell cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("DPNAME") == null ? "" : (String) map.get("DPNAME");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
//			str = map.get("CUSER_NAME") == null ? "" : (String) map.get("CUSER_NAME");
//			cell = row.createCell((short) j++);
//			cell.setCellValue(str);
//			cell.setCellStyle(cs2);
//
//			str = map.get("BELONG_ENT_NAME") == null ? "" : (String) map.get("BELONG_ENT_NAME");
//			cell = row.createCell((short) j++);
//			cell.setCellValue(str);
//			cell.setCellStyle(cs2);
			
			str = map.get("CUSER") == null ? "" : (String) map.get("CUSER");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("CODE") == null ? "" : (String) map.get("CODE");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("NUM") == null ? "" : (String) map.get("NUM");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("PURCHASEUNIT") == null ? "" : (String) map.get("PURCHASEUNIT");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("DRAWERUNIT") == null ? "" : (String) map.get("DRAWERUNIT");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
	
			
			str = map.get("INDATE") == null ? "" : (String) map.get("INDATE");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("CHECKDATE") == null ? "" : (String) map.get("CHECKDATE");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("ISCHECK") == null ? "" : (String) map.get("ISCHECK");
			if("0".equals(str)){
				str = "已查验";
			}else if("1".equals(str)){
				str = "未查验";
			}else if("2".equals(str)){
				str = "创建报销未验证";
			}
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("XMMC") == null ? "" : (String) map.get("XMMC");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("XMJE") == null ? "" : (String) map.get("XMJE");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("SHUILV") == null ? "" : (String) map.get("SHUILV");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("SE") == null ? "" : (String) map.get("SE");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("JSHJ") == null ? "" : (String) map.get("JSHJ");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			
			
		}
	
		
		return ExportExcelUtil.getPath(wb, excelName);
	}

	public String exportExcel(String sheetName, List<Map<String, Object>> list,List<Map<String, Object>> list2) {
//		HSSFWorkbook wb = CreateWorkbook.createWorkbook(sheetName,INVOICECHECK, list.size());
//		Sheet sheet = wb.getSheet(sheetName);
		
		HSSFWorkbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("发票信息");
		Sheet detailSheet = wb.createSheet("发票明细");
		CreateWorkbook.createHead(wb,sheet, "发票信息", INVOICECHECK, list.size());
		CreateWorkbook.createHead(wb,detailSheet, "发票明细", INVOICEDETAIL, list2.size());
			//设置列宽
		for (int i = 0; i < 14; i++) {
			sheet.setColumnWidth(i, 30 * 256);
		}
		CellStyle cs2 = wb.getCellStyleAt((short) 1);
		
		// 设置每行每列的值
		String str = "";
		for (short i = 0; i < list.size(); i++) {
			int j = 0;
			// Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
			// 创建一行，在页sheet上
			Row row = sheet.createRow((short) i + 3);
			// 在row行上创建一个方格
			Map<String, Object> map = list.get(i);
			
			str = map.get("EMP_NAME") == null ? "" : (String) map.get("EMP_NAME");
			Cell cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("CUSER") == null ? "" : (String) map.get("CUSER");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("CODE") == null ? "" : (String) map.get("CODE");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("NUM") == null ? "" : (String) map.get("NUM");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("PURCHASEUNIT") == null ? "" : (String) map.get("PURCHASEUNIT");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("DRAWERUNIT") == null ? "" : (String) map.get("DRAWERUNIT");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
//			str = map.get("XMMC") == null ? "" : (String) map.get("XMMC");
//			cell = row.createCell((short) j++);
//			cell.setCellValue(str);
//			cell.setCellStyle(cs2);
			
			str = map.get("INDATE") == null ? "" : (String) map.get("INDATE");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("HJJE") == null ? "" : (String) map.get("HJJE");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("HJSE") == null ? "" : (String) map.get("HJSE");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("INAMOUNTS") == null ? "" : (String) map.get("INAMOUNTS");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("CHECKDATE") == null ? "" : (String) map.get("CHECKDATE");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("ISCHECK") == null ? "" : (String) map.get("ISCHECK");//企业类型
			if("0".equals(str)){
				str = "已查验";
			}else if("1".equals(str)){
				str = "未查验";
			}else if("2".equals(str)){
				str = "创建报销未验证";
			}
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("SYSTEM_NUM") == null ? "" : (String) map.get("SYSTEM_NUM");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
		}
		
		//发票明细
//		Sheet detailSheet = wb.getSheet("发票明细");
		for (int i = 0; i < 10; i++) {
			detailSheet.setColumnWidth(i, 30 * 256);
		}
		
		// 设置每行每列的值
		for (short i = 0; i < list2.size(); i++) {
			int j = 0;
			// Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
			// 创建一行，在页sheet上
			Row row = detailSheet.createRow((short) i + 3);
			// 在row行上创建一个方格
			Map<String, Object> map = list2.get(i);
			
			str = map.get("CODE") == null ? "" : (String) map.get("CODE");
			Cell cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("NUM") == null ? "" : (String) map.get("NUM");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("XMMC") == null ? "" : (String) map.get("XMMC");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("GGXH") == null ? "" : (String) map.get("GGXH");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("DW") == null ? "" : (String) map.get("DW");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("SHULIANG") == null ? "" : (String) map.get("SHULIANG");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("XMDJ") == null ? "" : (String) map.get("XMDJ");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("XMJE") == null ? "" : (String) map.get("XMJE");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("SHUILV") == null ? "" : (String) map.get("SHUILV");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);
			
			str = map.get("SE") == null ? "" : (String) map.get("SE");
			cell = row.createCell((short) j++);
			cell.setCellValue(str);
			cell.setCellStyle(cs2);

			
		}
		
		return ExportExcelUtil.getPath(wb, sheetName);
	}
	

	
	public String covertName(HttpServletRequest request, String filename) {
		try {
			if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
				filename = URLEncoder.encode(filename, "UTF-8");
			} else {
				filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
			}
		} catch (Exception e) {
		}
		return filename;
	}



}
