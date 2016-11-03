package org.hacker.cas.sso;

/**
 * 用户凭证
 * 
 * @author  Mr.J.
 * 
 * @since   2015.07.09
 * **/
public class Credentials {
    public String username;
    public String password;
    public Credentials(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }
}
