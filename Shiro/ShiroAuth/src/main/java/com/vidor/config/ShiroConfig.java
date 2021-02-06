package com.vidor.config;

import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class ShiroConfig {

    //1、realm
//    @Bean
//    public Realm myRealm(){
//        return new MyRealm();
//    }
//    @Resource(name = "myRealm")
//    private Realm myRealm;
//    @Resource(name = "mobileRealm")
//    private Realm mobileRealm;

    //2、DefaultWebSecurityManager
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("myRealm") AuthorizingRealm myRealm, @Qualifier("mobileRealm") Realm mobileRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(new MemoryConstrainedCacheManager());
//        securityManager.setRealm(myRealm);
        securityManager.setAuthenticator(getModularRealmAuthenticator());
//        securityManager.setAuthorizer();
        securityManager.setRealms(Arrays.asList(myRealm,mobileRealm));
        securityManager.setRememberMeManager(rememberMeManager());
//        securityManager.setSessionManager(sessionManager());

        return securityManager;
    }

    //3、shiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        /*
        配合Shiro的内置过滤器,参见DefaultFilter
           anon : 无需认证就可以访问
           authc : 必须认证才可以访问 对应@RequiresAuthentication注解
           user : 用户登录且记住我 才可以访问 对应@RequiresUser注解
           perms : 拥有某个资源才可以访问 对应@RequiresPermissions注解
           roles : 拥有某个角色才可以访问  对应@RequiresRoles注解
         */
        Map<String, String> filterChainDefinition = new HashMap<>();
        //配置antMatcher，跟SpringSecurity一样，可以配**,*,?
        filterChainDefinition.put("/mobile/**","perms[mobile]");
//        filterChainDefinition.put("/mobile/**","perms[mobile, salary]");
//        filterChainDefinition.put("/salary/**","perms[salary]"); 使用了注解的方式@RequiresPermissions
        filterChainDefinition.put("/main.html","authc");
        //配置登出过滤器
        filterChainDefinition.put("/logout","logout");

        bean.setFilterChainDefinitionMap(filterChainDefinition);
        bean.setLoginUrl("/index.html");
        bean.setSuccessUrl("/main.html");

        bean.setUnauthorizedUrl("/common/noauth");
        return bean;
    }


    public ModularRealmAuthenticator getModularRealmAuthenticator(){
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return authenticator;
    }

    /**
     * shiro session的管理
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {  //配置默认的sesssion管理器
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(1 * 1000);
        sessionManager.setSessionDAO(sessionDAO());
//        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
//        sessionManager.setSessionListeners(listeners);
        return sessionManager;
    }

    @Bean
    public SessionDAO sessionDAO() {
        return new MemorySessionDAO();//使用默认的MemorySessionDAO
    }

//    @Bean
//    public EhCacheManager ehCacheManager() {
//        EhCacheManager em = new EhCacheManager();
//        em.setCacheManager(cacheManager());
//        return em;
//    }

    @Bean
    public RememberMeManager rememberMeManager (){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        Cookie cookie = cookieRememberMeManager.getCookie();
        cookie.setMaxAge(2592000); // 30天
        return cookieRememberMeManager;
    }
}
