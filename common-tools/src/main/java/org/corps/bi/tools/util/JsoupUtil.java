package org.corps.bi.tools.util;

import org.jsoup.safety.Whitelist;

public class JsoupUtil {

	private static Whitelist whitelist = new Whitelist();

	static {
		whitelist.addTags("a", "b", "blockquote", "br", "caption", "cite",
				"code", "col", "colgroup", "dd", "div", "dl", "dt", "em",
				"font", "h1", "h2", "h3", "h4", "h5", "h6", "i", "img", "li",
				"ol", "p", "q", "small", "span", "strike", "strong", "sub",
				"sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr",
				"u", "ul", "embed", "object", "param");

		whitelist.addAttributes(":all", "align", "alt", "bgcolor", "border",
				"bordercolor", "cellspacing", "cellpadding", "color", "face",
				"height", "scrolldelay", "style", "size", "src", "target",
				"title", "width", "autostart", "type", "pluginspage", "wmode",
				"invokeurls", "name", "pluginspage", "quality",
				"allowscriptaccess", "allownetworking", "flashvars", "ver",
				"playerid", "data", "scale", "value", "ubb", "codebase",
				"classid", "menu");

		whitelist.addEnforcedAttribute("a", "rel", "nofollow").addProtocols(
				"a", "href", "http", "https");
		whitelist.addProtocols("img", "src", "http", "https");

	}

	public static Whitelist getWhitelist() {
		return whitelist;
	}
}
