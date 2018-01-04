package com.cdhy.dao.security;

import java.util.List;
import java.util.Map;

public interface LoginMapper {
    
    public Map<String, Object> login(Map<String, Object> map);

    public List<Map<String, Object>> queryRolePrivilege(Map<String, Object> user);

    public void updatePassword(Map<String, Object> parm);

    public String getOldPassword(Map<String, Object> parm);
	
    public Map<String, Object> getUser(Map<String, Object> parm);

    public List<Map<String, Object>> queryAllRolePrivilege();

    public int queryDefaultPasswordFlag(Map<String, Object> map);
    
    public Map<String, Object> getDeptInfo(Map<String, Object> map);

}