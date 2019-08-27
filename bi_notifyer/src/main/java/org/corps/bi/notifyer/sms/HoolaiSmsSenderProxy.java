package org.corps.bi.notifyer.sms;

import java.util.HashMap;
import java.util.Map;

import org.corps.bi.tools.util.HttpClientUtils;
import org.springframework.util.DigestUtils;



public class HoolaiSmsSenderProxy {
	
	private final String smsHost="http://access.hoolai.com/message_service/messageService/sendSms.hl";
	
	private Integer productId;
	
	private String productKey;
	
	public HoolaiSmsSenderProxy(Integer productId, String productKey) {
		super();
		this.productId = productId;
		this.productKey = productKey;
	}

	public boolean send(SmsMess smsMess){
		if(smsMess==null||smsMess.getPhones()==null||smsMess.getParams()==null){
			return false;
		}
		try {
			String sendParams="";
			for (int i = 0; i < smsMess.getParams().length; i++) {
				sendParams+=smsMess.getParams()[i];
				if(i!=smsMess.getParams().length-1){
					sendParams+=",";
				}
			}
			
			for (int i = 0; i < smsMess.getPhones().length; i++) {
				String sendPhones=smsMess.getPhones()[i];
				long requestTime=System.currentTimeMillis();
				String srcMd5=this.productId+"."+requestTime+"."+this.productKey;
				String accessToken=DigestUtils.md5DigestAsHex(srcMd5.getBytes("UTF-8"));
				
				Map<String,String> params=new HashMap<String, String>();
				params.put("mobile", sendPhones);
				params.put("params", sendParams);
				params.put("productId", this.productId+"");
				params.put("templateId", smsMess.getTemplateId()+"");
				params.put("accessToken", accessToken);
				params.put("requestTime", requestTime+"");
				String response=this.request(smsHost, params);
				System.out.println("smsRes:"+response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private String request(String baseUrl,Map<String,String> params){
		try {
			String ret=HttpClientUtils.executePostRequest(baseUrl, params, "UTF-8");
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static class SmsMess{
		
		private Integer templateId;
		
		private String[] phones;
		
		private String[] params;
		
		public SmsMess(Integer templateId, String[] phones, String[] params) {
			super();
			this.templateId = templateId;
			this.phones = phones;
			this.params = params;
		}

		public Integer getTemplateId() {
			return templateId;
		}

		public void setTemplateId(Integer templateId) {
			this.templateId = templateId;
		}

		public String[] getPhones() {
			return phones;
		}

		public void setPhones(String[] phones) {
			this.phones = phones;
		}

		public String[] getParams() {
			return params;
		}

		public void setParams(String[] params) {
			this.params = params;
		}
		
	}
	
	public static void main(String args[]){
		HoolaiSmsSenderProxy smsSenderProxy=new HoolaiSmsSenderProxy(116, "5FsI3S46ePvO79r9");
		String[] phones=new String[]{"13718241912","18613864993","18810609695","15626093569","13810775157","15521000951"};
		String[] params=new String[]{"(测试)每天","成功","1","71","2015-09-08","15"};
		SmsMess smsMess=new SmsMess( 46, phones, params);
		smsSenderProxy.send(smsMess);
	}
	
}
