package org.hacker.cas.sso;

/**
 * Browser 是用于保存 app-url 和  browserAndApplicationSessionId
 * 配合SSO登出的POJO
 * 
 * @author  Mr.J.
 * 
 * @since   2015.07.11
 * */
public class Browser {
    String applicationUrl;
    String browserAndApplicationSessionId;
    public Browser(String applicationUrl, String browserAndApplicationSessionId) {
        super();
        this.applicationUrl = applicationUrl;
        this.browserAndApplicationSessionId = browserAndApplicationSessionId;
    }
    public String getApplicationUrl() {
        return applicationUrl;
    }
    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }
    public String getBrowserAndApplicationSessionId() {
        return browserAndApplicationSessionId;
    }
    public void setBrowserAndApplicationSessionId(
            String browserAndApplicationSessionId) {
        this.browserAndApplicationSessionId = browserAndApplicationSessionId;
    }
    
    /** 
     * 重写hashcode 方法，返回的hashCode 不一样才认定为不同的对象 
     */
    @Override
    public int hashCode() {
        return applicationUrl.hashCode() + browserAndApplicationSessionId.hashCode();
    }
    
    /** 
     * 如果对象类型是Browser的话 则返回true 去比较hashCode值 
     */  
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;  
        if(this == obj) return true;
        if(obj instanceof Browser){
            Browser b = (Browser)obj;  
            if(this.applicationUrl.equals(b.applicationUrl) &&
                    this.browserAndApplicationSessionId.equals(browserAndApplicationSessionId))
                return true;
        }
        return false;
    }
}
