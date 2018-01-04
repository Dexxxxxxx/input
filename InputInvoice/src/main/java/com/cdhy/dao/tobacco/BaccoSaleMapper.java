package com.cdhy.dao.tobacco;

import java.util.Map;

public interface BaccoSaleMapper {
    
    void save(Map<String, Object> param);
    
    void saveDetail(Map<String, Object> param);
    
    void update(Map<String, Object> param);

}
