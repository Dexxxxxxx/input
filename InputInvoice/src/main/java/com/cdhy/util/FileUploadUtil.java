package com.cdhy.util;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 文件上传工具类
 * 参数1:HttpServletRequest request
 * 参数2:String storePath   文件存储相对路径 ,例如："/upload","/image","/local/file"
 * 返回值：上传到服务器的相对路径
 * @author lsq
 *
 */
public class FileUploadUtil {

    /**
     *
     * @param request
     * @param storePath 文件存储相对路径 ,例如："/upload","/image","/local/file"
     * @return
     */

	public static String tranferFile(HttpServletRequest request,String storePath){
		 CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		//先判断request中是否包涵multipart类型的数据
		if(multipartResolver.isMultipart(request)){
			//再将request中的数据转化成multipart类型的数据
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            while(iter.hasNext()){
            	MultipartFile file = multiRequest.getFile((String)iter.next());
                if(file != null){
                	String originalFileName = file.getOriginalFilename();
                	String path =request.getSession().getServletContext().getRealPath(storePath);
                	//得到存储到本地的文件名
                    String localFileName=System.currentTimeMillis()+getFileSuffix(originalFileName);
                    //新建本地文件
                    File localFile = new File(path,localFileName);
                    //创建目录
                    File fileDir=new File(path);
                    if (!fileDir.isDirectory()) {
                        // 如果目录不存在，则创建目录
                        fileDir.mkdirs();
                    }
                    String src=path+"/"+localFileName;
                    //写文件到本地
                    try{
                    	file.transferTo(localFile);
                        return src;
                    }catch(IllegalStateException e){
                    	e.printStackTrace();
                    }catch(IOException e){
                    	e.printStackTrace();
                    }
                }

            }

		}
		return null;
	}



    /**
     * 获取文件后缀
     * @param originalFileName
     * @return
     */
	public static String getFileSuffix(String originalFileName){
        int dot=originalFileName.lastIndexOf('.');
        if((dot>-1)&&(dot<originalFileName.length())){
            return originalFileName.substring(dot);
        }
        return originalFileName;
    }

}

