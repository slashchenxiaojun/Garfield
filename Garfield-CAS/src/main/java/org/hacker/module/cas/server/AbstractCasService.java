package org.hacker.module.cas.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hacker.core.Dict;
import org.hacker.module.common.KCode;
import org.hacker.module.common.KWeb;

import com.jfinal.plugin.ehcache.CacheKit;

public class AbstractCasService implements CAS {
	
	@Override
	public boolean authenticationService(HttpServletRequest request, HttpServletResponse response, Credentials credentials) {
		Object user = authenticationUser(credentials);
		if(user != null) {
			String TGT = generateTicket();
			CacheKit.put(Dict.CACHE_TGT, TGT, user);
			KWeb.setCookie(response, TGC, TGT);
			return true;
		}
		return false;
	}
	
	@Override
	public String ticketGrantingService(HttpServletRequest request, HttpServletResponse response, String callBackUrl) {
		// 获取客户浏览器的TGT
		String TGT = KWeb.getCookie(request, TGC, "");
		return TGT;
	}
	
	/**
	 * 认证用户并返回用户对象，如果认证失败返回null
	 * @param credentials
	 * @return
	 */
	protected Object authenticationUser(Credentials credentials) {
		throw new UnsupportedOperationException();
	}
	
	protected String generateTicket() {
		return KCode.UUID().replace("-", "");
	}
}
