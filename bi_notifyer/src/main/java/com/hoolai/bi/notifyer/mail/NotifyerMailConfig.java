package com.hoolai.bi.notifyer.mail;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.util.StringUtils;

public class NotifyerMailConfig {
	
	public static boolean AUTH;
	
	public static String HOST;
	
	public static Integer PORT;
	
	public static String USERNAME;
	
	public static String PASSWORD;
	
	static{
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void init() throws Exception{
		InputStream globleIn = NotifyerMailConfig.class.getClassLoader().getResourceAsStream("default_notifyer_mail.properties");
		Properties globleProperties = new Properties();
		if(globleIn!=null){
			globleProperties.load(globleIn);
			parse(globleProperties);
		}
		
		InputStream mailIn = NotifyerMailConfig.class.getClassLoader().getResourceAsStream("notifyer_mail.properties");
		Properties mailProperties = new Properties();
		if(mailIn!=null){
			mailProperties.load(mailIn);
			parse(mailProperties);
		}
	}
	
	private static void parse(Properties properties){
		String authStr=properties.getProperty("mail.smtp.auth");
		if(!StringUtils.isEmpty(authStr)){
			AUTH=Boolean.parseBoolean(authStr);
		}
		HOST=properties.getProperty("mail.smtp.host");
		String portStr=properties.getProperty("mail.smtp.port");
		if(!StringUtils.isEmpty(portStr)){
			PORT=Integer.parseInt(portStr);
		}
		USERNAME=properties.getProperty("mail.smtp.username");
		PASSWORD=properties.getProperty("mail.smtp.password");
		
	}
	
}
