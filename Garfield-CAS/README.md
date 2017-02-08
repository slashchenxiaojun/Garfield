# Garfield-CAS
> A simple CAS implementation

##runtime environment
 JDK1.7

##get start
1. git clone 'https://github.com/slashchenxiaojun/Garfield.git'

2. cd Garfield/Garfield-CAS

3. mvn package

4. mvn jetty:run (now you have start cas server.)

5. new a j2ee web project like: hello world

6. copy files
 Garfield-CAS/src/main/java/org/hacker/module/cas/client 
 Garfield-CAS/src/main/java/org/hacker/module/common 
 to your web project

7. add some code in your web.xml file
```xml
<filter>
  <filter-name>cas</filter-name>
  <filter-class>org.hacker.module.cas.client.CasFilter</filter-class>
  <init-param>
    <param-name>casServerHost</param-name>
    <param-value>http://localhost:8080/cas</param-value>
  </init-param>
  <init-param>
    <param-name>secretKey</param-name>
    <param-value>123456</param-value>
  </init-param>
</filter>
<filter-mapping>
  <filter-name>cas</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>
```
> note: 'casServerHost' is cas server host
'secretKey' is verification key, the key you can configure in play.properties of cas's project what property is 'cas.secretkey'

8. run your web project and play it.
 