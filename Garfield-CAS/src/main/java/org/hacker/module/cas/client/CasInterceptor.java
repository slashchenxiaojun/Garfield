package org.hacker.module.cas.client;

import org.apache.log4j.Logger;
import org.hacker.module.common.KCode;
import org.hacker.module.common.KWeb;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.ehcache.CacheKit;

public class CasInterceptor implements Interceptor {
	Logger log = Logger.getLogger(CasInterceptor.class);
	
	private static String login_url, verification_url, secret_key;
	private static final String USER_COOKIE_NAME = "USER_SESSION";
	private static final String USER_CACHE_NAME = "SESSION_USERINFO";
	static {
		login_url = "http://127.0.0.1:8080/cas/login?url=";
		verification_url = "http://127.0.0.1:8080/cas/serviceTicketAuthentication";
	}
	
	@Override
	public void intercept(Invocation inv) {
		// get currut url
		String url = KWeb.getPathAndParamter(inv.getController().getRequest());
		String redirect_url = login_url + KWeb.getURLEncoder(url, "UTF-8");
		// first judge request whether ST request
		String casServiceTicket = inv.getController().getPara("casServiceTicket");
		// handler ST request
		if(casServiceTicket != null && !casServiceTicket.equals("")) {
			StringBuffer sb = new StringBuffer();
			sb.
			append("casServiceTicket=" + casServiceTicket + "&").
			append("secretKey=" + secret_key);
			// sha
			String sha_sign = KCode.hexSHA1(sb.toString());
			// via block http api
			String result = HttpKit.get(verification_url + "?casServiceTicket=" + casServiceTicket + "&sign=" + sha_sign);
			// handler result
			JSONObject obj = JSON.parseObject(result);
			if(obj.getInteger("code") != 0) {
				// TGC expire, login again
				if(obj.getInteger("code") == 40011) {
					inv.getController().redirect(redirect_url); return;
				}
				String error = "Oop! CAS error(" + obj.getInteger("code") + "):" + obj.getString("msg");
				log.error(error);
				throw new Error(error);
			} else {
//				getUserByCasServer();
				String data = obj.getString("data");
				JSONObject user = JSON.parseObject(data);
				if(user != null) {
					String uuid = KCode.UUID();
					inv.getController().setCookie(USER_COOKIE_NAME, uuid, -1);
					CacheKit.put(USER_CACHE_NAME, uuid, user);
					// clear paramter casServiceTicket to avoid endless redirection
					if(url.contains("?casServiceTicket=")) {
						url = url.substring(0, url.indexOf("?"));
					} else if(url.contains("&casServiceTicket=")) {
						url = url.substring(0, url.indexOf("&casServiceTicket="));
					} else {
						throw new Error("Oop! Illegal casServiceTicket is black!");
					}
					System.out.println("redirect url: " + url);
					inv.getController().redirect(url); return;
				} else {
					// local cache expire, login again
					inv.getController().redirect(redirect_url); return;
				}
			}
		}
		// judge whether already exists user session
		// session can be stored cache or redis etc.
		
		// via cookie
		String session = inv.getController().getCookie(USER_COOKIE_NAME);
		Object user = CacheKit.get(USER_CACHE_NAME, session);
		// 
		if(user == null) {
			inv.getController().redirect(redirect_url); return;
		} else {
			inv.invoke();
		}
	}
}
