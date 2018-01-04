package com.cdhy.service.tobacco;

import java.util.List;
import java.util.Map;

public interface IBaccoSaleService {
    
    List<Map<String, Object>> doInvoice(Map<String, Object> param);
    
    Map<String, Object> saveInvoice(Map<String, Object> param) throws Exception;
}
