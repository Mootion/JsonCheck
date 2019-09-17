package com.mootion.json.rule;

import java.util.List;

import lombok.Data;

/**
 ** 完整规则封装类
 * @author MuCheng Shi
 *
 */
@Data
public class RulePack {

	/**   操作名称   */
	private String handleName;
	
	/**   规则列表   */
	private List<Rule> rulerList;

	@Override
	public String toString() {
		return "RulerPack [handleName=" + handleName + ", rulerList=" + rulerList + "]";
	}
	
}
