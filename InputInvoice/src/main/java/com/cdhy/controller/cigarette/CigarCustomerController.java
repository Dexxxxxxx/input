package com.cdhy.controller.cigarette;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cdhy.entity.Page;
import com.cdhy.entity.Result;
import com.cdhy.exception.BizException;
import com.cdhy.service.cigarette.ICigarCustomerService;
import com.cdhy.service.cigarette.ICigarInvoiceService;
import com.cdhy.util.FileUploadUtil;

/**
 * 卷烟发票管理
 * 
 * @author dxg
 *
 */
@Controller
@Scope("prototype")
@RequestMapping("/cigarette/customer")
public class CigarCustomerController {
	private static final Logger log = Logger.getLogger(CigarCustomerController.class);
	@Autowired
	private ICigarCustomerService service;
	
	@RequestMapping("/getById")
	@ResponseBody
	public Result getById(@RequestParam Map<String, Object> param) {
		Map<String, Object> map = service.getById(param);
		return new Result("查询成功", Result.RESULT_SUCCESS, map);
	}
	@RequestMapping("/list")
	@ResponseBody
	public Result list(@RequestParam Map<String, Object> param){
		Page page=service.list(param);
		return new Result("查询成功", Result.RESULT_SUCCESS, page.getData(),page.getRowsCount());
	}
	@RequestMapping("/update")
	@ResponseBody
	public Result update(@RequestParam Map<String, Object> param){
	    Result rs = new Result();
		try {
			service.update(param);
		}catch(BizException ex){
			log.debug(ex);
			ex.printStackTrace();
			rs = Result.gerErrorResult(ex.getMessage());
		} catch (Exception e) {
			log.error(e);
		    e.printStackTrace();
		    rs = Result.gerErrorResult(Result.RESULT_ERROR_MSG);
		}
		return rs;
	}
	@RequestMapping("/add")
	@ResponseBody
	public Result add(@RequestParam Map<String, Object> param){
	    Result rs = new Result();
		try {
			service.add(param);
		}catch(BizException ex){
			log.debug(ex);
			ex.printStackTrace();
			rs = Result.gerErrorResult(ex.getMessage());
		} catch (Exception e) {
			log.error(e);
		    e.printStackTrace();
		    rs = Result.gerErrorResult(Result.RESULT_ERROR_MSG);
		}
		return rs;
	}
	/**
	 * 删除
	 * @param param
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Result delete(@RequestParam Map<String, Object> param){
		Result rs = new Result();
		try {
			int n=service.delete(param);
			rs.setMsg("成功删除" + n + "条");
		}catch(BizException ex){
			log.debug(ex);
			ex.printStackTrace();
			rs = Result.gerErrorResult(ex.getMessage());
		} catch (Exception e) {
			log.error(e);
		    e.printStackTrace();
		    rs = Result.gerErrorResult(Result.RESULT_ERROR_MSG);	
		}
		return rs;
	}
	/**
	 * 导入客户源数据
	 * @param param
	 * @return
	 */
	@RequestMapping("/import")
	@ResponseBody
	public ResponseEntity<String> saveBillsToDB(HttpServletRequest request, HttpServletResponse response) {
		// 上传文件，获取上传文件的存储地址
		HttpHeaders responseHeaders = new HttpHeaders();
		MediaType mediaType = new MediaType("text","html",Charset.forName("utf-8"));
		responseHeaders.setContentType(mediaType);
		String json = "";
		String filePath = FileUploadUtil.tranferFile(request, "WEB-INF/upload");
		if (filePath != "" && filePath != null) {
		    try {
			String result =service.saveBillsToDB(filePath);
			json = "Y" + result;
			return new ResponseEntity<String>(json, responseHeaders, HttpStatus.OK);
		    } catch (BizException ex) {
			json = "N"+ ex.getMessage();
			return new ResponseEntity<String>(json, responseHeaders, HttpStatus.OK);
		    }
		} else {
		    json = "N" + "请选择要导入的文件！";
		    return new ResponseEntity<String>(json, responseHeaders, HttpStatus.OK);
		}
	    }
}
