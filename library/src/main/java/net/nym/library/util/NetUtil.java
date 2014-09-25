package net.nym.library.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class NetUtil {

	/**
	 * 编码
	 * 
	 * @param text
	 * @return
	 */
	public static String URLEncode(String text) {
		String result = null;
		try {
			result = URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * 解码
	 * 
	 * @param text
	 * @return
	 */
	public static String URLDecode(String text) {
		String result = null;
		try {
			result = URLDecoder.decode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("URLDecode=%s", result);
		return result;

	}

	/**
	 * @param str
	 * @return
	 */
	public static String ChangeToHtml(String str) {
		// str含有HTML标签的文本
		str = str.replace("&amp;", "&");
		str = str.replace((char) 0, (char) 13);
		str = str.replace("&amp;", "&");
		str = str.replace("&lt;", "<");
		str = str.replace("&gt;", ">");
		str = str.replace("&nbsp;", " ");
		str = str.replace("&lt;br&gt;", "\n");
		return str;
	}
}
