package com.cdhy.controller.invoice;

import java.io.UnsupportedEncodingException;
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
import com.cdhy.dao.invoice.IInvoiceMapper;
import com.cdhy.entity.Result;
import com.cdhy.exception.BizException;
import com.cdhy.util.ApacheBase64Util;
import com.cdhy.util.ContextHolderUtils;
import com.cdhy.util.MapUtil;
import com.cdhy.util.XmlResult;
import com.framework.util.ResourceUtil;

@Controller
@Scope("prototype")
@RequestMapping("/invoiceCall")
public class InvoiceCallController {
	public static void main(String[] args) throws UnsupportedEncodingException {
		String aa = "鍙傛暟璁剧疆";
		System.out.println(new String(aa.getBytes("gbk"), "utf-8"));
	}

	private static final Logger log = Logger.getLogger(InvoiceCallController.class);
	// 统一常量
	private static final String SERVLETIP = ResourceUtil.getDefaultServletIp();
	private static final String KEYPWD = ResourceUtil.getDefaultKeyPwd();
	private static final String SERVLETPORT = ResourceUtil.getDefaultServletPort();
	@Autowired
	private IInvoiceMapper invoicemapper;

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("/setting")
	@ResponseBody
	public Result setting(@RequestParam Map<String, Object> param) {
		// "<?xml version=\"1.0\" encoding=\"gbk\"?>\r\n<business
		// id=\"20001\"comment=\"参数设置\">\r\n<body><yylxdm=\"1\">\r\n<servletip>118.123.249.141</servletip>\r\n<servletport>70</servletport>\r\n<keypwd>88888888</keypwd>\r\n</body>\r\n</business>";
		String yylxdm = "1";
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>");
		sb.append("<business id=\"20001\" comment=\"参数设置\">");
		sb.append("<body yylxdm=\"" + yylxdm + "\">");
		sb.append("<servletip>" + SERVLETIP + "</servletip>");
		sb.append("<servletport>" + SERVLETPORT + "</servletport>");
		sb.append("<keypwd>" + KEYPWD + "</keypwd>");
		sb.append("</body></business>");
		// String base64Str = "";
		// base64Str = ApacheBase64Util.Base64Encode(sb.toString());
		// return new Result("成功", Result.RESULT_SUCCESS, base64Str);
		return new Result("成功", Result.RESULT_SUCCESS, sb.toString());
	}

