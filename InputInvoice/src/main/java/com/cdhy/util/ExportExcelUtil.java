package com.cdhy.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;



public class ExportExcelUtil {
	 private static final Log logger = LogFactory.getLog(ExportExcelUtil.class);
	/**
	 * 获取生成的Excel文件路径
	 * @param wb 保存的文件
	 * @param ExcelName 文件名
	 * @return
	 */
	public static String getPath(HSSFWorkbook wb,String ExcelName){
		/* 将生成的EXCEL文件保存到指定位置 */
		String realPath = ContextHolderUtils.getSession().getServletContext().getRealPath("/");
		
		String ss = String.valueOf(System.nanoTime());
		int random = (int) (Math.random() * 1000000);
		String path = realPath + "temp" + File.separator + ExcelName+ss + "_" + random;
		File directory = new File(realPath + "temp");
        if (!directory.exists()) {  
            directory.mkdirs();  
        } 
		// 导出文件的名称
		String exportPath = path + ".xls";
		System.out.println(exportPath);
		File file = new File(exportPath);
        if (!file.isFile()) {  
        	try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}  
        } 
		try {
			FileOutputStream fos = new FileOutputStream(exportPath);
			wb.write(fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exportPath;
		
	}
	
	 public static void downloadFile(HttpServletResponse response,String excelName, String path) {
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			File f = null;
			try {
			    f = new File(path);
			    response.setContentType("application/x-excel");
			    response.setCharacterEncoding("UTF-8");
			    String ss = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			    int random = (int) (Math.random() * 10000);
			    String fileName = excelName+ss + "_" + random   + ".xls";
			    response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			    response.setHeader("Content-Length", String.valueOf(f.length()));
			    in = new BufferedInputStream(new FileInputStream(f));
			    out = new BufferedOutputStream(response.getOutputStream());
			    byte[] data = new byte[1024];
			    int len = 0;
			    while (-1 != (len = in.read(data, 0, data.length))) {
			    	out.write(data, 0, len);
			    }
			    out.flush();
			} catch (Exception e) {
			    logger.error(e);
			} finally {
			    try {
				if (in != null) {
				    in.close();
				}
				if (out != null) {
				    out.close();
				}
			    } catch (IOException e) {
				logger.error(e);
			    }
			    try {
				if (null != f && f.exists()) {
				    f.delete();
				}
			    } catch (Exception e) {
				logger.error(e);
			    }
			}
		    }

}
