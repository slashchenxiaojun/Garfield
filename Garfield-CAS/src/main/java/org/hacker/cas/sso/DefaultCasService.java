package org.hacker.cas.sso;

import com.alibaba.fastjson.JSONObject;

public class DefaultCasService extends AbstractCasService{

	@Override
	protected Object authenticationUser(Credentials credentials) {
		JSONObject user = new JSONObject();
		user.put("username", credentials.username);
		return user;
	}
	
}
