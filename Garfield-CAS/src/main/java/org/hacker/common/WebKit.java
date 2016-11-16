package org.hacker.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

/**
 * A Web helper tools
 * 
 * @version 1.0
 * @author Mr.J.(slashchenxiaojun@sina.com)
 * 
 * @date 2015-03-28
 * **/
public class WebKit {
	
	private static Location getLocation(HttpServletRequest request) {
		Location location = new Location();
		location.hash = "";
		location.search = request.getQueryString() == null ? "" : "?" + request.getQueryString();
		location.protocol = request.getScheme();
		location.hostname = request.getServerName();
		location.pathname = request.getRequestURI();
		location.port = request.getServerPort() == 80 ? "" : request.getServerPort() + "";
		location.host = location.hostname + (location.port.equals("") ? "" : ":" + location.port);
		location.origin = location.protocol + "://" + location.host;
		location.href = location.origin + location.pathname + location.search + location.hash;
		return location;
	}
	
	/**
	 * 获取绝对全局路径
	 * */
	public static String getPath(HttpServletRequest request) {
		Location location = getLocation(request);
		return location.origin;
	}
	
	/**
	 * 获取现有URI字符串(包含servletPath)，包括请求参数(多个参数用','分割)
	 * */
	public static String getPathAndParamter(HttpServletRequest request) {
		Location location = getLocation(request);
		return location.href;
	}
	
	/**
	 * URL反编码
	 * 
	 * @param url
	 * @param enc 编码
	 * @throws UnsupportedEncodingException
	 * */
	public static String getURLDecoder(String url, String enc) {
		try {
			return URLDecoder.decode(url, enc);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * URL编码
	 * 
	 * @param url
	 * @param enc 编码
	 * @throws UnsupportedEncodingException
	 * */
	public static String getURLEncoder(String url, String enc) {
		try {
			return URLEncoder.encode(url, enc);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
}

// pojo location bean like js window.location obj
class Location {
	String hash;
	String host;
	String hostname;
	String href;
	String origin;
	String pathname;
	String port;
	String protocol;
	String search;
}