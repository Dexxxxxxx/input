package com.cdhy.service.invoiceStatistics.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdhy.controller.security.LoginController;
import com.cdhy.dao.invoiceStatistics.IInvoiceInfoMapper;
import com.cdhy.entity.Page;
import com.cdhy.entity.Result;
import com.cdhy.service.invoiceStatistics.IInvoiceInfoService;
import com.cdhy.util.ContextHolderUtils;
import com.cdhy.util.ExportExcelUtil;

@Service
public class InvoiceInfoServiceImpl implements IInvoiceInfoService{
	 @Autowired
	    private IInvoiceInfoMapper mapper;
	
	
	public  Map<String,Object>  list(Map<String, Object> parm) {
		
		Map<String,Object> m=(Map<String,Object>)ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
		Map<String,Object> mm=(Map<String,Object>)m.get("user");
		//管理员查询
		if("admin".equals(mm.get("USERNAME").toString())){
			
		}
		//公司管理员
		else if(mm.get("USERNAME").toString().startsWith("admin")){
			if(parm.get("dpid")==null||"".equals(parm.get("dpid").toString())){
			parm.put("dpid",mm.get("DEPARTID"));
			}
		}
		//员工根据部门和userid查询，加入dpid是为了防止员工手机号存在于两个公司下
		else{
			parm.put("dpid",mm.get("DEPARTID"));
			parm.put("username",mm.get("USERNAME"));
		}

		
		
		Object start = parm.get("start");
		Object limit = parm.get("limit");
		Page page=new Page(start,limit);
		parm.put("start", page.getStart());
		parm.put("end", page.getEnd());
		int size = mapper.listCount(parm);
		page.setRowsCount(size);
		Map<String, Object> result_map=new HashMap<>();
		if(size>0){
			List<Map<String, Object>> list=mapper.list(parm);
			page.setData(list);
			result_map.put("count", mapper.listAllCollect(parm));
			
		}
		result_map.put("page", page);
		return result_map;
	}
public  Map<String,Object>  list2(Map<String, Object> parm) {
		
		Map<String,Object> m=(Map<String,Object>)ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
		Map<String,Object> mm=(Map<String,Object>)m.get("user");
		//管理员查询
		if("admin".equals(mm.get("USERNAME").toString())){
			
		}
		//公司管理员
		else if(mm.get("USERNAME").toString().startsWith("admin")){
			if(parm.get("dpid")==null||"".equals(parm.get("dpid").toString())){
			parm.put("dpid",mm.get("DEPARTID"));
			}
		}
		//员工根据部门和userid查询，加入dpid是为了防止员工手机号存在于两个公司下
		else{
			parm.put("dpid",mm.get("DEPARTID"));
			parm.put("username",mm.get("USERNAME"));
		}

		
		
		Object start = parm.get("start");
		Object limit = parm.get("limit");
		Page page=new Page(start,limit);
		parm.put("start", page.getStart());
		parm.put("end", page.getEnd());
		int size = mapper.listCount2(parm);
		page.setRowsCount(size);
		Map<String, Object> result_map=new HashMap<>();
		if(size>0){
			List<Map<String, Object>> list=mapper.list2(parm);
			page.setData(list);
			result_map.put("count", mapper.listAllCollect(parm));
			
		}
		result_map.put("page", page);
		return result_map;
	}
	
public Map<String,Object> listAll(Map<String, Object> parm) {
		
		Map<String,Object> m=(Map<String,Object>)ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
		Map<String,Object> mm=(Map<String,Object>)m.get("user");
		//管理员查询
		if("admin".equals(mm.get("USERNAME").toString())){
			
		}
		//公司管理员
		else if(mm.get("USERNAME").toString().startsWith("admin")){
			if(parm.get("dpid")==null||"".equals(parm.get("dpid").toString())){
			parm.put("dpid",mm.get("DEPARTID"));
			}
		}
		//员工根据部门和userid查询，加入dpid是为了防止员工手机号存在于两个公司下
		else{
			parm.put("dpid",mm.get("DEPARTID"));
			parm.put("username",mm.get("USERNAME"));
		}

		
		
		Object start = parm.get("start");
		Object limit = parm.get("limit");
		Page page=new Page(start,limit);
		parm.put("start", page.getStart());
		parm.put("end", page.getEnd());
		int size = mapper.listAllCount(parm);
		page.setRowsCount(size);
		Map<String, Object> result_map=new HashMap<>();
		if(size>0){
			List<Map<String, Object>> list=mapper.listAll(parm);
			result_map.put("count", mapper.listAllCollect(parm));
			page.setData(list);
			
		}
		result_map.put("page", page);
		return result_map;
	}

public List<Map<String, Object>> listCollect(Map<String, Object> parm) {
	Map<String,Object> m=(Map<String,Object>)ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
	Map<String,Object> mm=(Map<String,Object>)m.get("user");
	//管理员查询
			if("admin".equals(mm.get("USERNAME").toString())){
				
			}
			//公司管理员
			else if(mm.get("USERNAME").toString().startsWith("admin")){
				if(parm.get("dpid")==null||"".equals(parm.get("dpid").toString())){
				parm.put("dpid",mm.get("DEPARTID"));
				}
			}
			//员工根据部门和userid查询，加入dpid是为了防止员工手机号存在于两个公司下
			else{
				parm.put("dpid",mm.get("DEPARTID"));
				parm.put("username",mm.get("USERNAME"));
			}
	return mapper.listCollect(parm);
}
	
public Page detailList(Map<String, Object> parm) {
		
		Map<String,Object> m=(Map<String,Object>)ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
		Map<String,Object> mm=(Map<String,Object>)m.get("user");
		//管理员查询
				if("admin".equals(mm.get("USERNAME").toString())){
					
				}
				//公司管理员
				else if(mm.get("USERNAME").toString().startsWith("admin")){
					if(parm.get("dpid")==null||"".equals(parm.get("dpid").toString())){
					parm.put("dpid",mm.get("DEPARTID"));
					}
				}
				//员工根据部门和userid查询，加入dpid是为了防止员工手机号存在于两个公司下
				else{
					parm.put("dpid",mm.get("DEPARTID"));
					parm.put("username",mm.get("USERNAME"));
				}
		Object start = parm.get("start");
		Object limit = parm.get("limit");
		Page page=new Page(start,limit);
		parm.put("start", page.getStart());
		parm.put("end", page.getEnd());
		int size = mapper.detailListCount(parm);
		page.setRowsCount(size);
		if(size>0){
			List<Map<String, Object>> list=mapper.detailList(parm);
			page.setData(list);
		}
		
		return page;
	}

@Override
public Result exportList(Map<String,Object> parm,HttpServletRequest request, HttpServletResponse response){
	Result result = new Result("导出成功", Result.RESULT_SUCCESS,null);
	try{
	Map<String,Object> m=(Map<String,Object>)ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
	Map<String,Object> mm=(Map<String,Object>)m.get("user");
	//管理员查询
			if("admin".equals(mm.get("USERNAME").toString())){
				
			}
			//公司管理员
			else if(mm.get("USERNAME").toString().startsWith("admin")){
				if(parm.get("dpid")==null||"".equals(parm.get("dpid").toString())){
				parm.put("dpid",mm.get("DEPARTID"));
				}
			}
			//员工根据部门和userid查询，加入dpid是为了防止员工手机号存在于两个公司下
			else{
				parm.put("dpid",mm.get("DEPARTID"));
				parm.put("username",mm.get("USERNAME"));
			}
	
//	int size = mapper.listCount(parm);
	List<Map<String, Object>> list=mapper.exportList(parm);
//			if (size > 0) {
//				list = mapper.detailList(parm);
//			}
		
		ExportInvoiceInfo ex = new ExportInvoiceInfo();
		
//		String excelName = "发票信息";//导出文件名
		String path = ex.exportExcel("发票商品信息", "发票商品信息查询",list);
		ExportExcelUtil.downloadFile(response, "发票商品信息查询", path);
		result.setRows(path);
		
	} catch (Exception e) {
		 result = new Result("导出失败", Result.RESULT_ERROR,null);
		e.printStackTrace();
	}
	return result;
}
@Override
public Result exportList2(Map<String,Object> parm,HttpServletRequest request, HttpServletResponse response){
	Result result = new Result("导出成功", Result.RESULT_SUCCESS,null);
	try{
	Map<String,Object> m=(Map<String,Object>)ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
	Map<String,Object> mm=(Map<String,Object>)m.get("user");
	//管理员查询
			if("admin".equals(mm.get("USERNAME").toString())){
				
			}
			//公司管理员
			else if(mm.get("USERNAME").toString().startsWith("admin")){
				if(parm.get("dpid")==null||"".equals(parm.get("dpid").toString())){
				parm.put("dpid",mm.get("DEPARTID"));
				}
			}
			//员工根据部门和userid查询，加入dpid是为了防止员工手机号存在于两个公司下
			else{
				parm.put("dpid",mm.get("DEPARTID"));
				parm.put("username",mm.get("USERNAME"));
			}
	
//	int size = mapper.listCount(parm);
	List<Map<String, Object>> list=mapper.exportList2(parm);
//			if (size > 0) {
//				list = mapper.detailList(parm);
//			}
		
		ExportInvoiceInfo2 ex = new ExportInvoiceInfo2();
		
//		String excelName = "发票信息";//导出文件名
		String path = ex.exportExcel("发票信息", "发票信息查询",list);
		ExportExcelUtil.downloadFile(response, "发票信息查询", path);
		result.setRows(path);
		
	} catch (Exception e) {
		 result = new Result("导出失败", Result.RESULT_ERROR,null);
		e.printStackTrace();
	}
	return result;
}

@Override
public Result exportDetailList(Map<String,Object> parm,HttpServletRequest request, HttpServletResponse response){
	Result result = new Result("导出成功", Result.RESULT_SUCCESS,null);
	try{
	Map<String,Object> m=(Map<String,Object>)ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
	Map<String,Object> mm=(Map<String,Object>)m.get("user");
	//管理员查询
			if("admin".equals(mm.get("USERNAME").toString())){
				
			}
			//公司管理员
			else if(mm.get("USERNAME").toString().startsWith("admin")){
				if(parm.get("dpid")==null||"".equals(parm.get("dpid").toString())){
				parm.put("dpid",mm.get("DEPARTID"));
				}
			}
			//员工根据部门和userid查询，加入dpid是为了防止员工手机号存在于两个公司下
			else{
				parm.put("dpid",mm.get("DEPARTID"));
				parm.put("username",mm.get("USERNAME"));
			}
	
	List<Map<String, Object>> list=mapper.exportDetailList(parm);

		
		ExportInvoiceDetailInfo ex = new ExportInvoiceDetailInfo();
		
		String path = ex.exportExcel("商品明细查询", "商品明细查询",list);
		ExportExcelUtil.downloadFile(response, "商品明细查询", path);
		result.setRows(path);
		
	} catch (Exception e) {
		 result = new Result("导出失败", Result.RESULT_ERROR,null);
		e.printStackTrace();
	}
	return result;
}
public Page listDep(Map<String, Object> parm) {
	
	Map<String,Object> m=(Map<String,Object>)ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
	Map<String,Object> mm=(Map<String,Object>)m.get("user");
	//管理员查询
			if("admin".equals(mm.get("USERNAME").toString())){
				
			}
			//公司管理员
			else if(mm.get("USERNAME").toString().startsWith("admin")){
				if(parm.get("dpid")==null||"".equals(parm.get("dpid").toString())){
				parm.put("dpid",mm.get("DEPARTID"));
				}
			}
			//员工根据部门和userid查询，加入dpid是为了防止员工手机号存在于两个公司下
			else{
				parm.put("dpid",mm.get("DEPARTID"));
				parm.put("username",mm.get("USERNAME"));
			}
	Object start = parm.get("start");
	Object limit = parm.get("limit");
	Page page=new Page(start,limit);
	parm.put("start", page.getStart());
	parm.put("end", page.getEnd());
	int size = mapper.listDepCount(parm);
	page.setRowsCount(size);
	if(size>0){
		List<Map<String, Object>> list=mapper.listDep(parm);
		page.setData(list);
	}
	
	return page;
}

public Page listSupplier(Map<String, Object> parm) {
	
	Map<String,Object> m=(Map<String,Object>)ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
	Map<String,Object> mm=(Map<String,Object>)m.get("user");
	//管理员查询
			if("admin".equals(mm.get("USERNAME").toString())){
				
			}
			//公司管理员
			else if(mm.get("USERNAME").toString().startsWith("admin")){
				if(parm.get("dpid")==null||"".equals(parm.get("dpid").toString())){
				parm.put("dpid",mm.get("DEPARTID"));
				}
			}
			//员工根据部门和userid查询，加入dpid是为了防止员工手机号存在于两个公司下
			else{
				parm.put("dpid",mm.get("DEPARTID"));
				parm.put("username",mm.get("USERNAME"));
			}
	Object start = parm.get("start");
	Object limit = parm.get("limit");
	Page page=new Page(start,limit);
	parm.put("start", page.getStart());
	parm.put("end", page.getEnd());
	int size = mapper.listSupplierCount(parm);
	page.setRowsCount(size);
	if(size>0){
		List<Map<String, Object>> list=mapper.listSupplier(parm);
		page.setData(list);
	}
	
	return page;
}

}
