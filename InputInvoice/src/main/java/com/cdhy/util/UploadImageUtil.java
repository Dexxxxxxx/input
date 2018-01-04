package com.cdhy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * APP上传图片
 * 
 * @author dyc
 *
 */
public class UploadImageUtil {
    private static Log logger = LogFactory.getLog(UploadImageUtil.class);

    public static String saveImageToService(MultipartFile file, Map<String, Object> map) {

    // 获取配置文件的存放路径设置
    String pathName = PropertiesUtil.getInstance().getProperites("com_cred_path");
    // 获取原文件名字
    String fileName = file.getOriginalFilename();
    // 获取公司名称
    String comName = MapUtil.getString(map, "nsrsbh");
    // 拼装文件名字
    String extName = MapUtil.getString(map, "userid") + "_" + MapUtil.getString(map, "nsrsbh") + "_"
	    + MapUtil.getString(map, "zjlx") + fileName.substring(fileName.lastIndexOf("."));
    String path = pathName + File.separator + comName;
    // 创建目录
    if (!new File(path).exists()) {
	new File(path).mkdirs();
    }
    logger.debug("文件存放路径：" + path);
    // 存入文件
    InputStream is = null;
    OutputStream os = null;
    try {
	logger.debug("================【文件存储开始】==================");
	is = file.getInputStream();
	os = new FileOutputStream(path + File.separator + extName);
	byte[] b = new byte[2048];
	while (is.read(b) != -1) {
	    os.write(b, 0, b.length);
	}
	logger.debug("================【文件存储结束】==================");
    } catch (Exception e) {
	logger.debug("================【文件存储失败】==================");
	e.printStackTrace();
    } finally {
	try {
	    if (is != null) {
		is.close();
	    }
	    if (os != null) {
		os.flush();
		os.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    return path + File.separator + extName;
    }

    /**
     * ======================================以下方法都是之前系统使用=======================
     * ======================
     */
    // 将图片转存服务器路径，并返回路径
    public static String saveImageToSer(File f, String extensionName, String comName) {
    String properites = PropertiesUtil.getInstance().getProperites("com_cred_path");// 证件配置地址
										    // 这是一个路径
    String trueFile = properites + File.separator + comName + File.separator + extensionName;
    String localPath = f.getPath();
    File file = new File(properites + File.separator + comName);
    if (!file.exists()) {
	file.mkdirs();
    }
    InputStream is = null;
    OutputStream os = null;
    try {
	is = new FileInputStream(localPath);
	os = new FileOutputStream(trueFile);
	byte[] b = new byte[1024];
	while (is.read(b) != -1) {
	    os.write(b, 0, b.length);
	}
    } catch (Exception e) {
	logger.debug("================【没有读取到缓存文件】【没有读取到缓存文件】【没有读取到缓存文件】==================");
	trueFile = "";
	e.printStackTrace();
    } finally {
	try {
	    if (is != null) {
		is.close();
	    }
	    if (os != null) {
		os.flush();
		os.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    if (new File(localPath).exists()) {
	new File(localPath).delete();
    }
    return File.separator + comName + File.separator + trueFile.substring(trueFile.lastIndexOf(File.separator) + 1);
    }

    // 上传
    public static void uploadImagetoLoacl(MultipartFile file, Map<String, Object> map) {
    String fileName = null;
    String extensionName = null;
    // String pathName = "tmp//picture";//以前做的缓存，现在直接存至服务器
    String pathName = PropertiesUtil.getInstance().getProperites("com_cred_path");
    InputStream is = null;
    OutputStream os = null;
    try {
	File dir = ResourceUtils.getFile("classpath:conf");
	if (!dir.exists())
	    dir.mkdirs();

	fileName = file.getOriginalFilename();
	extensionName = MapUtil.getString(map, "userid") + "_" + MapUtil.getString(map, "nsrsbh") + "_"
		+ MapUtil.getString(map, "type") + "_" + MapUtil.getString(map, "zjlx")
		+ fileName.substring(fileName.lastIndexOf("."));
	File f = new File(dir.getAbsolutePath() + File.separator + pathName);
	logger.debug("========================【存放路径】===========================");
	logger.debug(f.getPath());
	logger.debug("========================【存放路径】===========================");
	if (!f.exists()) {
	    f.mkdirs();
	}
	is = file.getInputStream();
	os = new FileOutputStream(f + File.separator + extensionName);
	byte[] b = new byte[1024];
	while (is.read(b) != -1) {
	    os.write(b, 0, b.length);
	}

    } catch (Exception e) {
	e.printStackTrace();
    } finally {
	try {
	    if (is != null) {
		is.close();
	    }
	    if (os != null) {
		os.flush();
		os.close();
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    }

    // 这个方法移至 DownController.class类中
    // 下载
    public static void downloadImagetoLoacl(String filePath, HttpServletResponse resp) {
    logger.debug("==================================图片信息下载请求开始====================================");
    InputStream is = null;
    OutputStream os = null;
    try {
	resp.setCharacterEncoding("utf-8");
	resp.setContentType("image/" + filePath.substring(filePath.lastIndexOf(".") + 1));
	resp.setHeader("Content-Disposition", "attachment;fileName=" + filePath.substring(filePath.indexOf("_") + 1));
	is = new FileInputStream(new File(filePath));
	os = resp.getOutputStream();
	byte[] b = new byte[2048];
	int length;
	while ((length = is.read(b)) > 0) {
	    os.write(b, 0, length);
	}
	os.flush();
    } catch (IOException e) {
	logger.debug("==================================图片信息下载失败====================================");
	e.printStackTrace();
	resp.setCharacterEncoding("utf-8");
	try {
	    resp.getWriter().print("File can't find or don't exist !");
	} catch (IOException e1) {
	    e1.printStackTrace();
	}
    } finally {
	if (os != null) {
	    try {
		os.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    os = null;
	}
	if (is != null) {
	    try {
		is.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    is = null;
	}
    }
    logger.debug("==================================图片信息下载请求结束====================================");
    }

}
