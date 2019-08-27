package org.corps.bi.tools.util;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class ThreadSleepUtils {
	
	private static final ThreadSleep DEFAULT=new ThreadSleep(5000);
	
	public static ThreadSleep defaultInstance(){
		return DEFAULT;
	}
	
	public static  ThreadSleep create(int maxSleepTime){
		return new ThreadSleep(maxSleepTime);
	}

	public static class ThreadSleep{
		
		private static Logger logger=Logger.getLogger(ThreadSleep.class.getSimpleName());
		
		private final int maxSleepTime;
		
		private final Random random;
		
		private ThreadSleep(int maxSleepTime) {
			super();
			this.maxSleepTime = maxSleepTime;
			this.random = new Random();
		}

		public int getMaxSleepTime() {
			return maxSleepTime;
		}

		public void sleep(){
			try {
				int randomSleepTime=this.random.nextInt(this.maxSleepTime);
				logger.info("currentThread:"+Thread.currentThread().getName()+" will sleep "+randomSleepTime+" mills");
				TimeUnit.MILLISECONDS.sleep(randomSleepTime);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(),e);
			}
		}
		
		
	}
}
