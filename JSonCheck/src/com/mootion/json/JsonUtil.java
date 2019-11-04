package com.mootion.json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mootion.json.exception.ErrorMessage;
import com.mootion.json.exception.JsonCheckException;
import com.mootion.json.rule.Rule;
import com.mootion.json.rule.RulePack;
import com.mootion.util.check.CheckUtil;

/**
 ** Json对象转换 可扩展值校验
 * 
 * @author MuCheng Shi
 *
 */
public class JsonUtil {

	/**
	 ** Json对象属性解析
	 * 
	 * @param json
	 * @param pack 规则见RulerPack对象说明
	 * @return
	 * @throws JsonCheckException
	 */
	public static Map<?, ?> jsonCheck(String json, RulePack pack) throws JsonCheckException {
		Map<?, ?> map;
		try {
			map = new Gson().fromJson(json, HashMap.class);
		} catch (JsonSyntaxException e) {
			throw new JsonCheckException("Json解析失败！");
		}
		if (pack != null) {
			List<Rule> rulerList = pack.getRuleList();
			ErrorMessage msg = new ErrorMessage(pack.getHandleName());
			if (CheckUtil.isNotEmpty(rulerList)) {
				for (Rule ruler : rulerList) {
					ruleCheck(map, ruler, rulerList, msg);
				}
			}
			if (msg.hasErrorMessage()) {
				throw new JsonCheckException(msg.toString());
			}
		}
		return map;
	}

	/**
	 ** Json对象属性解析
	 * 
	 * @param json
	 * @param ruleJson 规则见RulerPack对象说明
	 * @return
	 * @throws JsonCheckException
	 */
	public static Map<?, ?> jsonCheck(String json, String ruleJson) throws JsonCheckException {
		return jsonCheck(ruleJson, getRulerPack(ruleJson));
	}

	/**
	 ** Json对象属性封装
	 * 
	 * @param <T>          按照泛型返回对象
	 * @param json
	 * @param pack         规则见RulerPack对象说明
	 * @param requiredType
	 * @return
	 * @throws JsonCheckException
	 */
	public static <T> T jsonFormart(String json, RulePack pack, Class<T> requiredType, Map<Class<?>, Object> map)
			throws JsonCheckException {
		jsonCheck(json, pack);
		GsonBuilder gb = new GsonBuilder();
		if(CheckUtil.isNotEmpty(map)) {
			for (Class<?> fromClass : map.keySet()) {
				gb = gb.registerTypeAdapter(fromClass, map.get(fromClass));
			}
		}
		Gson g = gb.create();
		return g.fromJson(json, requiredType);
	}

	/**
	 ** Json对象属性封装
	 * 
	 * @param <T>          按照泛型返回对象
	 * @param json
	 * @param ruleJson     规则见RulerPack对象说明
	 * @param requiredType
	 * @return
	 * @throws JsonCheckException
	 */
	public static <T> T jsonFormart(String json, String ruleJson, Class<T> requiredType, Map<Class<?>, Object> map)
			throws JsonCheckException {
		return jsonFormart(json, getRulerPack(ruleJson), requiredType, map);
	}

	private static RulePack getRulerPack(String rulerJson) throws JsonCheckException {
		try {
			return new Gson().fromJson(rulerJson, RulePack.class);
		} catch (JsonSyntaxException e) {
			throw new JsonCheckException("Json校验不可用，请联系后台管理员！");
		}
	}

	/**
	 ** 规则校验
	 ** 暂不支持遍历List 
	 * @param map
	 * @param rule
	 * @param rulerList
	 * @param msg
	 * @return
	 */
	private static boolean ruleCheck(Map<?, ?> map, Rule rule, List<Rule> rulerList, ErrorMessage msg) {
		String key = rule.getKey();
		Object value = getValue(map, key);
		String label = rule.getLabel() == null ? rule.getKey() : rule.getLabel();
		return checkValue(map, value, label, rule, msg);
	}

