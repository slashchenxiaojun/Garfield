package org.hacker.module.cas.cache;

import org.apache.log4j.Logger;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListenerAdapter;

public class ServiceCacheEventListener extends CacheEventListenerAdapter{
	Logger log = Logger.getLogger(ServiceCacheEventListener.class);
	
	@Override
	public void notifyElementExpired(Ehcache arg0, Element arg1) {
		log.info("Expired Time Ehcache:" + arg0.getName());
		log.info("Expired Time Element:" + arg1.getObjectKey() + ":" + arg1.getObjectValue());
	}
}
