package org.hacker.module.cas.client;

import org.hacker.module.common.KCode;
import org.hacker.module.common.KWeb;

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
		KWeb.setCookie(getResponse(), USER_COOKIE_NAME, uuid);
		CacheKit.put(USER_CACHE_NAME, uuid, user);
	}

	@Override
	public Object getUserCache() {
		String session = KWeb.getCookie(getRequest(), USER_COOKIE_NAME, "");
		return CacheKit.get(USER_CACHE_NAME, session);
	}

}
