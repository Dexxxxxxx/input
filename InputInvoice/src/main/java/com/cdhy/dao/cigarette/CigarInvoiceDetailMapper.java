package com.cdhy.dao.cigarette;

import java.util.List;
import java.util.Map;

public interface CigarInvoiceDetailMapper {
    
    List<Map<String, Object>> list(Map<String, Object> parm);
    
    void insert(Map<String, Object> parm);
    
    void delete(Map<String, Object> parm);
}
