package org.corps.bi.tools.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 正则表达式工具类
 * 
 * @author 李志忠
 * 
 */
public class RegexUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(RegexUtils.class);

	/** 正则之间的分隔符 "||" */
	public static final String TAG_REGEX_SPLIT = "||";

	public static final String TESHU_FUHAO = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";;
	
	private static final String URL_REGEX = "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	
	public static String replaceURL(String str, String replace){
		try {
			return RegexUtils.replaceAll(str, URL_REGEX, " ");
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * 清楚特殊符号
	 * 
	 * @param wd
	 * @return
	 */
	public static String replaceTeshufuhao(String wd) {
		try {
			return RegexUtils.replaceAll(wd, TESHU_FUHAO, " ");
		} catch (Exception e) {
			return wd;
		}
	}

	/**
	 * 获取网站域名
	 * 
	 * @param url
	 * @return
	 */
	public static synchronized String getDomain(String url) {
		if (url == null)
			return null;
		String first = "";

		// 协议头
		int pos = url.indexOf("://");
		if (pos >= 0) {
			first = url.substring(0, pos + 3);
			url = url.substring(pos + 3);
		}

		// 第一个路径
		pos = url.indexOf("/");
		if (pos >= 0)
			url = url.substring(0, pos);
		pos = url.indexOf("?");
		if (pos >= 0)
			url = url.substring(0, pos);
		return first + url;
	}

	/**
	 * 获取连接
	 * 
	 * @param url
	 * @param relativeUrl
	 * @return
	 */
	public static synchronized String getPath(String url, String relativeUrl) {
		if (relativeUrl == null)
			return null;
		if (url == null)
			return relativeUrl;
		if (relativeUrl.startsWith("/")) {
			return getDomain(url) + relativeUrl;
		} else if (relativeUrl.toLowerCase().startsWith("http:")
				|| relativeUrl.toLowerCase().startsWith("https:")) {
			return relativeUrl;
		} else if (relativeUrl.startsWith("?")) {
			int pos = url.indexOf("?");
			if (pos >= 0)
				return url.substring(0, pos) + relativeUrl;
			return url + relativeUrl;
		} else {
			// 协议头
			String first = "";
			int pos = url.indexOf("://");
			if (pos >= 0) {
				first = url.substring(0, pos + 3);
				url = url.substring(pos + 3);
			}
			// 取得参数名之前的地址
			int idx = url.indexOf('?');
			if (idx > 0)
				url = url.substring(0, idx);
			// 取路径
			idx = url.lastIndexOf('/');
			if (idx > 0)
				url = url.substring(0, idx);

			int flag = 3;
			// 处理../或./
			while (flag != 0) {
				if (relativeUrl.startsWith("../"))
					flag = 3;
				else if (relativeUrl.startsWith("./"))
					flag = 2;
				else
					flag = 0;
				if (flag == 3) {
					pos = url.lastIndexOf('/');
					if (pos >= 1)
						url = url.substring(0, pos);
				}
				if (flag > 0)
					relativeUrl = relativeUrl.substring(flag);
			}
			// 相对路径
			return first + url + "/" + relativeUrl;
		}

	}

	/**
	 * 正则表达式替换
	 * 
	 * @param source
	 * @param patten
	 * @param replace
	 * @return
	 * @throws Exception
	 */
	public static String replaceAll(String source, String patten, String replace)
			throws Exception {
		if (source == null)
			return null;
		if (patten == null || replace == null)
			return null;
		Perl5Matcher matcher = new Perl5Matcher();
		PatternCompiler compiler = new Perl5Compiler();
		Pattern pattern = compiler.compile(patten,
				Perl5Compiler.CASE_INSENSITIVE_MASK
						| Perl5Compiler.SINGLELINE_MASK);
		StringBuffer buf = new StringBuffer(100);
		while (matcher.contains(source, pattern)) {
			MatchResult result = matcher.getMatch();
			buf.delete(0, buf.length());
			int begin = result.beginOffset(0);
			int end = result.endOffset(0);
			buf.append(source.substring(0, begin));
			buf.append(replace);
			buf.append(source.substring(end));
			source = buf.toString();
		}
		return source;
	}

	/**
	 * 替换匹配到的第一个字符串
	 * 
	 * @param source
	 * @param patten
	 * @param replace
	 * @return
	 * @throws Exception
	 */
	public static String replaceFirst(String source, String patten,
			String replace) throws Exception {
		if (source == null)
			return null;
		if (patten == null || replace == null)
			return null;
		Perl5Matcher matcher = new Perl5Matcher();
		PatternCompiler compiler = new Perl5Compiler();
		Pattern pattern = compiler.compile(patten,
				Perl5Compiler.CASE_INSENSITIVE_MASK
						| Perl5Compiler.SINGLELINE_MASK);
		StringBuffer buf = new StringBuffer(100);
		if (matcher.contains(source, pattern)) {
			MatchResult result = matcher.getMatch();
			buf.delete(0, buf.length());
			int begin = result.beginOffset(0);
			int end = result.endOffset(0);
			buf.append(source.substring(0, begin));
			buf.append(replace);
			buf.append(source.substring(end));
			source = buf.toString();
		}
		return source;
	}


	/**
	 * 返回所有的匹配
	 * 
	 * @param source
	 * @param patten
	 * @param index
	 * @return
	 */
	public static List<String> getAllMatched(String source, String patten, int index) {
		List<String> list = new ArrayList<String>();
		if (source != null && patten != null) {
			try {
				Perl5Matcher matcher = new Perl5Matcher();
				Pattern pattern = getPattern(patten);
				PatternMatcherInput input = new PatternMatcherInput(source);
				while (matcher.contains(input, pattern)) {
					list.add(matcher.getMatch().group(index));
				}
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}
		}
		return list;
	}

	/**
	 * 返回所有匹配的字符串
	 * 
	 * @param source
	 *            字符传
	 * @param patten
	 *            正则表达式
	 * @param replaceRegex
	 *            替换正则表达式
	 * @param split
	 *            分割富豪
	 * @return
	 */
	public static String getAllMatchedStr(String source, String patten,
			String replaceRegex, String split) {

		try {
			// 捕获所有匹配
			List<MatchResult> list = getAllMatchResult(source, patten);
			if (list == null || list.size() == 0)
				return null;

			StringBuffer buf = new StringBuffer();
			MatchResult match = null;
			String result = null;
			// 遍历匹配
			for (int j = 0; j < list.size(); j++) {
				match = list.get(j);
				result = match.group(1);
				// 替换replaceRegex中的^1、^2、^3...等参数
				if (replaceRegex != null && !replaceRegex.trim().equals("")) {
					result = replaceRegex;
					int count = match.groups();
					for (int i = 1; i < count; i++) {
						result = result.replaceAll("\\^" + i, match.group(i));
					}
				}
				if (j > 0)
					buf.append(split);
				buf.append(result);
			}
			return buf.toString();
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
		return null;
	}

	/**
	 * @param source
	 * @param patten
	 * @return
	 * @throws Exception
	 */
	public static List<MatchResult> getAllMatchResult(String source, String patten)
			throws Exception {
		if (source == null || patten == null)
			return null;
		List<MatchResult> list = new ArrayList<MatchResult>();
		String pattens[] = StringUtil.splitByStr(patten, TAG_REGEX_SPLIT);
		PatternMatcherInput input = new PatternMatcherInput(source);
		for (int i = 0; i < pattens.length; i++) {
			Perl5Matcher matcher = new Perl5Matcher();
			Pattern pattern = getPattern(pattens[i]);
			while (matcher.contains(input, pattern)) {
				list.add(matcher.getMatch());
			}
		}
		return list;
	}

	/**
	 * 获取匹配的中间字符串
	 * 
	 * @param source
	 * @param beginPatten
	 * @param endPatten
	 * @return
	 * @throws ContentError
	 */
	public static String getBetween(String source, String beginPatten, String endPatten)
			throws Exception {
		if (source == null)
			return null;
		int beginPos = 0;
		int endPos = -1;

		MatchResult resultBegin = null;
		// 查找开始位置
		if (beginPatten != null && !beginPatten.equals("")) {
			resultBegin = getMatchResult(source, beginPatten);
			if (resultBegin == null)
				throw new Exception("beginPatten not found," + beginPatten);
			String begin = resultBegin.group(0);
			beginPos = resultBegin.beginOffset(0) + begin.length();
		}

		// 查找结束位置
		if (endPatten != null && !endPatten.equals("")) {
			String tail = source;
			if (resultBegin != null)
				tail = source.substring(beginPos);
			MatchResult resultEnd = getMatchResult(tail, endPatten);
			if (resultEnd == null)
				throw new Exception("endPatten not found," + endPatten);
			endPos = beginPos + resultEnd.beginOffset(0);

		}

		if (beginPos >= 0 && endPos >= 0) {
			return source.substring(beginPos, endPos);
		} else if (beginPos > 0)
			return source.substring(beginPos);
		else if (beginPos == 0)
			return source;
		else {
			return null;
		}
	}

	/**
	 * 返回匹配的字符串
	 * 
	 * @param source
	 *            字符传
	 * @param patten
	 *            正则表达式
	 * @param index
	 *            匹配的索引
	 * @return
	 */
	public static String getMatchedStr(String source, String patten, int index) {
		MatchResult result = getMatchResult(source, patten);
		if (result != null)
			return result.group(index);
		return null;
	}

	/**
	 * 返回匹配的字符串
	 * 
	 * @param source
	 *            字符传
	 * @param patten
	 *            正则表达式
	 * @param replaceRegex
	 *            替换正则表达式
	 * @return
	 */
	public static String getMatchedStr(String source, String patten,
			String replaceRegex) {
		MatchResult match = getMatchResult(source, patten);
		if (match == null)
			return null;
		String result = match.group(1);
		// 替换replaceRegex中的^1、^2、^3...等参数
		if (replaceRegex != null && !replaceRegex.trim().equals("")) {
			result = replaceRegex;
			int count = match.groups();
			for (int i = 1; i < count; i++) {
				result = result.replaceAll("\\^" + i, match.group(i));
			}
		}
		return result;
	}

	/**
	 * @param source
	 * @param patten
	 * @return
	 */
	public static MatchResult getMatchResult(String source, String patten) {
		if (source == null || patten == null) {
			return null;
		}
		try {
			String pattens[] = StringUtil.splitByStr(patten, TAG_REGEX_SPLIT);
			for (int i = 0; i < pattens.length; i++) {
				Perl5Matcher matcher = new Perl5Matcher();
				Pattern pattern = getPattern(pattens[i]);
				if (matcher.contains(source, pattern)) {
					return matcher.getMatch();
				}
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
		return null;
	}

	/**
	 * 设置模式
	 * 
	 * @param findStr
	 * @return
	 * @throws Exception
	 */
	private static Pattern getPattern(String findStr) throws Exception {
		Pattern pattern = null;
		PatternCompiler compiler = new Perl5Compiler();
		pattern = compiler.compile(findStr, Perl5Compiler.CASE_INSENSITIVE_MASK
				| Perl5Compiler.SINGLELINE_MASK);
		return pattern;
	}


	/**
	 * 返回是否匹配
	 * 
	 * @return
	 */
	public static boolean isMatch(String source, String patten) {
		if (patten == null || source == null)
			return false;
		boolean flag = true;
		if (patten.startsWith("!!")) {
			patten = patten.substring(2);
			flag = false;
		}
		MatchResult result = getMatchResult(source, patten);
		return flag ? result != null : result == null;
	}

}
