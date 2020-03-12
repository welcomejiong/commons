package org.corps.bi.notifyer.mail;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  异步发送邮件 
 */
public class AsyncMailSender {
	
	private static final Logger LOGGER=LoggerFactory.getLogger(AsyncMailSender.class);
	
	private static AsyncMailSender INSTANCE;
	
	private final int corePoolSize;
	
	private final ThreadPoolExecutor systemThreadPoolExecutor;
	
	private final MailSenderProxy mailSenderProxy;
	
	private AsyncMailSender() {
		super();
		this.corePoolSize=1;
		this.systemThreadPoolExecutor = new ThreadPoolExecutor(
				this.corePoolSize,		//指的是保留的线程池大小
				this.corePoolSize*2, 	//最大线程池， 指的是线程池的最大大小
				100, 	//指的是空闲线程结束的超时时间
				TimeUnit.SECONDS, 	//表示 keepAliveTime 的单位
				new LinkedBlockingQueue<Runnable>(100),
				new MailThreadFactory("sendmail-"+this.getClass().getSimpleName()),
				new ThreadPoolExecutor.CallerRunsPolicy() //直接放弃当前任务
		);
		this.mailSenderProxy=new MailSenderProxy();
	}
	
	public static synchronized AsyncMailSender getInstance() {
		if(INSTANCE==null) {
			INSTANCE=new AsyncMailSender();
		}
		return INSTANCE;
	}

	public final void sendMail(String to,String subject,String content){
		String[] tmp=new String[] {to};
		this.sendMail(tmp, subject, content);
		
	}
	
	public final void sendMail(String[] to,String subject,String content){
		AsyncSendMailThread asyncSendMailThread=new AsyncSendMailThread(this.mailSenderProxy,to, subject, content);
		this.systemThreadPoolExecutor.submit(asyncSendMailThread);
	}
	
	public static class MailThreadFactory implements ThreadFactory {

		private static final AtomicInteger poolNumber = new AtomicInteger(1);
		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;

		public  MailThreadFactory(String name) {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			namePrefix = "RealTimeStreaming "+ name+"-" + poolNumber.getAndIncrement() + "-thread-";
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			if (t.isDaemon())
				t.setDaemon(false);
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}

	}
	
	
}
