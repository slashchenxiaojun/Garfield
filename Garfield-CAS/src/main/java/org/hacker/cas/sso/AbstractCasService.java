package org.hacker.cas.sso;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hacker.common.CodeKit;
import org.hacker.core.Dict;

import com.jfinal.plugin.ehcache.CacheKit;

public class AbstractCasService implements CAS {
	
	@Override
	public boolean authenticationService(HttpServletRequest request, HttpServletResponse response, Credentials credentials) {
		Object user = authenticationUser(credentials);
		if(user != null) {
			String TGT = generateTicket();
			CacheKit.put(Dict.CACHE_TGT, TGT, user);
			Cookie cookie = new Cookie(TGC, TGT);
			cookie.setMaxAge(-1);
			cookie.setPath("/");
			response.addCookie(cookie);
			return true;
		}
		return false;
	}
	
	@Override
	public String ticketGrantingService(HttpServletRequest request, HttpServletResponse response, String callBackUrl) {
		// 获取客户浏览器的TGT
		String TGT = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(TGC))
					TGT = cookie != null ? cookie.getValue() : "";
		return TGT;
	}
	
	protected Object authenticationUser(Credentials credentials) {
		throw new UnsupportedOperationException();
	}
	
	protected String generateTicket() {
		return CodeKit.UUID().replace("-", "");
	}
}
