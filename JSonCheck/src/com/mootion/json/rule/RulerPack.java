package com.mootion.json.rule;

import java.util.List;

import lombok.Data;

/**
 ** 完整规则封装类
 * @author MuCheng Shi
 *
 */
@Data
public class RulerPack {

	/**   操作名称   */
	private String handleName;
	
	/**   规则列表   */
	private List<Ruler> rulerList;

	@Override
	public String toString() {
		return "RulerPack [handleName=" + handleName + ", rulerList=" + rulerList + "]";
	}
	
}
