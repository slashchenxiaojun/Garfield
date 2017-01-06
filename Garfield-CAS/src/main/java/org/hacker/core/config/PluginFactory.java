package org.hacker.core.config;

import org.hacker.core.Dict;
import org.hacker.core.plugin.SqlMapPlugin;

import com.jfinal.config.Plugins;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.mysql.jdbc.Connection;

public class PluginFactory {
	
	public static void startActiveRecordPlugin() {
		DruidPlugin dp = new DruidPlugin(
		getProperty(Dict.CONFIG_JDBC_URL), 
		getProperty(Dict.CONFIG_JDBC_USERNAME), 
		getProperty(Dict.CONFIG_JDBC_PASSWORD).trim());
		
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
		if(getPropertyToBoolean(Dict.CONFIG_JFINAL_MODE, false)){
			arp.setShowSql(true);
		}
		arp.setTransactionLevel(Connection.TRANSACTION_READ_COMMITTED);
		
		MappingModel.mapping(arp);
		
		dp.start();
		arp.start();
	}

  public static void startActiveRecordPlugin(Plugins me) {
    DruidPlugin dp = new DruidPlugin(
    getProperty(Dict.CONFIG_JDBC_URL), 
    getProperty(Dict.CONFIG_JDBC_USERNAME), 
    getProperty(Dict.CONFIG_JDBC_PASSWORD).trim());
    
    ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
    if(getPropertyToBoolean(Dict.CONFIG_JFINAL_MODE, false)){
      arp.setShowSql(true);
    }
    arp.setTransactionLevel(Connection.TRANSACTION_READ_COMMITTED);
    
    MappingModel.mapping(arp);
    
    me.add(dp);
    me.add(arp);
  }
  
	public static void startSqlPlugin() {
	  new SqlMapPlugin().start();
	}
	
  public static void startSqlPlugin(Plugins me) {
    me.add(new SqlMapPlugin());
  }
  
	/**
	 * 获取play.properties的配置
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		return PropKit.use("play.properties", "UTF-8").get(key);
	}
	
	public static Boolean getPropertyToBoolean(String key, Boolean defaultValue) {
		return PropKit.use("play.properties", "UTF-8").getBoolean(key, defaultValue);
	}
}
