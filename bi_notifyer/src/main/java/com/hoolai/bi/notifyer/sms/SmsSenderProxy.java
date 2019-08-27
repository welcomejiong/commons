package com.hoolai.bi.notifyer.sms;

import java.net.URLEncoder;
import java.util.List;

import com.jian.tools.util.HttpClientUtils;



public class SmsSenderProxy {
	
	private String smsHost="http://si.800617.com:4400/SendLenSms.aspx?un=bjhlyx-20&pwd=28f68d&mobile=#phone#&msg=#msg#";
	
	public boolean send(String phone,String msg){
		try {
			String msgHost=smsHost.replace("#phone#", phone);
			msgHost=msgHost.replace("#msg#", URLEncoder.encode(msg, "GB2312"));
			String res=HttpClientUtils.executeGetRequest(msgHost, "GB2312");
			System.out.println("sms:"+res);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean send(String[] phones,String msg){
		
		for (String phone : phones) {
			this.send(phone, msg);
		}
		
		return true;
	}
	
	public boolean send(List<String> phones,String msg){
		
		for (String phone : phones) {
			this.send(phone, msg);
		}
		
		return true;
	}
	
	public static void main(String args[]){
		SmsSenderProxy smsSenderProxy=new SmsSenderProxy();
		smsSenderProxy.send("13718241912", "胡莱数据报告【互爱BI】");
	}
	
}
