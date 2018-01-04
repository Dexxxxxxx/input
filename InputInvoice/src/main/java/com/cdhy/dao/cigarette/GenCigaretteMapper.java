package com.cdhy.dao.cigarette;

import java.util.List;
import java.util.Map;

public interface GenCigaretteMapper {
    List<Map<String, Object>> getSpInfo(Map<String, Object> param);
    
    int deleteByIds(Map<String, Object> param);
    
}
