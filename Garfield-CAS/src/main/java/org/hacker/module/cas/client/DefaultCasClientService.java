package org.hacker.module.cas.client;

import java.util.HashMap;
import java.util.Map;

import org.hacker.module.common.KCode;
import org.hacker.module.common.KWeb;

import com.jfinal.kit.HttpKit;

public class DefaultCasClientService extends AbstractCasClientService {
  private static final String USER_COOKIE_NAME = "USER_SESSION";
  @SuppressWarnings("unused")
  private static final String USER_CACHE_NAME = "SESSION_USERINFO";
  private static final Map<String, Object> cache = new HashMap<>();
  
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
    cache.put(uuid, user);
  }

  @Override
  public Object getUserCache() {
    String session = KWeb.getCookie(getRequest(), USER_COOKIE_NAME, "");
    return cache.get(session);
  }

}
