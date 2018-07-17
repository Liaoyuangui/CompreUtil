package com.lyg.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Map、消息工具类
 * 
 * @author
 *
 */
public class MsgUtil {

	public static final String STATUS_FAIL = "0";// 状态：执行失败代码
	public static final String STATUS_SUCCESS = "1";// 状态： 执行成功代码
	public static final String FIELD_MESSAGE = "MESSAGE";// 字段：消息字段
	public static final String FIELD_CODE = "CODE";// 字段：代码字段

	public static final String ERROR_FIELD_EMPTY = "提示结果信息数据为空!";// 错误：字段为空
   
	public static final String FIELD_RESULT = "RESULT"; //结果
	/**
	 * 获取消息代码
	 * 
	 * @param status
	 * @param message
	 * @return
	 */
	public static Map<String, Object> getCodeMessage(String status, String message) {
		Map<String, Object> emptyData = new HashMap<String, Object>(3);
		emptyData.put(FIELD_CODE, status);
		emptyData.put(FIELD_MESSAGE, message);
		return emptyData;
	}
	
	/**
	 * 获取错误消息代码
	 * 
	 * @param e 异常类
	 * @return
	 */
	public static Map<String, Object> getCodeMessage(Exception e) {
		Map<String, Object> emptyData = new HashMap<String, Object>(3);
		emptyData.put(FIELD_CODE, STATUS_FAIL);
		emptyData.put(FIELD_MESSAGE, e.getMessage().toString());
		return emptyData;
	}
	
	/**
	 * 获取结果代码
	 * 
	 * @param status 代码
	 * @param result 结果
	 * @return
	 */
	public static Map<String, Object> getCodeResult(String status, Object result) {
		Map<String, Object> emptyData = new HashMap<String, Object>(3);
		emptyData.put(FIELD_CODE, status);
		emptyData.put(FIELD_RESULT, result);
		return emptyData;
	}
}
