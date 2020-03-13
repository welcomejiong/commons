import java.util.HashMap;
import java.util.Map;

import org.corps.bi.tools.util.DateUtils;
import org.corps.bi.tools.util.JSONUtils;
import org.junit.Test;


public class TestDateUtils {

	@Test
	public void testSecondToTime(){
		System.out.println(DateUtils.secondToTime(120)+"-"+DateUtils.millisToTime(120*1000));
		System.out.println(DateUtils.secondToTime(1200)+"-"+DateUtils.millisToTime(1200*1000));
		System.out.println(DateUtils.secondToTime(12000)+"-"+DateUtils.millisToTime(12000*1000));
		System.out.println(DateUtils.secondToTime(120000)+"-"+DateUtils.millisToTime(120000*1000));
		System.out.println(DateUtils.secondToTime(1200000)+"-"+DateUtils.millisToTime(1200000*1000));
		System.out.println(DateUtils.millisToTime(2788810));
	}
	
	@Test
	public void testJsonFormat(){
		Map<String,Object> tmpMap=new HashMap<String, Object>();
		tmpMap.put("name", "jiong");
		tmpMap.put("password", "pass_jiong");
		
		Map<String,Object> address=new HashMap<String, Object>();
		address.put("country", "china");
		address.put("province", "cq");
		
		tmpMap.put("address", address);
		
		System.out.println(JSONUtils.toJSON(tmpMap));
		System.out.println(JSONUtils.toJSONFormat(tmpMap));
		System.out.println(JSONUtils.toJSON(tmpMap));
		System.out.println(JSONUtils.toJSONFormat(tmpMap));
	}
	
}
