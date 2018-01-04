package com.cdhy.controller.invoiceStatistics;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cdhy.entity.Page;
import com.cdhy.entity.Result;
import com.cdhy.service.invoiceStatistics.IInvoiceInfoService;
import com.cdhy.util.ExportExcelUtil;
@Controller
@Scope("prototype")
@RequestMapping("/invoiceStatistics/invoiceInfo")
public class InvoiceInfoController {
	
	private static final Logger log = Logger.getLogger(InvoiceInfoController.class);
	@Autowired
	private IInvoiceInfoService service;
	/**
	 * 发票信息分页查询
	 * @param param
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Result list(@RequestParam Map<String, Object> param){

		Map<String, Object> map = service.list(param);

		Page page=(Page) map.get("page");
		return new Result("查询成功", Result.RESULT_SUCCESS, map, page.getRowsCount());
		
	}
	
	@RequestMapping("/list2")
	@ResponseBody
	public Result list2(@RequestParam Map<String, Object> param){

		Map<String, Object> map = service.list2(param);

		Page page=(Page) map.get("page");
		return new Result("查询成功", Result.RESULT_SUCCESS, map, page.getRowsCount());
		
	}
	
	@RequestMapping("/listAll")
	@ResponseBody
	public Result listAll(@RequestParam Map<String, Object> param){

		Map<String, Object> map = service.listAll(param);
		Page page=(Page) map.get("page");
		return new Result("查询成功", Result.RESULT_SUCCESS, map, page.getRowsCount());
	}
	@RequestMapping("/listCollect")
	@ResponseBody
	public Result listCollect(@RequestParam Map<String, Object> param){
		List<Map<String, Object>> list = service.listCollect(param);
		return new Result("查询成功", Result.RESULT_SUCCESS, list);
	}
	
	@RequestMapping("/listDep")
	@ResponseBody
	public Result listDep(@RequestParam Map<String, Object> param){
		Page page=service.listDep(param);
		return new Result("查询成功", Result.RESULT_SUCCESS, page.getData(),page.getRowsCount());
	}
	@RequestMapping("/listSupplier")
	@ResponseBody
	public Result listSupplier(@RequestParam Map<String, Object> param){
		Page page=service.listSupplier(param);
		return new Result("查询成功", Result.RESULT_SUCCESS, page.getData(),page.getRowsCount());
	}
	
//	 @RequestMapping("/listAll")
//	    @ResponseBody
//	    public Result listAll(@RequestParam Map<String, Object> parm) {
//		Map<String, Object> map = service.listAll(parm);
//		Page page=(Page) map.get("page");
//		return new Result("查询成功", Result.RESULT_SUCCESS, map, page.getRowsCount());
//	    }
	
	/**
	 * 商品信息分页查询
	 * @param param
	 * @return
	 */
	@RequestMapping("/detailList")
	@ResponseBody
	public Result detailList(@RequestParam Map<String, Object> param){
		Page page=service.detailList(param);
		return new Result("查询成功", Result.RESULT_SUCCESS, page.getData(),page.getRowsCount());
	}
	
	/**
	 * 发票信息导出
	 * @param param
	 * @return
	 */
	@RequestMapping("/exportList")
	@ResponseBody
	public void exportList(@RequestParam Map<String, Object> param,HttpServletRequest request, HttpServletResponse response){
		try{
			param.put("emp_name",URLDecoder.decode((String)request.getParameter("emp_name"),"UTF-8"));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		  service.exportList(param, request, response);
	}
	@RequestMapping("/exportList2")
	@ResponseBody
	public void exportList2(@RequestParam Map<String, Object> param,HttpServletRequest request, HttpServletResponse response){
		try{
			param.put("emp_name",URLDecoder.decode((String)request.getParameter("emp_name"),"UTF-8"));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		  service.exportList2(param, request, response);
	}
	
	/**
	 * 发票信息导出
	 * @param param
	 * @return
	 */
	@RequestMapping("/exportDetailList")
	@ResponseBody
	public void exportDetailList(@RequestParam Map<String, Object> param,HttpServletRequest request, HttpServletResponse response){
		try{
			param.put("xmmc",URLDecoder.decode((String)request.getParameter("xmmc"),"UTF-8"));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		  service.exportDetailList(param, request, response);
	}
	


}
