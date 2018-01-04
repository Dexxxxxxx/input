package com.cdhy.controller.cigarette;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cdhy.controller.security.LoginController;
import com.cdhy.dao.cigarette.CigarInvoiceMapper;
import com.cdhy.dao.cigarette.CigarOrigMapper;
import com.cdhy.dao.invoice.IInvoiceMapper;
import com.cdhy.entity.Page;
import com.cdhy.entity.Result;
import com.cdhy.exception.BizException;
import com.cdhy.service.cigarette.ICigarInvoiceService;
import com.cdhy.service.cigarette.ICommonsService;
import com.cdhy.util.Base64Util;
import com.cdhy.util.ContextHolderUtils;
import com.cdhy.util.MapUtil;
import com.cdhy.util.MessageUtil;
import com.cdhy.util.XMLUtil_TaxServer;
import com.cdhy.util.XMLUtil_TaxServer2;
import com.cdhy.util.XmlResult;
import com.framework.util.ResourceUtil;

/**
 * 卷烟发票管理
 * 
 * @author dxg
 *
 */
@Controller
@Scope("prototype")
@RequestMapping("/cigarette/common")
public class CigarCommonController {
	private static final Logger log = Logger.getLogger(CigarCommonController.class);
	// 统一常量
	private static final String SERVLETIP = ResourceUtil.getDefaultServletIp();
	private static final String KEYPWD = ResourceUtil.getDefaultKeyPwd();
	private static final String SERVLETPORT = ResourceUtil.getDefaultServletPort();
	@Autowired
	private CigarInvoiceMapper invoicemapper;
	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/setting")
	@ResponseBody
	public Result setting(@RequestParam Map<String, Object> param) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("servletip", SERVLETIP);
		map.put("servletport", SERVLETPORT);
		map.put("keypwd", KEYPWD);
		Map<String, Object> result = (Map<String, Object>) ContextHolderUtils.getSession()
			.getAttribute(LoginController.LOGIN_USER);
		Map<String, Object> dept = (Map<String, Object>) result.get("dept");
		Result rs = new Result();
		Map<String, Object> mm = new HashMap<String,Object>();
		try {
			String setting = XMLUtil_TaxServer2.createSetingXmlWithMap(map);
			mm.put("xml", setting);
			mm.put("kpzdbs", MapUtil.getString(dept, "KPZDBS"));
			rs.setRows(mm);
		} catch (BizException ex) {
			log.debug(ex);
			ex.printStackTrace();
			rs = Result.gerErrorResult(ex.getMessage());
		} catch (Exception e) {
			log.debug(e);
			e.printStackTrace();
			rs = Result.gerErrorResult(e.getMessage());
		}
		return rs;
	}
	/**
	     * 检测是否打印成功
	     * 
	     * @param param
	     * @return
	     */
	    @RequestMapping("/checkPrint")
	    @ResponseBody
	    public Result printReturn(@RequestBody Map<String, Object> param) {
		//修改发票的打印状态
		param.put("dystatus", "1");
		invoicemapper.updateDyStatus(param);
		return null;
	    }
	
}
