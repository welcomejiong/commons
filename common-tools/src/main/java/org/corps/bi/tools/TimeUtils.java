package org.corps.bi.tools;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;

public class TimeUtils {
	
	public static final int ONE_MINUTES_MILLS=60*1000;
	
	public static final int ONE_HOUR_MILLS=60*ONE_MINUTES_MILLS;
	
	public static final int ONE_DAY_MILLS=24*ONE_HOUR_MILLS;
	
	public static long currentTimeMillis(){
		return System.currentTimeMillis();
	}
	
	public static long currentSeconds(){
		return TimeUnit.MILLISECONDS.toSeconds(TimeUtils.currentTimeMillis());
	}
	
	public static long currentMinutes(){
		return TimeUnit.MILLISECONDS.toMinutes(TimeUtils.currentTimeMillis());
	}
	
	public static long currentHours(){
		return TimeUnit.MILLISECONDS.toHours(TimeUtils.currentTimeMillis());
	}
	
	public static long currentDays(){
		return TimeUnit.MILLISECONDS.toDays(TimeUtils.currentTimeMillis());
	}
	
	public static long convertSeconds(long duration){
		return TimeUnit.MILLISECONDS.toSeconds(duration);
	}
	
	public static long convertMinutes(long duration){
		return TimeUnit.MILLISECONDS.toMinutes(duration);
	}
	
	public static long convertHours(long duration){
		return TimeUnit.MILLISECONDS.toHours(duration);
	}
	
	public static long convertDays(long duration){
		return TimeUnit.MILLISECONDS.toDays(duration);
	}
	
	/**
	 * 传入时间与当前时间相差多少秒
	 * @param duration
	 * @return
	 */
	public static long diffCurrentSeconds(long duration){
		return TimeUtils.currentSeconds()-TimeUtils.convertSeconds(duration);
	}
	
	/**
	 * 传入时间与当前时间相差多少分
	 * @param duration
	 * @return
	 */
	public static long diffCurrentMinutes(long duration){
		return TimeUtils.currentMinutes()-TimeUtils.convertMinutes(duration);
	}
	
	/**
	 * 传入时间与当前时间相差多少小时
	 * @param duration
	 * @return
	 */
	public static long diffCurrentHours(long duration){
		return TimeUtils.currentHours()-TimeUtils.convertHours(duration);
	}
	
	/**
	 * 传入时间与当前时间相差多少天
	 * @param duration
	 * @return
	 */
	public static long diffCurrentDays(long duration){
		return TimeUtils.currentDays()-TimeUtils.convertDays(duration);
	}
	
	/**
	 * 当前天0:0:0:0
	 * @return
	 */
	public static long currDayBeginMills(){
		long now=TimeUtils.currentTimeMillis();
    	Calendar calEnd = Calendar.getInstance();
		calEnd.setTimeInMillis(now); 
		calEnd.set(Calendar.HOUR_OF_DAY, 0) ; 
		calEnd.set(Calendar.MINUTE, 0); 
		calEnd.set(Calendar.SECOND, 0);
		calEnd.set(Calendar.MILLISECOND, 0); 
		return calEnd.getTimeInMillis();
	}
	/**
	 * 当前天23:59:59:00
	 * @return
	 */
	public static long currDayEndMills(){
		long now=TimeUtils.currentTimeMillis();
    	Calendar calEnd = Calendar.getInstance();
		calEnd.setTimeInMillis(now); 
		calEnd.set(Calendar.HOUR_OF_DAY, 23) ; 
		calEnd.set(Calendar.MINUTE, 59); 
		calEnd.set(Calendar.SECOND, 59);
		calEnd.set(Calendar.MILLISECOND, 999); 
		return calEnd.getTimeInMillis();
	}
	/**
	 * 当前天0:0:0:0 加的天簌
	 * @return
	 */
	public static long addDaysToCurrDayBeginMills(int addDays){
		long now=TimeUtils.currentTimeMillis();
    	Calendar calEnd = Calendar.getInstance();
		calEnd.setTimeInMillis(now); 
		calEnd.set(Calendar.HOUR_OF_DAY, 0) ; 
		calEnd.set(Calendar.MINUTE, 0); 
		calEnd.set(Calendar.SECOND, 0);
		calEnd.set(Calendar.MILLISECOND, 0); 
		return DateUtils.addMilliseconds(calEnd.getTime(), addDays*ONE_DAY_MILLS).getTime();
	}
	
	public static void main(String args[]) throws Exception{
		long now=TimeUtils.currentTimeMillis();
    	Calendar calEnd = Calendar.getInstance();
		calEnd.setTimeInMillis(now); 
		calEnd.set(Calendar.HOUR_OF_DAY, 0) ; 
		calEnd.set(Calendar.MINUTE, 0); 
		calEnd.set(Calendar.SECOND, 0);
		calEnd.set(Calendar.MILLISECOND, 0); 
		
		System.out.println(calEnd.getTimeInMillis());
		System.out.println(currDayBeginMills());
		
		System.out.println(org.corps.bi.tools.util.DateUtils.format(calEnd.getTimeInMillis(), org.corps.bi.tools.util.DateUtils.DEFAULT_DATE_FORMAT));
		System.out.println(org.corps.bi.tools.util.DateUtils.format(currDayBeginMills(), org.corps.bi.tools.util.DateUtils.DEFAULT_DATE_FORMAT));
		System.out.println(org.corps.bi.tools.util.DateUtils.format(currDayEndMills(), org.corps.bi.tools.util.DateUtils.DEFAULT_DATE_FORMAT));
		System.out.println(org.corps.bi.tools.util.DateUtils.format(addDaysToCurrDayBeginMills(1), org.corps.bi.tools.util.DateUtils.DEFAULT_DATE_FORMAT));
		System.out.println(org.corps.bi.tools.util.DateUtils.format(addDaysToCurrDayBeginMills(-1), org.corps.bi.tools.util.DateUtils.DEFAULT_DATE_FORMAT));
	}
	

}
