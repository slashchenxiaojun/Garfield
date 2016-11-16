package org.hacker.common;

import java.util.Properties;

import com.jfinal.kit.PropKit;

/**
 * 配置服务
 * 
 * @version 1.0
 * @author Mr.J.(slashchenxiaojun@sina.com)
 * 
 * @date 2015-05-17
 **/
public class ProKit {
	
	public static String getInfo(String configName, String key){
		return getInfo(configName, "UTF-8", key);
	}
	
	public static String getInfo(String configName, String encoding,  String key){
		//为了写的更加简洁一点(不过这就要求所有的配置文件需要后缀为.properties，COC原则)
		if(!configName.contains(".")){
			configName += ".properties";
		}
		Properties p = PropKit.use(configName, encoding).getProperties();
		return p.getProperty(key);
	}
	
	public static Properties getPro(String configName){
		return getPro(configName, "UTF-8");
	}
	
	public static Properties getPro(String configName, String encoding){
		if(!configName.contains(".")){
			configName += ".properties";
		}
		return PropKit.use(configName, encoding).getProperties();
	}
}
