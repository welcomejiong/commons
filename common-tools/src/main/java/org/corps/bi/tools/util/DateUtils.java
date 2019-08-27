package org.corps.bi.tools.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.corps.bi.tools.TimeUtils;
public class DateUtils {
	
	public static final String DEFAULT_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
	
	public static final String DEFAULT_DAY_FORMAT="yyyy-MM-dd";
	
	public static final  String DEFAULT_HOUR_FORMAT="HH:mm:ss";
	
	public static final String HOUR_NO_SECOND_FORMAT="HH:mm";
	
	public static final String formatToDay(Date date){
		return format(date, DEFAULT_DAY_FORMAT);
	}
	
	public static String formatToDay(long millis){
		return format(millis, DEFAULT_DAY_FORMAT);
	}
	
	public static String formatToHour(Date date){
		return format(date, DEFAULT_HOUR_FORMAT);
	}
	
	public static String formatToHour(long millis){
		return format(millis, DEFAULT_HOUR_FORMAT);
	}
	
	public static String format(Date date){
		return format(date, DEFAULT_DATE_FORMAT);
	}
	
	public static String format(long millis){
		return format(millis, DEFAULT_DATE_FORMAT);
	}

	public static String format(Date date,String pattern){
		return DateFormatUtils.format(date, pattern);
	}
	
	public static String format(long millis,String pattern){
		return DateFormatUtils.format(millis, pattern);
	}
	
	public static long getDiffDayThanToday(long millis){
		return TimeUtils.currentDays()-TimeUtils.convertDays(millis);
	}
	
	public static long getDateMillis(Date date){
		return date.getTime();
	}
	
	public static Date parseDateFromMillis(long millis){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		return cal.getTime();
	}
	
	public static String millisToTime(long time) {  
        String timeStr = null;  
        long hour = 0l;  
        long minute = 0l;  
        long second = 0l;  
        if (time <= 0l){
        	return "00:00";
        }else {  
            minute = time / 60000l;  
            if (minute < 60l) {  
                second = (time % 60000l)/1000;  
                timeStr = unitFormat(minute) + ":" + unitFormat(second);  
            } else {  
                hour = minute / 60l;  
                if (hour > 99l)  
                    return "99:59:59";  
                minute = minute % 60l;  
                second = time - hour * 3600000l - minute * 60000l;  
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);  
            }  
        }  
        return timeStr;  
    }  
	
	public static String secondToTime(long time) {  
		 return millisToTime(time*1000);
//        String timeStr = null;  
//        long hour = 0l;  
//        long minute = 0l;  
//        long second = 0l;  
//        if (time <= 0l){
//        	return "00:00";
//        }else {  
//            minute = time / 60l;  
//            if (minute < 60l) {  
//                second = time % 60l;  
//                timeStr = unitFormat(minute) + ":" + unitFormat(second);  
//            } else {  
//                hour = minute / 60l;  
//                if (hour > 99l)  
//                    return "99:59:59";  
//                minute = minute % 60l;  
//                second = time - hour * 3600l - minute * 60l;  
//                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);  
//            }  
//        }  
//        return timeStr;  
    }  
  
    public static String unitFormat(long i) {  
        String retStr = null;  
        if (i >= 0l && i < 10l)  
            retStr = "0" + Long.toString(i);  
        else  
            retStr = "" + i;  
        return retStr;  
    }  
	
}