	/**
	 ** 获取值
	 * @param map
	 * @param key
	 * @return
	 */
	private static Object getValue(Map<?, ?> map, String key) {
		 Object value = null;
		if (map != null) {
			if (key.indexOf(".") == -1) {
				value = map.get(key);
			} else {
				String[] keyList = key.split("[.]");
				Map<?, ?> childMap = map;
				for (int i = 0; i < keyList.length - 1; i++) {
					Object cur = childMap.get(keyList[i]);
					if (cur == null) {
						childMap = null;
						break;
					}
					childMap = (Map<?, ?>)cur ;
				}
				if (childMap == null) {
					value = null;
				} else {
					value = childMap.get(keyList[keyList.length - 1]);
				}
			}
		}
		return value;
	}

	/**
	 ** 值校验
	 * @param value
	 * @param label
	 * @param rule
	 * @param msg
	 * @return
	 */
	private static boolean checkValue(Map<?, ?> map, Object value,String label,Rule rule,ErrorMessage msg) {
		// 必填校验
		if (!checkNecessary(value, label, rule.getNecessary(), msg)) {
			return false;
		}
		// 类型校验
		if (!checkType(value, label, rule.getType(), msg)) {
			return false;
		}
		// 格式校验
		if (!checkFormat(value, label, rule.getType(), rule.getFormat(), msg)) {
			return false;
		}
		// 长度校验
		if (!checkLength(value, label, rule.getMaxLength(), msg)) {
			return false;
		}
		// 值域校验 -- 最大最小值校验
		if (!checkValueArea(value, label, rule.getType(), rule.getMinValue(), rule.getMaxValue(), msg)) {
			return false;
		}
		// 值域校验 -- 可选值校验
		if (!checkValueExist(value, label, rule.getType(), rule.getValueList(), msg)) {
			return false;
		}
		// 连带值校验 -- 文本未优化
		if (!checkValueRulerList(value, label, rule.getValueRuleList(), map, msg)) {
			return false;
		}
		return true;
	}
	
	/**
	 ** 连带值校验 -- 文本未优化
	 * 
	 * @param value
	 * @param label
	 * @param valueRulerList
	 * @param map
	 * @param msg
	 * @return
	 */
	private static boolean checkValueRulerList(Object value, String label, Map<Rule, List<Rule>> valueRulerList,
			Map<?, ?> map, ErrorMessage msg) {
		if (CheckUtil.isNotEmpty(valueRulerList)) {
			for (Rule keyRuler : valueRulerList.keySet()) {
				boolean checked = false;
				if (keyRuler.getMinValue() != null || keyRuler.getMaxLength() != null) {
					checked = checkValueArea(value, label, keyRuler.getType(), keyRuler.getMinValue(),
							keyRuler.getMaxValue(), null);
				}
				if (CheckUtil.isNotEmpty(keyRuler.getValueList())) {
					checked = checkValueExist(value, label, keyRuler.getType(), keyRuler.getValueList(), null);
				}
				// TODO 连带值校验文本优化
				if (checked) {
					return ruleCheck(map, keyRuler, valueRulerList.get(keyRuler), msg);
				}
			}
		}
		return true;
	}

	/**
	 ** 必填校验
	 * 
	 * @param value
	 * @param label
	 * @param nessecary
	 * @param msg
	 * @return
	 */
	private static boolean checkNecessary(Object value, String label, Boolean necessary, ErrorMessage msg) {
		if (necessary != null && necessary) {
			if (value == null || String.valueOf(value).length() == 0) {
				if (msg != null) {
					msg.addErrorMessage(label + "不能为空");
				}
				return false;
			}
		}
		return true;
	}

