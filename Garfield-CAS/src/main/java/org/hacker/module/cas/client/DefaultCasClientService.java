package org.hacker.module.cas.client;

import javax.servlet.http.Cookie;

import org.hacker.module.common.KCode;

import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.ehcache.CacheKit;

public class DefaultCasClientService extends AbstractCasClientService {
	private static final String USER_COOKIE_NAME = "USER_SESSION";
	private static final String USER_CACHE_NAME = "SESSION_USERINFO";
	
	@Override
	public String verificationCASToken(String verification_url, String casServiceTicket, String secret_key) {
		StringBuffer sb = new StringBuffer();
		sb.
		append("casServiceTicket=" + casServiceTicket + "&").
		append("secretKey=" + secret_key);
		// sha
		String sha_sign = KCode.hexSHA1(sb.toString());
		return HttpKit.get(verification_url + "?casServiceTicket=" + casServiceTicket + "&sign=" + sha_sign);
	}

	@Override
	public void setUserCache(Object user) {
		String uuid = KCode.UUID();
		Cookie cookie = new Cookie(USER_COOKIE_NAME, uuid);
		cookie.setMaxAge(-1);
		cookie.setPath("/");
		getResponse().addCookie(cookie);
		CacheKit.put(USER_CACHE_NAME, uuid, user);
	}

	@Override
	public Object getUserCache() {
		String session = null;
		Cookie[] cookies = getRequest().getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(USER_COOKIE_NAME))
					session = cookie != null ? cookie.getValue() : "";
		return CacheKit.get(USER_CACHE_NAME, session);
	}

}
