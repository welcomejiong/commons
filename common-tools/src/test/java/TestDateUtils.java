import org.corps.bi.tools.util.DateUtils;
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
	
}
