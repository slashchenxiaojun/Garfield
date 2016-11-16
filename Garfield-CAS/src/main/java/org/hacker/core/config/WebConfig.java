package org.hacker.core.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.beetl.ext.jfinal.BeetlRenderFactory;
import org.hacker.aop.handler.GlobalHandler;
import org.hacker.core.Dict;
import org.hacker.core.plugin.RoutePlugin;
import org.hacker.core.plugin.SqlMapPlugin;
import org.hacker.mvc.controller.HomeController;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.mysql.jdbc.Connection;

public class WebConfig extends JFinalConfig{
	Logger log = Logger.getLogger(WebConfig.class);
	
	@Override
	public void configConstant(Constants me) {
		loadPropertyFile("play.properties");
		me.setDevMode(getPropertyToBoolean(Dict.CONFIG_JFINAL_MODE, false));
		System.out.println("root path: " + PathKit.getWebRootPath());
		System.out.println("web path: " + PathKit.getWebRootPath());
		me.setBaseViewPath("WEB-INF/views");
		me.setMainRenderFactory(new BeetlRenderFactory(PathKit.getWebRootPath()));
	}

	@Override
	public void configRoute(Routes me) {
		new RoutePlugin(me).start();
		me.add("/", HomeController.class);
	}

	@Override
	public void configPlugin(Plugins me) {
		DruidPlugin dp = new DruidPlugin(
		getProperty(Dict.CONFIG_JDBC_URL), 
		getProperty(Dict.CONFIG_JDBC_USERNAME), 
		getProperty(Dict.CONFIG_JDBC_PASSWORD).trim(), 
		null, "stat,wall");
		
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
		if(getPropertyToBoolean(Dict.CONFIG_JFINAL_MODE, false)){
			arp.setShowSql(true);
		}
		arp.setTransactionLevel(Connection.TRANSACTION_READ_COMMITTED);
		
//		me.add(dp);
//		me.add(arp);
		
		Map<String, ActiveRecordPlugin> arpMap = new HashMap<String, ActiveRecordPlugin>();
		arpMap.put(Dict.DB_DATASOURCE_MAIN, arp);
//		new ModelPlugin(arpMap).start();
		
		me.add(new EhCachePlugin());
		me.add(new SqlMapPlugin());
	}

	@Override
	public void configInterceptor(Interceptors me) {
	
	}

	@Override
	public void configHandler(Handlers me) {
		me.add(new GlobalHandler());
//		me.add(new DruidStatViewHandler("/druid"));
	}
	
}