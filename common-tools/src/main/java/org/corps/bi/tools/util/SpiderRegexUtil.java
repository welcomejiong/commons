package org.corps.bi.tools.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderRegexUtil {

	private static final String JAVASCRIPT_TAG_REGEX = "(<script[^>]+>.*?</script>)|(<script[^>]+>)";

	private static final String NO_JAVASCRIPT_TAG_REGEX = "(<noscript[^>]+>.*?</noscript>)|(<noscript[^>]+>)";

	private static final String CSS_TAG_REGEX = "<link[^>]+>";

	private static final String IFRAME_TAG_REGEX = "(<iframe[^>]+>)|(</iframe>)";

	private static final String LINK_TAG_REGEX = "(<a[^>]+>)|(</a>)";

	private static final String DEFAULT_EMPTY = "";

	public static String clearJsCssIframeLink(String source) {
		if (StringUtils.isBlank(source)) {
			return source;
		}
		source = replaceJavascriptTag(source);
		source = replaceCssTag(source);
		source = replaceIframeTag(source);
		source = replaceLinkTag(source);
		return source;
	}

	public static String replaceJavascriptTag(String source) {
		try {
			source = RegexUtils.replaceAll(source, JAVASCRIPT_TAG_REGEX,
					DEFAULT_EMPTY);
			return RegexUtils.replaceAll(source, NO_JAVASCRIPT_TAG_REGEX,
					DEFAULT_EMPTY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return source;
	}

	public static String replaceCssTag(String source) {
		try {
			return RegexUtils.replaceAll(source, CSS_TAG_REGEX, DEFAULT_EMPTY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return source;
	}

	public static String replaceIframeTag(String source) {
		try {
			return RegexUtils.replaceAll(source, IFRAME_TAG_REGEX,
					DEFAULT_EMPTY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return source;
	}

	public static String replaceLinkTag(String source) {
		try {
			return RegexUtils.replaceAll(source, LINK_TAG_REGEX, DEFAULT_EMPTY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return source;
	}

	public static String getMetaKeywords(Document document) throws Exception {
		Elements keywordEles = document.select("meta[name=keywords]");
		if (keywordEles != null && !keywordEles.isEmpty()) {
			Element keywordEle = keywordEles.get(0);
			String keyword = keywordEle.attr("content");
			if (StringUtils.isNotEmpty(keyword) && keyword.endsWith(",")) {
				keyword = RegexUtils.replaceAll(keyword, ",$", "");
			}
			return keyword;
		}
		return null;
	}

	public static String getMetaDescription(Document document) throws Exception {
		Elements descriptionEles = document.select("meta[name=description]");
		if (descriptionEles != null && !descriptionEles.isEmpty()) {
			Element descriptionEle = descriptionEles.get(0);
			String description = descriptionEle.attr("content");
			description = RegexUtils.replaceAll(description, "\"", "");
			description = RegexUtils.replaceAll(description, "\\r\\n", " ");
			description = RegexUtils.replaceAll(description, "\\r", " ");
			description = RegexUtils.replaceAll(description, "\\n", " ");
			description = RegexUtils.replaceAll(description, "\\t", " ");
			// NOTE:必须是空格
			description = RegexUtils.replaceAll(description, "&nbsp;", " ");
			// description = RegexUtils.replaceAll(description, "\\s", " ");
			description = RegexUtils.replaceAll(description, "  ", " ");
			description = RegexUtils.replaceAll(description, "  ", " ");
			description = RegexUtils.replaceAll(description, "  ", " ");
			description = RegexUtils.replaceAll(description, "  ", " ");
			description = RegexUtils.replaceAll(description, "  ", " ");
			description = RegexUtils.replaceAll(description, "  ", " ");
			return description;
		}
		return null;
	}

	public static String getMetaDescriptionFromContent(String contentTxt)
			throws Exception {
		String description = "";
		if (StringUtils.isBlank(contentTxt)) {
			return description;
		}
		int length = contentTxt.length();
		if (length < 80) {
			return contentTxt;
		}
		if (length > 150) {
			description = contentTxt.substring(0, 150);
		} else if (length > 100) {
			description = contentTxt.substring(0, 100);
		} else {
			description = contentTxt;
		}
		return description;
	}

	public static String getText(String str) throws Exception {
		str = RegexUtils.replaceAll(str, "\"", "");
		str = RegexUtils.replaceAll(str, "\"", "");
		str = RegexUtils.replaceAll(str, "\\r\\n", " ");
		str = RegexUtils.replaceAll(str, "\\r", " ");
		str = RegexUtils.replaceAll(str, "\\n", " ");
		str = RegexUtils.replaceAll(str, "\\t", " ");
		// NOTE:必须是空格
		str = RegexUtils.replaceAll(str, "&nbsp;", " ");
		// str = RegexUtils.replaceAll(str, "\\s", " ");
		str = RegexUtils.replaceAll(str, "  ", " ");
		str = RegexUtils.replaceAll(str, "  ", " ");
		str = RegexUtils.replaceAll(str, "  ", " ");
		str = RegexUtils.replaceAll(str, "  ", " ");
		str = RegexUtils.replaceAll(str, "  ", " ");
		str = RegexUtils.replaceAll(str, "  ", " ");

		return str;
	}

}
