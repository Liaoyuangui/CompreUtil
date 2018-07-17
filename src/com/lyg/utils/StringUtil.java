package com.lyg.utils;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/***
 * 字符串相关工具类
 * 
 * @author xiaoguizi
 * 
 */
public class StringUtil extends StringUtils {
	private static String localIp;

	/***
	 * 查询参数转义
	 * 
	 * @param str
	 * @return
	 */
	public final static String sqlParamEscape(String str) {
		return str.replaceAll("'", "\"");

	}

	/***
	 * 取当前时间戳字符
	 * 
	 * @return
	 */
	public final static String curTimestamp() {
		return Long.toString(new Date().getTime());
	}

	/****
	 * 去除尾巴固定字符
	 * 
	 * @param str
	 * @param ge
	 * @return
	 */
	public final static String rtrim(String str, String ge) {
		return NulltoEmpty(str).replaceAll(ge + "*$", "");

	}

	/***
	 * 英文字符集转中文
	 * 
	 * @param str
	 * @return
	 */
	public final static String ascii2GBK(String str) {
		try {
			return new String(str.getBytes("ISO-8859-1"), "GBK");
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

	/***
	 * 英文字符集转中文
	 * 
	 * @param str
	 * @return
	 */
	public final static String asciiUTF8(String str) {
		try {
			return new String(str.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

	/***
	 * 中文转英文字符集
	 * 
	 * @param str
	 * @return
	 */
	public final static String GBK2ascii(String str) {
		try {
			return new String(str.getBytes("GBK"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

	/**
	 * 获取访问者IP
	 * 
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 * 
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	 * 如果还不存在则调用Request .getRemoteAddr()。
	 * 
	 * @param request
	 * @return
	 */
	public final static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteHost();
		}
	}

	/**
	 * 获取本机IP
	 * 
	 * @return
	 */
	public final static String getLocalIpAddr() {
		if (localIp != null)
			return localIp;
		try {
			Enumeration<?> netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces
						.nextElement();
				if (ni.isLoopback())
					continue;
				if (ni.isVirtual()) {
					continue;
				}
				Enumeration<?> addresss = ni.getInetAddresses();
				while (addresss.hasMoreElements()) {
					InetAddress address = (InetAddress) addresss.nextElement();
					if (address instanceof Inet4Address) {
						ip = address;
						break;
					}
				}
				if (ip != null) {
					break;
				}
			}
			if (ip != null)
				localIp = ip.getHostAddress();
			else
				localIp = "127.0.0.1";

		} catch (Exception e) {
			localIp = "127.0.0.1";
		}
		return localIp;
	}

	/****
	 * 处理JSON 中存在‘null’问题
	 * 
	 * @param obj
	 * @return
	 */
	public final static String NulltoEmpty(Object obj) {
		return isNotNull(obj) ? obj.toString().trim() : "";
	}

	/****
	 * 字符前后空格处理
	 * 
	 * @param obj
	 * @return
	 */
	public final static String spaceTrim(String str) {
		return isNotNull(str) ? str.trim() : null;
	}

	/***
	 * 处理页面提交到后台得参数中的html标记处理
	 * 
	 * @param param
	 * @return
	 */
	public final static String htmlSignDandle(String param) {
		return isNotNull(param) ? param.replaceAll("<", "&lt;").replaceAll(">",
				"&gt;") : "";
	}

	/**
	 * 半角转全角
	 * 
	 * @param input
	 *            String.
	 * @return 全角字符串.
	 */
	public final static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}

	/**
	 * 全角转半角
	 * 
	 * @param input
	 *            String.
	 * @return 半角字符串
	 */
	public final static String ToDBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);
		return returnString;
	}

	/***
	 * CH 移动电话号码验证
	 * 
	 * @param phoneNum
	 * @return true|false
	 */
	public final static boolean isMoblePhone(String phoneNum) {
		return isNotEmpty(phoneNum) && phoneNum.matches("^1[3-9]\\d{9}");
	}

	/***
	 * CH 固定电话验证
	 * 
	 * @param phoneNum
	 * @return true|false
	 */
	public final static boolean isTelPhone(String phoneNum) {
		return isNotEmpty(phoneNum)
				&& phoneNum.matches("^(0[0-9]{2,3}-?)?([2-9][0-9]{6,7})+$");
	}

	/***
	 * email 电子邮件格式验证
	 * 
	 * @param phoneNum
	 * @return true|false
	 */
	public final static boolean isEmail(String email) {
		return isNotEmpty(email)
				&& email.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
	}

	/***
	 * 有效整数验证
	 * 
	 * @param phoneNum
	 * @return true|false
	 */
	public final static boolean isInt(String num) {
		return isNotEmpty(num) && num.matches("^[1-9]+\\d*$");
	}

	/***
	 * 有效浮点数验证
	 * 
	 * @param phoneNum
	 * @return true|false
	 */
	public final static boolean isFloat(String num) {
		return isNotEmpty(num) && num.matches("^[0-9]+\\d*(.?)\\d+$");
	}

	/***
	 * 有效数字验证
	 * 
	 * @param num
	 * @return true|false
	 */
	public final static boolean isNumber(String num) {
		return isNotEmpty(num) && num.matches("^[1-9]+\\d*(.?)\\d+$");
	}

	/***
	 * 只有数字组合
	 * 
	 * @param num
	 * @return true|false
	 */
	public final static boolean isOnlyNum(String num) {
		return isNotEmpty(num) && num.matches("\\d+$");
	}

	/***
	 * 只有数字字母组合
	 * 
	 * @param num
	 * @return true|false
	 */
	public final static boolean isOnlyNumAndLetters(String num) {
		return isNotEmpty(num)
				&& num.matches("^(([A-Za-z]+[0-9]+)+|([0-9]+[A-Za-z]+))+$");
	}

	/****
	 * 空值验证
	 * 
	 * @param Object
	 * @return true|false
	 */
	public final static boolean isNotNull(Object obj) {
		return obj != null;
	}

	/****
	 * 空值验证
	 * 
	 * @param Str
	 * @return true|false
	 */
	public final static boolean isNotEmpty(String str) {
		return isNotNull(str) && str.trim().length() > 0;
	}

	/****
	 * 返回字符长度
	 * 
	 * @param Str
	 *            字符
	 * @return Int
	 */
	public final static int length(String str) {
		return isNotNull(str) ? str.length() : 0;
	}

	/****
	 * 中文字符集
	 * 
	 * @param num
	 * @return true|false
	 */
	public final static boolean isChinese(String num) {
		return isNotEmpty(num) && num.matches("[\u4e00-\u9fa5]+");
	}


	/***
	 * 字符长度验证
	 * 
	 * @param num
	 * @param minLength
	 *            最小长度
	 * @param maxLength
	 *            最大长度
	 * @return true||false
	 */
	public final static boolean isValidLength(String num, long minLength,
			long maxLength) {
		if (isNotEmpty(num)) {
			final long len = num.length();
			if (len >= minLength && len <= maxLength)
				return true;
		}
		return false;
	}

	/***
	 * 固定字符长度验证
	 * 
	 * @param num
	 * @param length
	 *            固定长度
	 * @return true||false
	 */
	public final boolean isValidLength(String num, long length) {
		return isNotNull(num) && num.length() == length;
	}

	/***
	 * 字符长度验证
	 * 
	 * @param num
	 * @param maxLength
	 *            最大长度
	 * @return true||false
	 */
	public final static boolean isValidMaxLength(String num, long maxLength) {
		return isValidLength(num, 0, maxLength);
	}

	/***
	 * 字符长度验证
	 * 
	 * @param num
	 * @param minLength
	 *            最小长度
	 * @return true||false
	 */
	public final static boolean isValidMinLength(String num, long minLength) {
		return isValidLength(num, minLength, Long.MAX_VALUE);
	}

	/***
	 * 字符转换为日期对象
	 * 
	 * @param source
	 *            日期字符
	 * @param format
	 *            转换格式【YYYY-MM-DD】 yyyy-MM-dd HH:mm:ss SSSS
	 * @return Date
	 */
	public final static Date format(String source, String format) {
		Object obj = innerFormat(null, source, format);
		return isNotNull(obj) ? (Date) obj : null;
	}

	/**
	 * 将日期转为字符
	 * 
	 * @param date
	 *            日期对象
	 * @param format
	 *            转换格式【YYYY-MM-DD】 yyyy-MM-dd HH:mm:ss SSSS
	 * @return String
	 */
	public final static String format(Date date, String format) {
		return innerFormat(date, null, format) + "";
	}

	/**
	 * 验证身份证格式
	 * 
	 * @param str
	 * @return
	 */
	public final static boolean isIDCard(String str) {
		if (isNotEmpty(str)) {
			int len = str.length();
			if (len == 15 || len == 18)
				return str
						.matches("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
		}
		return false;

	}

	/**
	 * 日期字符间转换内部实现
	 * 
	 * @param date
	 *            日期对象
	 * @param source
	 *            日期字符
	 * @param format
	 *            格式化字符
	 * @return Object
	 */
	private static Object innerFormat(Date date, String source, String format) {
		SimpleDateFormat fmt = new SimpleDateFormat(
				format == null ? "yyyy-MM-dd" : format);
		if (isNotNull(date))
			return fmt.format(date);
		else if (isNotNull(source))
			try {
				return fmt.parse(source);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return null;
	}

	/**
	 * 驼峰字符转固定字符
	 * 
	 * @param param
	 * @param char
	 * @return
	 */
	public static String camelToCharString(String param, char ch) {
		if (!isNotEmpty(param))
			return "";
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append(ch);
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 字符串首字母小写
	 * 
	 * @param str
	 * @return
	 */
	public static String toLowerCaseFirstOne(String str) {
		if (str == null || "".equals(str))
			return str;
		if (Character.isLowerCase(str.charAt(0)))
			return str;
		else
			return (new StringBuilder())
					.append(Character.toLowerCase(str.charAt(0)))
					.append(str.substring(1)).toString();
	}

	/**
	 * 字符串首字母大写
	 * 
	 * @param str
	 * @return
	 */
	public static String toUpperCaseFirstOne(String str) {
		if (str == null || "".equals(str))
			return str;
		if (Character.isUpperCase(str.charAt(0)))
			return str;
		else
			return (new StringBuilder())
					.append(Character.toUpperCase(str.charAt(0)))
					.append(str.substring(1)).toString();
	}

	public static void main(String[] args) {

		/*
		 * String QJstr = "wch"; String QJstr1 = "ｈｅｌｌｏ．．．．８８８８８８"; String
		 * result = ToSBC(QJstr); String result1 = ToDBC(QJstr1);
		 * System.out.println(QJstr + "\n" + result); System.out.println(QJstr1
		 * + "\n" + result1);
		 * System.out.println(StringUtil.isMoblePhone("13668502657"));
		 * System.out.println(StringUtil.isTelPhone("0851-86668888"));
		 * System.out.println(StringUtil.isEmail("0851-sdaf@qq.com"));
		 * System.out.println(StringUtil.isNumber("0851.77"));
		 * System.out.println(StringUtil.isOnlyNumAndLetters("ss22ss44"));
		 * System.out.println(StringUtil.isValidMinLength("ss22ss44", 7));
		 * org.springframework.util.StringUtils.hasLength("ddddd");
		 * System.out.println("--"+("  sadf   ".trim())+"--");
		 * System.out.println(StringUtil.format(new Date(), "yyyy-MM-dd"));
		 * System.out.println(StringUtil.format(StringUtil.format("2015-90-99",
		 * "yyyy-MM-dd"), "yyyy-MM-dd"));
		 * System.out.println(StringUtil.isDateString("20080431"));*
		 */
		// System.out.println(StringUtil.rtrim("2232000","0"));
		/*
		 * String[] nmStrings=new
		 * String[]{"09.99","9.99","900099","000","sdfe","89s89sdfdd0"};
		 * for(String str : nmStrings){ if(StringUtil.isFloat(str)){
		 * System.out.println(str+"==num"); }else{
		 * System.out.println(str+"==no num"); } }
		 */
		System.out.println(StringUtil.isIDCard("52222919881022261"));
		System.out.println(StringUtil.format(new Date(), "HHmmssSSSS"));
	}

}
