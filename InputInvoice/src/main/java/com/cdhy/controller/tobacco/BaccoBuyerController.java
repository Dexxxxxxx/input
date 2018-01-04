package com.cdhy.controller.tobacco;

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
import com.cdhy.service.tobacco.IBaccoBuyerService;
import com.cdhy.util.MapUtil;

@Controller
@Scope("prototype")
@RequestMapping("/tobacco/buyer")
public class BaccoBuyerController {
    private static final Logger log = Logger.getLogger(BaccoBuyerController.class);
    @Autowired
    private IBaccoBuyerService service;
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
			service.addBuyer(param);
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
			int n=service.delBuyer(param);
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
			service.updateBuyer(param);
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
	 * 获取单条数据信息
	 * @param param
	 * @return
	 */
	@RequestMapping("/checkAndSave")
	@ResponseBody
	public Result checkAndSave(@RequestParam Map<String, Object> param){
	    param.put("name", MapUtil.getString(param, "newbuyername"));
	    param.put("nsrsbh", MapUtil.getString(param, "newbuyernsrsbh"));
	    param.put("address", MapUtil.getString(param, "newbuyeraddress"));
	    param.put("phone", MapUtil.getString(param, "newbuyerphone"));
	    param.put("khh", MapUtil.getString(param, "newbuyerkhh"));
	    param.put("yhzh", MapUtil.getString(param, "newbuyeryhzh"));
	    Result rs = new Result();
		try {
		    Map<String, Object> map = service.checkAndSave(param);
		    rs.setRows(map);
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
	
}
