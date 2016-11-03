package org.hacker.mvc.controller;

import static org.hacker.common.Assert.*;

import org.apache.log4j.Logger;
import org.hacker.cas.sso.CAS;
import org.hacker.cas.sso.Credentials;
import org.hacker.common.CodeKit;
import org.hacker.core.BaseController;
import org.hacker.core.Dict;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * 
 * 全局返回代码说明 
 * 40010: 用户浏览器TCG失效(用户还未登陆，或者TCG已经过期) 
 * 40011: CAS服务端session失效(用户还未登陆，或者TCT已经过期)
 * 41010: ST 失效或者错误
 * 41011: ST sha效验失败
 * 500: 代码Exception
 * 
 * @author Mr.J
 *
 */
public class CasController extends BaseController implements CAS {
	Logger log = Logger.getLogger(CasController.class);
	private final static String TGC = "Ticket_Granting_Cookie";
	
	// 登陆是否需要验证码
	static boolean identifyingCode = false;
	// 秘钥效验ST的安全性
	static String secretKey;
	static {
		
	}
	
	public void index() {
		try {
			String callBackUrl = getPara("url");
			checkNotNull(callBackUrl, "url");
			
			ticketGrantingService(callBackUrl);
		} catch (Exception e) {
			Error(500, e.getMessage());
		}
	}

	public void credentialsAuthentication() {
		try {
			String 
			url 	 = getPara("url"),
			username = getPara("username"),
			password = getPara("password");
			checkNotNull(url, "url");
			checkNotNull(username, "username");
			checkNotNull(password, "password");
			// 这里可以设置配置文件是否支持随机码
			Credentials credentials = new Credentials(username, password);
			Object user = authenticationService(credentials);
			if(user != null) {
				String TGT = generateTicket();
				CacheKit.put(Dict.CACHE_TGT, TGT, user);
				setCookie(TGC, TGT, -1);
				redirectCasClientTakeServiceTicket(TGT, url);
			} else {
				Error(500, "Oop! credentials authentication fail.");
			}
		} catch (Exception e) {
			Error(500, e.getMessage());
		}
	}
	
	// ST 效验
	public void serviceTicketAuthentication() {
		try {
			String 
			sign 		  	 = getPara("sign"),
			casServiceTicket = getPara("casServiceTicket");
			checkNotNull(sign, "sign");
			checkNotNull(casServiceTicket, "casServiceTicket");
			String TGT = CacheKit.get(Dict.CACHE_ST, casServiceTicket);
			if(StrKit.isBlank(TGT)) {
				Error(41010, "Oop! Illegal or Invalid casServiceTicket.");
				return;
			}
			StringBuffer sb = new StringBuffer();
			sb.
			append("casServiceTicket=" + casServiceTicket + "&").
			append("secretKey=" + secretKey);
			// 进行SHA加密
			String sha_sign = CodeKit.hexSHA1(sb.toString());
			if(sign.equals(sha_sign)) {
				Object user = CacheKit.get(Dict.CACHE_TGT, TGT);
				if(user == null) {
					Error(40011, "Oop! TGT is null, authentication credentials frist.");
				} else {
					renderStatus(0, "ok", user);
				}
			} else {
				Error(41011, "Oop! SHA sign fail.");
			}
		} catch (Exception e) {
			Error(500, e.getMessage());
		}
	}

	public void logout() {
		
	}

	// 图片验证码
	public void identifyingCode() {
		renderCaptcha();
	}

	@Override
	public Object authenticationService(Credentials credentials) {
		// 认证用户身份
//		if() {
//			
//		}
		return null;
	}

	@Override
	public void ticketGrantingService(String callBackUrl) {
		// 获取客户浏览器的TGT
		String TGT = getCookie(TGC);
		if (StrKit.isBlank(TGT)) {
			Error(40010, "Oop! Unreliable user credentials, redirect cas login service.");
			return;
		}
		redirectCasClientTakeServiceTicket(TGT, callBackUrl);
	}
	
	private void redirectCasClientTakeServiceTicket(String TGT, String callBackUrl) {
		Object user = CacheKit.get(Dict.CACHE_TGT, TGT);
		// 如果TGT为null跳转到login.html
		if (user == null) {
			Error(40011, "Oop! TGT is null, authentication credentials frist.");
		} else {
			// 重定向到app端并带上ST票据
			String ST = generateTicket();
			CacheKit.put(Dict.CACHE_ST, ST, TGT);
			redirect(callBackUrl + "?casServiceTicket=" + ST);
		}
	}
	
	private String generateTicket() {
		return CodeKit.UUID().replace("-", "");
	}
}
