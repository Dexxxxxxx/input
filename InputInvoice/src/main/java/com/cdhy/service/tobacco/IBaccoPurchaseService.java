package com.cdhy.service.tobacco;

import java.util.List;
import java.util.Map;

import com.cdhy.entity.Page;

public interface IBaccoPurchaseService {
    
    List<Map<String, Object>> doInvoice(Map<String, Object> parm);
    
    List<Map<String, Object>> doInvoice1(Map<String, Object> parm);
    
    Map<String, Object> doInvoiceByOne(Map<String, Object> parm);
    
    Map<String,Object> redoInvoice(Map<String, Object> parm);
    
    void  rollback(Map<String, Object> parm);
    
    Map<String, Object> saveInvoice(Map<String, Object> parm) throws Exception;
    
    List<Map<String, Object>> getTree(Map<String, Object> parm);
}
