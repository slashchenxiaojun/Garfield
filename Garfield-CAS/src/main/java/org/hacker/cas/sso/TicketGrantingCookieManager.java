package org.hacker.cas.sso;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来管理存放在TGC（cookie）中的TGT，方便 维护TGT的唯一性
 * 
 * 当关闭浏览器时，TGC会失效，而关联全局标识的TGT将不会再被获取
 * 
 * 但是TGT，并没有被内存清除，所以需要一个事件驱动来帮助删除当TGC失效的情况下，删除没有用的TGT
 * */
public class TicketGrantingCookieManager {
	//存放Cookie中value（TGT）的list
	private static List<String> ticketGrantingTicketList = null;
	
	private static TicketGrantingCookieManager instance = new TicketGrantingCookieManager();
	
	private TicketGrantingCookieManager(){}
	
	public static TicketGrantingCookieManager getInstance(){
		return instance;
	}
	
	public void addTicketGrantingTicket(String value){
		if(ticketGrantingTicketList == null){
			synchronized (TicketGrantingCookieManager.class) {
				if(ticketGrantingTicketList == null)
					ticketGrantingTicketList = new ArrayList<String>();
			}
		}
		ticketGrantingTicketList.add(value);
	}
	
	public void removeTicketGrantingTicket(String value){
		ticketGrantingTicketList.remove(value);
	}
}
