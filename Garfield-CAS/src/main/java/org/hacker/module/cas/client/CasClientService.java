package org.hacker.module.cas.client;

public interface CasClientService {
	/**
	 * 访问CAS服务的效验接口返回接口
	 * 这个接口需要根据casServiceTicket和secret_key使用SHA算法进行加密
	 * 然后在url上加上casServiceTicket与sign参数用于CAS安全性的效验
	 * 
	 * <p> verification_url?casServiceTicket=XXX&sign=XXX
	 * 
	 * @param verification_url CAS效验的url
	 * @param casServiceTicket CAS返回的ST
	 * @param secret_key 客户端与CAS之间效验使用的秘钥
	 * @return
	 */
	public String verificationCASToken(String verification_url, String casServiceTicket, String secret_key);
	
	/**
	 * 用户信息缓存方案，用于保存用户信息在本地
	 */
	public void setUserCache(Object user);
	
	/**
	 * 获取用户本地缓存方案
	 * @return user obj
	 */
	public Object getUserCache();
}
