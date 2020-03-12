package org.corps.bi.notifyer.mail;

import org.corps.bi.notifyer.mail.MailSenderProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncSendMailThread  implements Runnable{
	
	private static final Logger LOGGER=LoggerFactory.getLogger(AsyncMailSender.class);
	
	private final MailSenderProxy mailSenderProxy;
	
	private String[] toList;
	
	private String subject;
	
	private String content;

	public AsyncSendMailThread(MailSenderProxy mailSenderProxy,String[] toList, String subject,
			String content) {
		super();
		this.mailSenderProxy = mailSenderProxy;
		this.toList = toList;
		this.subject = subject;
		this.content = content;
	}

	@Override
	public void run() {
		try {
			mailSenderProxy.sendMail(toList, subject, content);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
	}
	
}