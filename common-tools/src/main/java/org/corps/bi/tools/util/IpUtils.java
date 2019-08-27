package org.corps.bi.tools.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;


public class IpUtils {

	public static String getIp(HttpServletRequest request){
		String ip=request.getHeader("X-Real-IP");
		if(StringUtils.isEmpty(ip)){
			ip=request.getHeader("X-Forwarded-For");
		}
		if(StringUtils.isEmpty(ip)){
			ip=request.getRemoteAddr();
		}
		return ip;
	}
	
	public static boolean isValidIp(String ip){
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher matcher = pattern.matcher(ip); //以验证127.400.600.2为例
		return matcher.matches();
	}
}
