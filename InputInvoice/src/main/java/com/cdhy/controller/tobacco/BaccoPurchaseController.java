package com.cdhy.controller.tobacco;

import java.util.List;
import java.util.Map;

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
import com.cdhy.service.tobacco.IBaccoPurchaseService;

@Controller
@Scope("prototype")
@RequestMapping("/tobacco/purchase")
public class BaccoPurchaseController {
    private static final Logger log = Logger.getLogger(BaccoPurchaseController.class);
    @Autowired
    private IBaccoPurchaseService service;

    /**
     * 批量开票
     * 
     * @param param
     * @return
     */
    @RequestMapping("/doInvoice")
    @ResponseBody
    public Result doInvioice(@RequestParam Map<String, Object> param) {
	Result rs = new Result();
	try {
	    List<Map<String, Object>> list = service.doInvoice(param);
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
    /**
     * 单条开票
     * @param param
     * @return
     */
    @RequestMapping("/doInvoice1")
    @ResponseBody
    public Result doInvioice1(@RequestParam Map<String, Object> param) {
	Result rs = new Result();
	try {
	    List<Map<String, Object>> list = service.doInvoice1(param);
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
    /**
     * 单张开票
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
     * 作废发票重开
     * 
     * @param param
     * @return
     */
    @RequestMapping("/redoInvoice")
    @ResponseBody
    public Result reInvioice(@RequestParam Map<String, Object> param) {
	Result rs = new Result();
	try {
	    Map<String, Object> map = service.redoInvoice(param);
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
     * 回滚开票信息
     * @param param
     * @return
     */
    @RequestMapping("/rollBack")
    @ResponseBody
    public Result rollBack(@RequestParam Map<String, Object> param) {
	Result rs = new Result();
	try {
	    service.rollback(param);
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
     * 保存发票信息
     * 
     * @param param
     * @return
     */
    @RequestMapping("/saveInvoice")
    @ResponseBody
    public Result saveInvioice(@RequestBody Map<String, Object> param) {
	try {
	   Map<String, Object> map= service.saveInvoice(param);
	   return new Result("开票成功", Result.RESULT_SUCCESS, map);
	} catch (BizException ex) {
	    log.debug(ex);
	    ex.printStackTrace();
	    return new Result(ex.getMessage(), Result.RESULT_ERROR_MSG, "");
	} catch (Exception e) {
	    log.error(e);
	    e.printStackTrace();
	    return new Result(e.getMessage(), Result.RESULT_ERROR_MSG, "");
	}
	
    }
    /**
     * 获取纳税人树形
     * 
     * @param param
     * @return
     */
    @RequestMapping("/getNsrTree")
    @ResponseBody
    public Result getTree(@RequestParam Map<String, Object> param) {
	Result rs = new Result();
	List<Map<String, Object>> list = service.getTree(param);
	rs.setRows(list);
	return rs;
    }
    
    

}
