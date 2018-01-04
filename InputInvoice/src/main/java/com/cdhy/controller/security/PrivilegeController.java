package com.cdhy.controller.security;

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
import com.cdhy.service.security.IPrivilegeService;


/**
 * 权限
 * 
 * @Copyright (C),沪友科技
 * @author pjf
 * @Date:2015年10月27日
 */
@Controller
@Scope("prototype")
@RequestMapping("/security/privilege")
public class PrivilegeController {
    private static final Logger log = Logger.getLogger(DepartController.class);
    @Autowired
    private IPrivilegeService service;

    /**
     * 列表
     * 
     * @author pjf
     * @Date 2015年10月28日
     * @param parm
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> parm) {
	Page page = service.list(parm);
	return new Result("查询成功", Result.RESULT_SUCCESS, page.getData(), page.getRowsCount());
    }

    /**
     * 增加
     * 
     * @author pjf
     * @Date 2015年10月28日
     * @param parm
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Result add(@RequestParam Map<String, Object> parm) {
	Result rs = new Result();
	try {
	    service.add(parm);
	} catch (BizException ex) {
	    log.debug(ex);
	    ex.printStackTrace();
	    rs = Result.gerErrorResult(ex.getMessage());
	} catch (Exception e) {
	    log.error(e);
	    e.printStackTrace();
	    rs = Result.gerErrorResult("");
	}
	return rs;
    }

    /**
     * 删除
     * 
     * @author pjf
     * @Date 2015年10月28日
     * @param parm
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Result delete(@RequestParam Map<String, Object> parm) {
	Result rs = new Result();
	try {
	    int n = service.delete(parm);
	    rs.setMsg("成功删除" + n + "条");
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
     * 修改
     * 
     * @author pjf
     * @Date 2015年10月28日
     * @param parm
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public Result update(@RequestParam Map<String, Object> parm) {
	Result rs = new Result();
	try {
	    service.update(parm);
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
     * 获取单条记录
     * 
     * @author pjf
     * @Date 2015年10月28日
     * @param parm
     * @return
     */
    @RequestMapping("/getById")
    @ResponseBody
    public Result getById(@RequestParam Map<String, Object> parm) {
	Result rs = new Result();
	Map<String, Object> map = service.getById(parm);
	rs.setRows(map);
	return rs;
    }

    /**
     * 获取 tree数据
     * 
     * @author pjf
     * @Date 2015年10月28日
     * @param parm
     * @return
     */
    @RequestMapping("/getTree")
    @ResponseBody
    public Result getAllTree(@RequestParam Map<String, Object> parm) {
	Result rs = new Result();
	List<Map<String, Object>> list = service.getTree(parm);
	rs.setRows(list);
	return rs;
    }
    
}
