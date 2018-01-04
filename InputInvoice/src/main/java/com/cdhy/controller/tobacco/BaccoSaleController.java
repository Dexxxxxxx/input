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

import com.cdhy.entity.Result;
import com.cdhy.exception.BizException;
import com.cdhy.service.tobacco.IBaccoPurchaseService;
import com.cdhy.service.tobacco.IBaccoSaleService;

import net.sf.json.JSONObject;

@Controller
@Scope("prototype")
@RequestMapping("/tobacco/sale")
public class BaccoSaleController {
    private static final Logger log = Logger.getLogger(BaccoSaleController.class);
    @Autowired
    private IBaccoSaleService service;
    /**
     * 开票
     * 
     * @param param
     * @return
     */
    @RequestMapping("/doInvoice")
    @ResponseBody
    public Result doInvioice(@RequestBody Map<String, Object> param) {
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
     * 存数据
     * 
     * @param param
     * @return
     */
    @RequestMapping("/saveInvoice")
    @ResponseBody
    public Result saveInvioice(@RequestBody Map<String, Object> param) {
	try {
	   Map<String, Object> map = service.saveInvoice(param);
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
    
}
