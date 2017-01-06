package org.hacker.module.cas.controller;

import static org.hacker.module.common.Assert.checkNotNull;

import org.apache.log4j.Logger;
import org.hacker.core.BaseController;
import org.hacker.core.Dict;
import org.hacker.module.cas.server.CAS;
import org.hacker.module.cas.server.Credentials;
import org.hacker.module.common.KCode;
import org.hacker.module.common.KPro;
import org.hacker.module.common.KWeb;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * 
 * 全局返回代码说明 
 * 40010: 用户浏览器TCG失效(用户还未登陆，或者TGC已经过期) 
 * 40011: CAS服务端session失效(用户还未登陆，或者TGT已经过期)
 * 41010: ST 失效或者错误
 * 41011: ST sha效验失败
 * 500: 代码Exception
 * 
 * @author Mr.J
 *
 */
public class CasController extends BaseController {
	Logger log = Logger.getLogger(CasController.class);
	
	static CAS casServer = null;
	// 登陆是否需要验证码
	static boolean identifyingCode = false;
	// 秘钥效验ST的安全性
	static String secretKey;
	static {
		secretKey = KPro.getInfo("play", "cas.secretkey");
		// init casServer
		String casServerClass = KPro.getInfo("play", "cas.serverClass");
		if(StrKit.isBlank(casServerClass)) {
			casServerClass = "org.hacker.cas.sso.DefaultCasService";
		}
		try {
			casServer = (CAS) Class.forName(casServerClass).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new Error(e);
		}
	}

	public void index() {
		try {
			String callBackUrl = getPara("url");
			
			checkNotNull(callBackUrl, "url");
			
			String TGT = casServer.ticketGrantingService(getRequest(), getResponse(), callBackUrl);
			
			if (StrKit.isBlank(TGT)) {
				Error(40010, "Oop! Unreliable user credentials, redirect cas login service.");
				return;
			}
			redirectCasClientTakeServiceTicket(TGT, callBackUrl);
		} catch (Exception e) {
			Error(500, e.getMessage());
		}
	}

	public void login() {}
	
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
			boolean success = casServer.authenticationService(getRequest(), getResponse(), credentials);
			if(success) 
				OK();
			else 
				Error(500, "Oop! credentials authentication fail.");
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
			// ST 只有一次效验机会
			CacheKit.remove(Dict.CACHE_ST, casServiceTicket);
			if(StrKit.isBlank(TGT)) {
				Error(41010, "Oop! Illegal or Invalid casServiceTicket.");
				return;
			}
			
			StringBuffer sb = new StringBuffer();
			sb.
			append("casServiceTicket=" + casServiceTicket + "&").
			append("secretKey=" + secretKey);
			// 进行SHA加密
			String sha_sign = KCode.hexSHA1(sb.toString());
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

	// 图片验证码
	public void identifyingCode() {
		renderCaptcha();
	}
	
	public void redirectCasClientTakeServiceTicket() {
		String TGT = getCookie(CAS.TGC);
		String callBackUrl = getPara("url");
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
			callBackUrl = KWeb.getURLDecoder(callBackUrl, "UTF-8");
			callBackUrl = callBackUrl + (callBackUrl.contains("?") ? "&" : "?");
			redirect(callBackUrl + "casServiceTicket=" + ST);
		}
	}
	
	private String generateTicket() {
		return KCode.UUID().replace("-", "");
	}
}
