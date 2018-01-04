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
import com.cdhy.service.security.IDepartService;
@Controller
@Scope("prototype")
@RequestMapping("/security/depart")
public class DepartController {
	private static final Logger log = Logger.getLogger(DepartController.class);
	@Autowired
	private IDepartService service;
	/**
	 * 分页查询
	 * @param param
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Result list(@RequestParam Map<String, Object> param){
		Page page=service.list(param);
		return new Result("查询成功", Result.RESULT_SUCCESS, page.getData(),page.getRowsCount());
	}
	/**
	 * 增加
	 * @param param
	 * @return
	 */
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
	 * 修改
	 * @param param
	 * @return
	 */
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
	/**
	 * 获取单条数据信息
	 * @param param
	 * @return
	 */
	@RequestMapping("/getById")
	@ResponseBody
	public Result getById(@RequestParam Map<String, Object> param){
		Result rs = new Result();
			Map<String, Object> map = service.getById(param);
			rs.setRows(map);
		return rs;
	}
	/**
	 * 获取组织架构树形
	 * @param param
	 * @return
	 */
	@RequestMapping("/getTree")
	@ResponseBody
	public Result getTree(@RequestParam Map<String, Object> param){
		Result rs = new Result();
		List<Map<String, Object>> list = service.getTree(param);
		rs.setRows(list);
		return rs;
	}
	
}
