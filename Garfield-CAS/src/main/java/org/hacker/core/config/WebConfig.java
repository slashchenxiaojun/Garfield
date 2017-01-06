package org.hacker.core.config;

import org.apache.log4j.Logger;
import org.beetl.ext.jfinal.BeetlRenderFactory;
import org.hacker.aop.handler.GlobalHandler;
import org.hacker.core.Dict;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.ehcache.EhCachePlugin;

public class WebConfig extends JFinalConfig{
	Logger log = Logger.getLogger(WebConfig.class);
	
	@Override
	public void configConstant(Constants me) {
		loadPropertyFile("play.properties");
		me.setDevMode(getPropertyToBoolean(Dict.CONFIG_JFINAL_MODE, false));
		me.setBaseViewPath("WEB-INF/views");
		me.setMainRenderFactory(new BeetlRenderFactory(PathKit.getWebRootPath()));
	}

	@Override
	public void configRoute(Routes me) {
	  MappingRoute.mapping(me);
	}

	@Override
	public void configPlugin(Plugins me) {
		me.add(new EhCachePlugin());
	}

	@Override
	public void configInterceptor(Interceptors me) {}

	@Override
	public void configHandler(Handlers me) {
		me.add(new GlobalHandler());
	}
	
	public static void main(String[] args){
	  JFinal.start("webapp", 8080, "/", 1);
	}
}