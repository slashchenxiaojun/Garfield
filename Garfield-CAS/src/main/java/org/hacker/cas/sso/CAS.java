package org.hacker.cas.sso;

/**
 * CAS是一套认证体系
 * 
 * 	user						app                  	  	cas service
 *  |  							|							|
 *  | 1.user visit app service	| 							|
 *  | ----------------------->	| ----						|
 *  |							|	 | 2.check session		|
 *  |							| <--- 						|
 *  | 3.redirect cas service								|
 *  | --------------------------------------------------->	| ----
 *  |							|							|    | 4.check session
 *  |							|							| <---
 *  | 5.redirect to login.html								|
 *  | <---------------------------------------------------	|
 *  | 6.authentication user info							|
 *  | --------------------------------------------------->  |
 *  |							| 7.cas server ticket		|
 *  |							| <-----------------------	|
 *  |							| 8.visit cas ticket check	|
 *  |							| server					|
 *  |							| ----------------------->  |
 *  |							| 9.result user info and c	|
 *  |							| reate cookie				|
 *  | 10.login success			| <-----------------------	|
 *  | <-----------------------	|							|
 *  |							|							|
 *  CAS登录过程简单解释
 *  1.用户访问app-A
 *  2.app-A检查cookie，如果存在直接到步骤10否则进入步骤3
 *  3.用户重定向访问cas的认证服务
 *  4.cas server检查session，如果存在直接到步骤7否则进入步骤5
 *  5.跳转到登录页面
 *  6.用户输入用户名和密码
 *  7.认证用户身份成功返回给app-A ticket
 *  8.app-A访问cas的检查ticket server
 *  9.验证成功返回用户信息并且app-A创建TGC
 *  10.认证成功
 *  
 *  Ticket Granting ticket(TGT): 存放在cas server的session
 *  Ticket Granting cookie(TGC): 存放在客户端的cookie
 *  Service Ticket(ST): cas验证客户端的随机字符串(UUID)
 *  
 * @author Mr.J
 *
 */
public interface CAS {
	public final static String TGC = "Ticket_Granting_Cookie";
	/**
	 * AS服务需要实现认证用户credentials，并且发放TGT票据给cas server
	 * @param credentials 用户认证对象
	 */
	public Object authenticationService(Credentials credentials);
	
	/**
	 * TGS服务需要实现获取TGT票据，发放ST票据
	 */
	public void ticketGrantingService(String callBackUrl);
	
}
