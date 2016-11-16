package org.hacker.cas.sso.client;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hacker.common.WebKit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class CasFilter implements Filter {
	private String login_url, verification_url, secret_key;
	private AbstractCasClientService casClientService;
	
	@Override
	public void destroy() {
		// help CG
		casClientService = null;
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)arg0;
		HttpServletResponse response = (HttpServletResponse)arg1;
		// init casClientService
		casClientService.setRequest(request);
		casClientService.setResponse(response);
		
		// get currut url
		String url = WebKit.getPathAndParamter(request);
		String redirect_url = login_url + "?url=" + WebKit.getURLEncoder(url, "UTF-8");
		// first judge request whether ST request
		String casServiceTicket = request.getParameter("casServiceTicket");
		
		// handler ST request
		if(casServiceTicket != null && !casServiceTicket.equals("")) {
			// via block http api
			String result = casClientService.verificationCASToken(verification_url, casServiceTicket, secret_key);
			// handler result
			JSONObject obj = JSON.parseObject(result);
			if(obj.getInteger("code") != 0) {
				// TGC expire, login again
				if(obj.getInteger("code") == 40011) {
					response.sendRedirect(redirect_url); return;
				}
				String error = "Oop! CAS error(" + obj.getInteger("code") + "):" + obj.getString("msg");
				throw new Error(error);
			} else {
				String data = obj.getString("data");
				JSONObject user = JSON.parseObject(data);
				if(user != null) {
					casClientService.setUserCache(user);
					// clear paramter casServiceTicket to avoid endless redirection
					if(url.contains("?casServiceTicket=")) {
						url = url.substring(0, url.indexOf("?"));
					} else if(url.contains("&casServiceTicket=")) {
						url = url.substring(0, url.indexOf("&casServiceTicket="));
					} else {
						throw new Error("Oop! Illegal casServiceTicket is black!");
					}
					// redirect to user access url, next 
					response.sendRedirect(url); return;
				} else {
					// local cache expire, login again
					response.sendRedirect(redirect_url); return;
				}
			}
		}
		// judge whether already exists user session
		// session can be stored cache or redis etc.
		Object user = casClientService.getUserCache();
		if(user == null) {
			response.sendRedirect(redirect_url); return;
		} else {
			arg2.doFilter(arg0, arg1);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		String 
		casServerHost    = arg0.getInitParameter("casServerHost");
		secret_key 		 = arg0.getInitParameter("secretKey");
		login_url 		 = casServerHost + "/login";
		verification_url = casServerHost + "/serviceTicketAuthentication";
		String serviceClass = arg0.getInitParameter("casClientService");
		
		try {
			casClientService = (AbstractCasClientService) Class.forName(serviceClass).newInstance();
		} catch (Exception e) {
			casClientService = new DefaultCasClientService();
		}
	}

}
