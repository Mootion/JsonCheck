package com.mootion.util.check;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 ** 快速校验工具类
 * @author MuCheng Shi
 *
 */
public class CheckUtil {

	/**
	 ** 判断集合是否为空
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(List<? extends Object> list) {
		return list == null || list.size() == 0;
	}

	/**
	 ** 判断集合是否不为空
	 * @param list
	 * @return
	 */
	public static boolean isNotEmpty(List<? extends Object> list) {
		return !isEmpty(list);
	}

	/**
	 ** 判断字符串是否有值
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 ** 判断字符串是否无值
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}
	
	/**
	 ** 判断对象是否有文本值
	 * @param obj
	 * @return
	 */
	public static boolean isBlank(Object obj) {
		return obj == null || isBlank(obj.toString());
	}
	
	/**
	 ** 判断对象是否无文本值
	 * @param obj
	 * @return
	 */
	public static boolean isNotBlank(Object obj) {
		return !isBlank(obj);
	}

	private final static Pattern INTEGER_PATTERN = Pattern.compile("^[-+][\\d]+[.]?[0]*");;

	/**
	 ** 整数格式校验
	 * @param str
	 * @return
	 */
	public static boolean isIntegerValue(String str) {
		return INTEGER_PATTERN.matcher(str).matches();
	}

	private final static Pattern DOUBLE_PATTERN = Pattern.compile("^[-+]?\\d*[.]\\d+$");

	public static boolean isDoubleValue(String str) {
		return DOUBLE_PATTERN.matcher(str).matches();
	}

	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}
	
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.size() == 0;
	}
}
