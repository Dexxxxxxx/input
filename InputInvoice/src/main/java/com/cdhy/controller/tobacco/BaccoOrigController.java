package com.cdhy.controller.tobacco;

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
import com.cdhy.service.tobacco.IBaccoOrigService;

@Controller
@Scope("prototype")
@RequestMapping("/tobacco/orig")
public class BaccoOrigController {
    private static final Logger log = Logger.getLogger(BaccoOrigController.class);
    @Autowired
    private IBaccoOrigService service;

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
    @RequestMapping("/listDetail")
    @ResponseBody
    public Result listDetail(@RequestParam Map<String, Object> param) {
	List<Map<String, Object>> list = service.listDetail(param);
	return new Result("查询成功", Result.RESULT_SUCCESS, list);
    }

}
