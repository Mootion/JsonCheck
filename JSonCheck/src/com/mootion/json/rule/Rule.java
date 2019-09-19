package com.mootion.json.rule;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 ** 单项规则封装类
 * @author MuCheng Shi
 *
 */
@Data
public class Rule {
	
	/** 字段名称 */
	private String key;
	
	/** 字段显示名称，不设置时显示key的值 */
	private String label;

	/** 类型 */
	private String type;
	
	/** 必填 */
	private Boolean necessary;
	
	/** 最大长度 */
	private Integer maxLength;
	
	/** 最小值 */
	private Double minValue;
	
	/** 最大值 */
	private Double maxValue;
	
	/** 指定值域 */
	private List<Object> valueList;
	
	/** 指定属性值校验 */
	private Map<Rule, List<Rule>> valueRuleList;

	@Override
	public String toString() {
		return "Rule [key=" + key + ", label=" + label + ", type=" + type + ", necessary=" + necessary + ", maxLength="
				+ maxLength + ", minValue=" + minValue + ", maxValue=" + maxValue + ", valueList=" + valueList
				+ ", valueRuleList=" + valueRuleList + "]";
	}

}