	/**
	 ** 类型校验
	 * 
	 * @param value
	 * @param label
	 * @param type
	 * @param msg
	 * @return
	 */
	private static boolean checkType(Object value, String label, String type, ErrorMessage msg) {
		if (value != null && CheckUtil.isNotBlank(type)) {
			if (DataType.INTEGER.equals(type) && !CheckUtil.isIntegerValue(value.toString())) {
				// 整型
				if (msg != null) {
					msg.addErrorMessage(label + "不是一个整数");
				}
				return false;
			} else if (DataType.DOUBLE.equals(type) && !CheckUtil.isDoubleValue(value.toString())) {
				// 浮点型
				if (msg != null) {
					msg.addErrorMessage(label + "不是一个数字");
				}
				return false;
			} else if (DataType.BOOLEAN.equals(type)) {
				// 布尔型
				if (!(value instanceof Boolean)) {
					if (msg != null) {
						msg.addErrorMessage(label + "不是布尔值");
					}
					return false;
				}
			} else if (DataType.OBJECT.equals(type)) {
				// 对象型
				if (!(value instanceof Map)) {
					if (msg != null) {
						msg.addErrorMessage(label + "不是对象");
					}
					return false;
				}
			} else if (DataType.ARRAY.equals(type)) {
				// 数组型
				if (!(value instanceof List)) {
					if (msg != null) {
						msg.addErrorMessage(label + "不是数组");
					}
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 格式校验
	 * 
	 * @param value
	 * @param label
	 * @param type
	 * @param format
	 * @param msg
	 * @return
	 */
	private static boolean checkFormat(Object value, String label, String type, String format, ErrorMessage msg) {
		if (value != null && CheckUtil.isNotBlank(format)) {
			// 日期类型校验
			if (DataType.DATE.equals(type)) {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				try {
					sdf.parse(value.toString());
				} catch (ParseException e) {
					if (msg != null) {
						msg.addErrorMessage(label + ":" + value + "不符合规则" + format);
					}
					return false;
				}
			}
			// TODO 正则校验
		}
		return true;
	}

	/**
	 ** 长度校验
	 * 
	 * @param value
	 * @param label
	 * @param maxLength
	 * @param msg
	 * @return
	 */
	private static boolean checkLength(Object value, String label, Integer maxLength, ErrorMessage msg) {
		if (value != null && maxLength != null && value.toString().getBytes().length > maxLength) {
			if (msg != null) {
				msg.addErrorMessage(label + "超出最大长度限制：" + maxLength);
			}
			return false;
		}
		return true;
	}

	/**
	 ** 值域校验 - 数字指定最大最小值
	 * 
	 * @param value
	 * @param label
	 * @param type
	 * @param minValue
	 * @param maxValue
	 * @param msg
	 * @return
	 */
	private static boolean checkValueArea(Object value, String label, String type, Double minValue, Double maxValue,
			ErrorMessage msg) {
		if (value != null && (DataType.INTEGER.equals(type) || DataType.DOUBLE.equals(type))) {
			double dValue = Double.valueOf(value.toString());
			if (minValue != null && dValue < minValue) {
				if (msg != null) {
					if (DataType.INTEGER.equals(type)) {
						msg.addErrorMessage(label + "当前值：" + value + "不可小于" + minValue.intValue());
					} else {
						msg.addErrorMessage(label + "当前值：" + value + "不可小于" + minValue);
					}
				}
				return false;
			}
			if (maxValue != null && dValue > maxValue) {
				if (msg != null) {
					if (DataType.INTEGER.equals(type)) {
						msg.addErrorMessage(label + "当前值：" + value + "不可大于" + maxValue.intValue());
					} else {
						msg.addErrorMessage(label + "当前值：" + value + "不可大于" + maxValue);
					}
				}
				return false;
			}
		}
		return true;
	}

	/**
	 ** 值域校验 - 指定可选值
	 * 
	 * @param value
	 * @param label
	 * @param valueList
	 * @param msg
	 * @return
	 */
	private static boolean checkValueExist(Object value, String label, String type, List<Object> valueList,
			ErrorMessage msg) {
		boolean result = true;
		String strList = "";
		if (value != null && CheckUtil.isNotEmpty(valueList)) {
			String strValueList = valueList.toString();
			if (DataType.INTEGER.equals(type) || DataType.DOUBLE.equals(type)) {
				double doubleValue = Double.parseDouble(value.toString());
				if (!valueList.contains(doubleValue)) {
					result = false;
					if (DataType.INTEGER.equals(type)) {
						strList = strValueList.substring(1, strValueList.length() - 1).replaceAll(".0", "");
					} else {
						strList = strValueList.substring(1, strValueList.length() - 1);
					}
				}
			} else if (!valueList.contains(value)) {
				result = false;
				strList = strValueList.substring(1, strValueList.length() - 1);
			}
		}
		if (!result && msg != null) {
			msg.addErrorMessage(label + "当前值：" + value + "必须在" + strList + "之中");
		}
		return result;
	}

}
