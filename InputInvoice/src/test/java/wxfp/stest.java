package wxfp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cdhy.service.invoiceStatistics.IInvoiceInfoService;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"classpath:conf/spring.xml","classpath:conf/spring.xml","classpath:conf/spring-mybatis.xml"}) 
//@Transactional 
public class stest extends AbstractTransactionalJUnit4SpringContextTests{ 
    @Autowired 
    private IInvoiceInfoService service; 
   
    @Test 
    public void testGetAcccountById() { 
    	List<Map<String, Object>> l=service.listCollect(new HashMap<String, Object>());
    	String jsonString = JSON.toJSONString(l);
    	System.out.println(jsonString);
    	
    	 Date date=new Date();  
    	  //输出毫秒值
    	  System.out.println(JSON.toJSONString(date));
    	  //默认格式为yyyy-MM-dd HH:mm:ss  
    	  System.out.println(JSON.toJSONString(date, SerializerFeature.WriteDateUseDateFormat));
    	  //根据自定义格式输出日期 
    	  System.out.println(JSON.toJSONStringWithDateFormat(date, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat));

    	
    } 
}
