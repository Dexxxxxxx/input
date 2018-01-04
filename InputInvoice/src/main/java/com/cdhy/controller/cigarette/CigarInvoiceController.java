package com.cdhy.controller.cigarette;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.cdhy.service.cigarette.ICigarInvoiceService;

/**
 * 卷烟发票管理
 * 
 * @author dxg
 *
 */
@Controller
@Scope("prototype")
@RequestMapping("/cigarette/invoice")
public class CigarInvoiceController {
	private static final Logger log = Logger.getLogger(CigarInvoiceController.class);
	@Autowired
	private ICigarInvoiceService service;
	
	@RequestMapping("/listInvoiceDetail")
	@ResponseBody
	public Result getById(@RequestParam Map<String, Object> param) {
		List<Map<String, Object>> list = service.listInvoiceDetail(param);
		return new Result("查询成功", Result.RESULT_SUCCESS, list);
	}
	@RequestMapping("/list")
	@ResponseBody
        public Result list(@RequestParam Map<String, Object> param) {
    		Page page = service.list(param);
    		return new Result("查询成功", Result.RESULT_SUCCESS, page.getData(), page.getRowsCount());
        }
    
        /**
         * 执行作废
         * 
         * @param param
         * @return
         */
        @RequestMapping("/cancel")
        @ResponseBody
        public Result cancle(@RequestParam Map<String, Object> param) {
    		try {
    		    service.cancel(param);
    		} catch (BizException ex) {
    		    return new Result(ex.getMessage(), Result.RESULT_ERROR, "");
    		} catch (Exception e) {
    		    return new Result(Result.RESULT_ERROR_MSG, Result.RESULT_ERROR, "");
    		}
    		return new Result("作废操作成功", Result.RESULT_SUCCESS, "");
        }
        /**
         * 执行专普票冲红
         * 
         * @param param
         * @return
         */
        @RequestMapping("/redCancel")
        @ResponseBody
        public Result redCancel(@RequestBody Map<String, Object> param) {
    		try {
    		    service.redCancel(param);
    		} catch (BizException ex) {
    		    return new Result(ex.getMessage(), Result.RESULT_ERROR, "");
    		} catch (Exception e) {
    		    return new Result(Result.RESULT_ERROR_MSG, Result.RESULT_ERROR, "");
    		}
    		return new Result("作废操作成功", Result.RESULT_SUCCESS, "");
        }
        /**
         * 执行电票冲红
         * 
         * @param param
         * @return
         */
        @RequestMapping("/e_redCancel")
        @ResponseBody
        public Result e_redCancel(@RequestParam Map<String, Object> param) {
    		try {
    		    service.e_redCancel(param);
    		} catch (BizException ex) {
    		    return new Result(ex.getMessage(), Result.RESULT_ERROR, "");
    		} catch (Exception e) {
    		    return new Result(Result.RESULT_ERROR_MSG, Result.RESULT_ERROR, "");
    		}
    		return new Result("作废操作成功", Result.RESULT_SUCCESS, "");
        }
        @RequestMapping("/rollback")
        @ResponseBody
        public Result rollback(@RequestParam Map<String, Object> param) {
    		try {
    		    service.e_redCancel(param);
    		} catch (BizException ex) {
    		    return new Result(ex.getMessage(), Result.RESULT_ERROR, "");
    		} catch (Exception e) {
    		    return new Result(Result.RESULT_ERROR_MSG, Result.RESULT_ERROR, "");
    		}
    		return new Result("作废操作成功", Result.RESULT_SUCCESS, "");
        }
        

}
