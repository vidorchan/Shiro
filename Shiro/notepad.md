# BasicAuth
1.1 定义拦截器：

HandlerInterceptorAdapter

    preHandle
    
1.2注册拦截器：

WebMvcConfigurer

    addInterceptors：
        InterceptorRegistry addInterceptor(HandlerInterceptor).addPathPatterns("/**").order(int order);
    addViewControllers: 可配置默认页面
        ViewControllerRegistry addViewController(urlPath).setViewName("redirect:/页面.html");

# Shiro
1. 加入依赖
```xml
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring-boot-starter</artifactId>
     <version>1.6.0</version>
</dependency>
```
2.配置
2.1 创建Realm
```text
AuthorizingRealm鉴权 -- 这个抽象类继承了AuthenticatingRealm认证 
    AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection)
        获取当前用户：
            Subject subject = SecurityUtils.getSubject();
            User currentUser = (User)subject.getPrincipal();
        将当前的用户鉴权信息放入到AuthorizationInfo：
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.addRoles(currentUser.getUserRoles());
            info.addStringPermissions(currentUser.getUserPerms());
    AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
        从authenticationToken中取出username，然后获取user：
            UsernamePasswordToken usertoken = (UsernamePasswordToken)authenticationToken;
            String username = usertoken.getUsername();
        shiro会完成密码匹配的校验工作，校验成功则放到Subject，不成功则抛出异常：
            SimpleAuthenticationInfo(Object principal, Object hashedCredentials, ByteSource credentialsSalt, String realmName)
                hashedCredentials:
                    SimpleHash(String algorithmName, Object source, Object salt, int hashIterations)
                    toString()                   
    ---加密配置
    CredentialsMatcher getCredentialsMatcher()
        HashedCredentialsMatcher
            setHashAlgorithmName("MD5") 加密方式
            setHashIterations 加密次数       
```
2.2 Shiro配置
```text
1. Realm 在上一个文件中已经放到spring容器中；
2.DefaultWebSecurityManager
DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("") AuthorizingRealm realm1, @Qualifier("") Realm realm2...)
    setAuthenticator // 设置多种认证策略
        ModularRealmAuthenticator：
           setAuthenticationStrategy：AtLeastOneSuccessfulStrategy
    setCacheManager // 设置缓存管理 EhCache MemoryConstrainedCacheManager
    setRememberMeManager // 设置RememberMe CookieRememberMeManager
    setRealms // 设置资源
    setSessionManager // session管理 DefaultWebSessionManager
3.ShiroFilterFactoryBean
ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager)
    setFilterChainDefinitionMap // 设置过滤链 KEY:资源 VALUE: 权限
        @RequiresPermissions 可以使用注解替代权限的配置
        //配置登出过滤器
        filterChainDefinition.put("/logout","logout");
    setLoginUrl // 指定登录的url
    setSuccessUrl // 指定登录成功跳转的url
    setUnauthorizedUrl // 指定未授权跳转的url
```

-----------------记住我------------------------------
```java
setRememberMeManager CookieRememberMeManager
token.setRememberMe(true);
```

默认：1年

对象需要实现序列化接口Serializable

获取checkbox是否选中：.is(":checked)

点击退出，还是会清理掉cookie