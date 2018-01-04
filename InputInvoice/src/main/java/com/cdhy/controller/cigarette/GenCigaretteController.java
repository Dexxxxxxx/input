package com.cdhy.controller.cigarette;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cdhy.entity.Page;
import com.cdhy.entity.Result;
import com.cdhy.exception.BizException;
import com.cdhy.service.cigarette.ICigarOrigService;
import com.cdhy.service.cigarette.IECigaretteService;
import com.cdhy.service.cigarette.IGenCigaretteService;

@Controller
@Scope("prototype")
@RequestMapping("/cigarette/gencigarette")
public class GenCigaretteController {
	private static final Logger log = Logger.getLogger(GenCigaretteController.class);
	@Autowired
	private IGenCigaretteService service;

	/**
	 * 分页查询
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Result list(@RequestParam Map<String, Object> param) {
		Page page = service.list(param);
		return new Result("查询成功", Result.RESULT_SUCCESS, page.getData(), page.getRowsCount());
	}

	/**
	 * 获取单条数据信息
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("/getById")
	@ResponseBody
	public Result getById(@RequestParam Map<String, Object> param) {
		List<Map<String, Object>> list = service.getById(param);
		return new Result("查询成功", Result.RESULT_SUCCESS, list);
	}
	/**
	 * 获取待开发票的信息
	 * @param param
	 * @return
	 */
	@RequestMapping("/invoiceInfo")
	@ResponseBody
	public Result getInvoiceInfo(@RequestParam Map<String,Object> param){
		Result rs = new Result();
		try {
			List<Map<String,Object>> result_list = service.getInvoice(param);
			rs.setRows(result_list);
		} catch (BizException ex) {
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
	 * 保存发票信息（普票）
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("/saveInvoice")
	@ResponseBody
	public Result saveInvoice(@RequestBody Map<String, Object> param) {
		Result rs = new Result();
		try {
			Map<String, Object> map = service.saveInvoice(param);
			rs.setRows(map);
		} catch (BizException ex) {
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
	 * 开具发票
	 * @param param
	 * @return
	 */
	@RequestMapping("/doInvoice")
	@ResponseBody
	public Result doInvoice(@RequestParam Map<String, Object> param) {
		Result rs = new Result();
		String order_no = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date()) + new Random().nextInt(999);
		param.put("order_no", order_no);
		try {
			service.doInvoice(param);
			rs.setRows(order_no);
		} catch (BizException ex) {
			log.debug(ex);
			ex.printStackTrace();
			rs = Result.gerErrorResult(ex.getMessage());
			rs.setRows(order_no);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			rs = Result.gerErrorResult(Result.RESULT_ERROR_MSG);
			rs.setRows(order_no);
		}

		return rs;
	}
	

	/**
	 * 获取单张开票的数据源
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("/doInvoiceByOne")
	@ResponseBody
	public Result doInvoiceByOne(@RequestParam Map<String, Object> param) {
		Result rs = new Result();
		try {
			Map<String, Object> map = service.doInvoiceByOne(param);
			rs.setRows(map);
		} catch (BizException ex) {
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
	 * 重新开票
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("/reDoInvoice")
	@ResponseBody
	public Result reDoInvoice(@RequestParam Map<String, Object> param) {
		Result rs = new Result();
		try {
			Map<String, Object> map = service.reDoInvoice(param);
			rs.setRows(map);
		} catch (BizException ex) {
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
	
	@RequestMapping("/getInvoiceList")
	@ResponseBody
	public Result getInvoiceList(@RequestBody Map<String, Object> param) {
		Result rs = new Result();
		try {
			List<Map<String, Object>> list = service.getInvoiceList(param);
			rs.setRows(list);
		} catch (BizException ex) {
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
	
	

}
