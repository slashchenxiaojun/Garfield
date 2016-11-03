package org.hacker.core;

import com.jfinal.core.Controller;

/**
 * BaseController 只提供基础方法
 * 
 * @version 1.0
 * @author Mr.J.(slashchenxiaojun@sina.com)
 * 
 * @date 2015-05-18
 * */
public class BaseController extends Controller {
	protected void OK() {
		renderStatus(0, "ok", null);
	}
	
	protected void Error(Integer code, String msg) {
		renderStatus(code, msg, null);
	}
	
	protected void renderStatus(Integer code, String msg, Object data) {
		setAttr("code", code);
		setAttr("msg", msg);
		setAttr("data", data);
		renderJson(new String[] {"code", "msg", "data"});
	}
}
