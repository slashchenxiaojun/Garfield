package org.hacker.module.cas.server;

import com.alibaba.fastjson.JSONObject;

public class DefaultCasService extends AbstractCasService{

	@Override
	protected Object authenticationUser(Credentials credentials) {
		/**
		 * 这里可以覆盖API完成对用户的获取
		 * Demo:
		 * 
		 * Object user = User.dao.find("select * from user where username = ? and password = ?")
		 * if(user == null) return null;
		 * if(user.password != md5(credentials.password)) return null;
		 * 
		 * JSONObject obj = = new JSONObject();
		 * user.put("id", user.id);
		 * user.put("username", user.username);
		 * ...
		 * 
		 * return obj;
		 */
		JSONObject user = new JSONObject();
		user.put("username", credentials.username);
		return user;
	}
	
}
