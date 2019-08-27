package org.corps.bi.notifyer.mail;

import java.util.List;
import java.util.Properties;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


public class MailSenderProxy {
	
	private String senderEmailAddr;

	private JavaMailSender javaMailSender;

	public MailSenderProxy() {
		super();
		this.senderEmailAddr=NotifyerMailConfig.USERNAME;
		this.javaMailSender = this.instanceMailSender();
	}
	
	private JavaMailSenderImpl instanceMailSender(){
		JavaMailSenderImpl javaMailSender=new JavaMailSenderImpl();    
    	String stmpServer=NotifyerMailConfig.HOST;
    	javaMailSender.setHost(stmpServer);
    	javaMailSender.setUsername(NotifyerMailConfig.USERNAME);
    	javaMailSender.setPassword(NotifyerMailConfig.PASSWORD);
    	
    	Properties javaMailProperties=new Properties();
    	 // 同时通过验证 
    	javaMailProperties.put("mail.smtp.auth", NotifyerMailConfig.AUTH); 
        // 存储发送邮件服务器的信息 
    	javaMailProperties.put("mail.smtp.host", NotifyerMailConfig.HOST); 
    	javaMailSender.setJavaMailProperties(javaMailProperties);
    	return javaMailSender;
	}
	
	
	 /** 
     * 发送邮件 
     * @param from :寄信人邮件地址 
     * @param to :收信人邮件地址 
     * @param subject :邮件标题 
     * @param concept :邮件内容 
     */ 
    public final void sendMail(String to,String subject,String concept){ 
        SimpleMailMessage msg=new SimpleMailMessage(); 
        msg.setFrom(senderEmailAddr); 
        msg.setTo(to); 
        msg.setSubject(subject); 
        msg.setText(concept); 
         
        javaMailSender.send(msg); 
    }
    
    /** 
     * 发送邮件 
     * @param from :寄信人邮件地址 
     * @param to :收信人邮件地址 
     * @param subject :邮件标题 
     * @param concept :邮件内容 
     */ 
    public final void sendMail(List<String> to,String subject,String concept){ 
       String[] temp=new String[to.size()];
       to.toArray(temp);
       this.sendMail(temp, subject, concept);
    }
    
    /** 
     * 发送邮件 
     * @param from :寄信人邮件地址 
     * @param to :收信人邮件地址 
     * @param subject :邮件标题 
     * @param concept :邮件内容 
     */ 
    public final void sendMail(String[] to,String subject,String concept){ 
        SimpleMailMessage msg=new SimpleMailMessage(); 
        msg.setFrom(senderEmailAddr); 
        msg.setTo(to); 
        msg.setSubject(subject); 
        msg.setText(concept); 
         
        javaMailSender.send(msg); 
    }
    
    public static void main(String[] args){
    	
    	MailSenderProxy mailSenderProxy=new MailSenderProxy();
    	mailSenderProxy.sendMail("guojianjiong@hoolai.com", "bi_test", "bi monitor test....");
    }
	
}
