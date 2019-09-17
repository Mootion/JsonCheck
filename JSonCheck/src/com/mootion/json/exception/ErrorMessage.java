package com.mootion.json.exception;

import java.util.ArrayList;
import java.util.List;

import com.mootion.util.check.CheckUtil;

import lombok.Data;

/**
 ** 异常信息整合输出类
 * @author JLNX
 *
 */
@Data
public class ErrorMessage {

	/**  操作名称    */
	private String handlerName;

	/**  分隔符    */
	private String separation = "、";
	
	/**  消息列表    */
	private List<String> errorMessageList = new ArrayList<String>();
	

	public ErrorMessage() {
		super();
	}
	
	/**
	 ** 异常信息整合输出类 使用默认分隔符、进行消息拼接
	 * @param handlerName
	 */
	public ErrorMessage(String handlerName) {
		super();
		this.handlerName = handlerName;
	}

	public ErrorMessage(String handlerName, String separation) {
		super();
		this.handlerName = handlerName;
		this.separation = separation;
	}
	

	public void addErrorMessage(String message) {
		errorMessageList.add(message);
	}
	
	public boolean hasErrorMessage() {
		return CheckUtil.isNotEmpty(errorMessageList);
	}

	@Override
	public String toString() {
		if (CheckUtil.isEmpty(errorMessageList)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		if (CheckUtil.isNotBlank(handlerName)) {
			sb.append(handlerName);
		}
		sb.append("Json校验失败：");
		for (String message : errorMessageList) {
			sb.append(message).append(separation);
		}
		return sb.substring(0, sb.length() - 1);
	}

}