	/**
	 * 参数设置校验
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("/settingCheck")
	@ResponseBody
	public Result settingCheck(@RequestBody Map<String, Object> param) {
		Map<String, Object> map = XmlResult.check(param);
		String returncode = MapUtil.getString(map, "returncode");
		String returnmsg = MapUtil.getString(map, "returnmsg");
		if (!returncode.equals("0")) {
			if (returnmsg.equals("")) {
				return new Result("参数设置失败", Result.RESULT_ERROR, "");
			} else {

				return new Result(returnmsg, Result.RESULT_ERROR, "");
			}
		}
		return new Result("参数设置成功", Result.RESULT_SUCCESS, "");
	}

	/**
	 * 查询未开票
	 * 
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/checkInvoice")
	@ResponseBody
	public Result checkInvoice(@RequestParam Map<String, Object> param) {
		// "<?xml version=\"1.0\" encoding=\"gbk\"?>\r\n<business id=\"10004\"
		// comment=\"查询当前未开票号\">\r\n<body
		// yylxdm=\"1\"><kpzdbs>51010221</kpzdbs>\r\n<fplxdm>007</fplxdm>\r\n</body>\r\n</business>";
		Map<String, Object> result = (Map<String, Object>) ContextHolderUtils.getSession()
				.getAttribute(LoginController.LOGIN_USER);
		Map<String, Object> dept = (Map<String, Object>) result.get("dept");

		String yylxdm = "1";
		String kpzdbs = MapUtil.getString(dept, "KPZDBS");
		String fplxdm = MapUtil.getString(param, "fplxdm");
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>");
		sb.append("<business id=\"10004\" comment=\"查询当前未开票号\">");
		sb.append("<body yylxdm=\"" + yylxdm + "\">");
		sb.append("<kpzdbs>" + kpzdbs + "</kpzdbs>");
		sb.append("<fplxdm>" + fplxdm + "</fplxdm>");
		sb.append("</body></business>");
		// String base64Str = "";
		// base64Str = ApacheBase64Util.Base64Encode(sb.toString());
		// return new Result("成功", Result.RESULT_SUCCESS, base64Str);
		return new Result("成功", Result.RESULT_SUCCESS, sb.toString());
	}

	/**
	 * 查询未开票
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("/checkInvoiceBack")
	@ResponseBody
	public Result checkInvoiceBack(@RequestBody Map<String, Object> param) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = XmlResult.check(param);
		} catch (Exception e) {
			return new Result(e.getMessage(), Result.RESULT_ERROR, "");
		}
		String returncode = MapUtil.getString(map, "returncode");
		String returnmsg = MapUtil.getString(map, "returnmsg");
		if (!returncode.equals("0")) {
			if (returnmsg.equals("")) {
				return new Result("查看当前票失败", Result.RESULT_ERROR, "");
			} else {
				return new Result(returnmsg, Result.RESULT_ERROR, "");
			}
		}
		return new Result("查看当前票成功", Result.RESULT_SUCCESS, "");
	}

	/**
	 * 执行开票
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("/execute")
	@ResponseBody
	public Result execute(@RequestParam Map<String, Object> param) {

		return null;
	}

	/**
	 * 执行作废
	 * 
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/cancel")
	@ResponseBody
	public Result cancle(@RequestParam Map<String, Object> param) {
		Map<String, Object> result = (Map<String, Object>) ContextHolderUtils.getSession()
				.getAttribute(LoginController.LOGIN_USER);
		Map<String, Object> dept = (Map<String, Object>) result.get("dept");
		Map<String, Object> user = (Map<String, Object>) result.get("user");
		String yylxdm = "1";
		String kpzdbs = MapUtil.getString(dept, "KPZDBS");
		String fplxdm = MapUtil.getString(param, "fplxdm");
		String fpdm = MapUtil.getString(param, "fpdm");
		String fphm = MapUtil.getString(param, "fphm");
		String hjje = MapUtil.getString(param, "hjje");
		String zfr = MapUtil.getString(user, "USERNAME");
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>");
		sb.append("<business id=\"10009\" comment=\"发票作废\">");
		sb.append("<body yylxdm=\"" + yylxdm + "\">");
		sb.append("<kpzdbs>" + kpzdbs + "</kpzdbs>");
		sb.append("<fplxdm>" + fplxdm + "</fplxdm>");
		sb.append("<zflx>1</zflx>");
		sb.append("<fpdm>" + fpdm + "</fpdm>");
		sb.append("<fphm>" + fphm + "</fphm>");
		sb.append("<hjje>" + hjje + "</hjje>");
		sb.append("<zfr>" + zfr + "</zfr>");
		sb.append("</body></business>");
		// String base64Str = "";
		// base64Str = ApacheBase64Util.Base64Encode(sb.toString());
		// return new Result("成功", Result.RESULT_SUCCESS, base64Str);
		return new Result("成功", Result.RESULT_SUCCESS, sb.toString());
	}

	/**
	 * 修改发票状态
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("/checkCancel")
	@ResponseBody
	public Result checkCancle(@RequestBody Map<String, Object> param) {
		Map<String, Object> map = XmlResult.check(param);
		String returncode = MapUtil.getString(map, "returncode");
		String returnmsg = MapUtil.getString(map, "returnmsg");
		String fphm = MapUtil.getString(map, "fphm");
		String fpdm = MapUtil.getString(map, "fpdm");
		if (!returncode.equals("0")) {
			if (returnmsg.equals("")) {
				return new Result("作废操作失败", Result.RESULT_ERROR, "");
			} else {
				return new Result(returnmsg, Result.RESULT_ERROR, "");
			}
		}
		String suffix = suffixCheck();
		Map<String, Object> parm = new HashMap<String, Object>();
		parm.put("fphm", fphm);
		parm.put("fpdm", fpdm);
		parm.put("suffix", suffix);
		invoicemapper.updateStatus(parm);
		return new Result("作废操作成功", Result.RESULT_SUCCESS, "");
	}

	/**
	 * 打印
	 * 
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/print")
	@ResponseBody
	public Result print(@RequestParam Map<String, Object> param) {
		// '<?xml version="1.0" encoding="gbk"?><business
		// id="20004"comment="发票打印"><body
		// yylxdm="1"><kpzdbs>51010221</kpzdbs><fplxdm>007</fplxdm><fpdm>1110000789</fpdm>'<fphm>85131117</fphm><dylx>0</dylx><dyfs>0</dyfs></body></business>'
		Map<String, Object> result = (Map<String, Object>) ContextHolderUtils.getSession()
				.getAttribute(LoginController.LOGIN_USER);
		Map<String, Object> dept = (Map<String, Object>) result.get("dept");
		String yylxdm = "1";
		String kpzdbs = MapUtil.getString(dept, "KPZDBS");
		String fplxdm = MapUtil.getString(param, "fplxdm");
		String fpdm = MapUtil.getString(param, "fpdm");
		String fphm = MapUtil.getString(param, "fphm");
		if (fpdm.equals("") || fphm.equals("")) {
			return new Result("失败", Result.RESULT_ERROR, null);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>");
		sb.append("<business id=\"20004\" comment=\"发票打印\">");
		sb.append("<body yylxdm=\"" + yylxdm + "\">");
		sb.append("<kpzdbs>" + kpzdbs + "</kpzdbs>");
		sb.append("<fplxdm>" + fplxdm + "</fplxdm>");
		sb.append("<fpdm>" + fpdm + "</fpdm>");
		sb.append("<fphm>" + fphm + "</fphm>");
		sb.append("<dylx>0</dylx>");
		sb.append("<dyfs>0</dyfs>");
		sb.append("</body></business>");
		// String base64Str = "";
		// try {
		// base64Str = ApacheBase64Util.Base64Encode(new
		// String(sb.toString().getBytes("utf-8"), "gbk"));
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		// return new Result("成功", Result.RESULT_SUCCESS, base64Str);
		return new Result("成功", Result.RESULT_SUCCESS, sb.toString());
	}

	/**
	 * 检测是否打印成功
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("/checkPrint")
	@ResponseBody
	public Result checkPrint(@RequestBody Map<String, Object> param) {
		Map<String, Object> map = XmlResult.check(param);
		String returncode = MapUtil.getString(map, "returncode");
		String returnmsg = MapUtil.getString(map, "returnmsg");
		if (!returncode.equals("0")) {
			if (returnmsg.equals("")) {
				return new Result("打印操作失败", Result.RESULT_ERROR, "");
			} else {
				return new Result(returnmsg, Result.RESULT_ERROR, "");
			}
		}
		return new Result("打印操作成功", Result.RESULT_SUCCESS, "");
	}

	/**
	 * 打印设置
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("/printSet")
	@ResponseBody
	public Result printSet(@RequestParam Map<String, Object> param) {
		String yylxdm = "1";
		String top = MapUtil.getString(param, "top");
		String left = MapUtil.getString(param, "left");
		String fplxdm = MapUtil.getString(param, "fplxdm");
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>");
		sb.append("<business id=\"20003\" comment=\"页边距设置\">");
		sb.append("<body yylxdm=\"" + yylxdm + "\">");
		sb.append("<fplxdm>" + fplxdm + "</fplxdm>");
		sb.append("<top>" + top + "</top>");
		sb.append("<left>" + left + "</left>");
		sb.append("</body></business>");
		// String base64Str = "";
		// try {
		// base64Str = ApacheBase64Util.Base64Encode(new
		// String(sb.toString().getBytes("utf-8"), "gbk"));
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		// return new Result("成功", Result.RESULT_SUCCESS, base64Str);
		return new Result("成功", Result.RESULT_SUCCESS, sb.toString());
	}

	@RequestMapping("/printSetCheck")
	@ResponseBody
	public Result printSetCheck(@RequestBody Map<String, Object> param) {
		Map<String, Object> map = XmlResult.check(param);
		String returncode = MapUtil.getString(map, "returncode");
		String returnmsg = MapUtil.getString(map, "returnmsg");
		if (!returncode.equals("0")) {
			if (returnmsg.equals("")) {
				return new Result("打印页边设置失败", Result.RESULT_ERROR, "");
			} else {
				return new Result(returnmsg, Result.RESULT_ERROR, "");
			}
		}
		return new Result("打印页边设置成功", Result.RESULT_SUCCESS, "");
	}

	@SuppressWarnings("unchecked")
	private String suffixCheck() {
		Map<String, Object> loginMap = (Map<String, Object>) ContextHolderUtils.getSession()
				.getAttribute(LoginController.LOGIN_USER);
		Map<String, Object> dept = (Map<String, Object>) loginMap.get("dept");
		String pidcode = MapUtil.getString(dept, "CODE");
		if (pidcode.length() >= 4) {
			pidcode = pidcode.substring(0, 4);
		}
		if (pidcode.equals("")) {
			throw new BizException("");
		}
		String suffix = "";
		if (pidcode.equals("6103")) {
			// 宝鸡
			suffix = "_BJ";
		} else if (pidcode.equals("6104")) {
			// 咸阳
			suffix = "_XY";
		} else if (pidcode.equals("6106")) {
			// 延安
			suffix = "_YA";
		} else if (pidcode.equals("6107")) {
			// 汉中
			suffix = "_HZ";
		} else if (pidcode.equals("6124")) {
			// 安康
			suffix = "_AK";
		} else if (pidcode.equals("6125")) {
			// 商洛
			suffix = "_SL";
		}
		return suffix;
	}
}
