package com.cdhy.service.cigarette;

import java.util.Map;

import com.cdhy.entity.Page;
import com.cdhy.entity.Result;

public interface ICigarCustomerService {
    Page list(Map<String, Object> parm);
    void update(Map<String, Object> parm);
    Map<String, Object> getById(Map<String, Object> parm);
    void add(Map<String, Object> parm);
    int delete(Map<String, Object> parm);
    String saveBillsToDB(String filePath);
}
