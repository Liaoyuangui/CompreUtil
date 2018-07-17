package com.lyg.utils;

import java.io.InputStream;
import java.util.Properties;


/**
 * 获取properties文件中的值
 * 使用方法：
 * GetPropertiesValueUtils.getInstance("/xx.properties");
 * String property = instance.getProperty("xx");
 * @author Liaoyuangui
 *
 */
public class GetPropertiesValueUtils extends Properties {
	
	private static final long serialVersionUID = 1L;
	private static GetPropertiesValueUtils instance;
	
	public static GetPropertiesValueUtils getInstance(String propertitesFile){
		if(propertitesFile == null){
			return null;
		}
		instance = new GetPropertiesValueUtils(propertitesFile);
		return instance;
		
	}
	
	private GetPropertiesValueUtils(String propertitesFile){
		InputStream is = getClass().getResourceAsStream(propertitesFile);
		try {
			load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private GetPropertiesValueUtils(){
		
	}
	
	//测试
	public static void main(String[] args) {
		GetPropertiesValueUtils instance = GetPropertiesValueUtils.getInstance("/xx.properties");
		String property = instance.getProperty("xx");
		System.out.println(property);
	}

}
