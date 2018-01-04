package com.cdhy.controller.invoice;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cdhy.entity.Page;
import com.cdhy.entity.Result;
import com.cdhy.exception.BizException;
import com.cdhy.service.invoice.IInvoiceService;

@Controller
@Scope("prototype")
@RequestMapping("/invoice")
public class InvoiceController {
    private static final Logger log = Logger.getLogger(InvoiceController.class);
    @Autowired
    private IInvoiceService service;
//烟草
    /**
     * 分页
     * 查询烟草发票开具数据列表
     * @param param
     * @return
     */
    @RequestMapping("/tobacco/listInvoice")
    @ResponseBody
    public Result listTobacco(@RequestParam Map<String, Object> param) {
	Page page = service.listTobaccoInvoice(param);
	return new Result("查询成功", Result.RESULT_SUCCESS, page.getData(), page.getRowsCount());
    }
    /**
     * 获取某条烟草开票信息
     * @param param
     * @return
     */
    @RequestMapping("/tobacco/getInvoiceDetail")
    @ResponseBody
    public Result getTobaccoInvoice(@RequestParam Map<String, Object> param) {
	List<Map<String,Object>> list = service.getTobaccoInvoiceDetail(param);
	return new Result("查询成功", Result.RESULT_SUCCESS, list);
    }
    
    
//卷烟
    /**
     * 分页
     * 查询卷烟发票开具数据列表
     * @param param
     * @return
     */
    @RequestMapping("/cigarette/listInvoice")
    @ResponseBody
    public Result listCigarette(@RequestParam Map<String, Object> param) {
	Page page = service.listTobaccoInvoice(param);
	return new Result("查询成功", Result.RESULT_SUCCESS, page.getData(), page.getRowsCount());
    }
    /**
     * 获取某条卷烟开票信息
     * @param param
     * @return
     */
    @RequestMapping("/cigarette/getInvoiceDetail")
    @ResponseBody
    public Result getCigaretteInvoice(@RequestParam Map<String, Object> param) {
	List<Map<String,Object>> list = service.getTobaccoInvoiceDetail(param);
	return new Result("查询成功", Result.RESULT_SUCCESS, list);
    }
    
    
    @RequestMapping("/cancelInvoice")
    @ResponseBody
    public Result cancelInvoice(@RequestParam Map<String, Object> param) {
//	List<Map<String,Object>> list = service.getTobaccoInvoiceDetail(param);
//	return new Result("查询成功", Result.RESULT_SUCCESS, list);
	return null;
    }
   
}
