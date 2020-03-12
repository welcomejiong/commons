package org.corps.bi.tools.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
	
	public static  String getErrorStrackTrace(Exception e) {
		try {
			StringWriter sw=new StringWriter();
			PrintWriter pw=new PrintWriter(sw, true);
			e.printStackTrace(pw);
			String msg=sw.getBuffer().toString();
			return msg;
		} catch (Exception e1) {
			return e1.getMessage();
		}
	}

}
