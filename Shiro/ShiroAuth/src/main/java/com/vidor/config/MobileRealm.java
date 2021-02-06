package com.vidor.config;

import com.vidor.bean.User;
import com.vidor.service.UserService;
import com.vidor.utils.MyConstants;
import com.vidor.utils.MyPassWordEncoder;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "mobileRealm")
public class MobileRealm extends AuthenticatingRealm {

    private final Logger logger = LoggerFactory.getLogger(MobileRealm.class);

    @Autowired
    private UserService userService;

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info("entered method doGetAuthenticationInfo;");
        UsernamePasswordToken usertoken = (UsernamePasswordToken)authenticationToken;

        String username = usertoken.getUsername();
        //只需要管用户名查询逻辑。模拟获取数据库中用户密码
        User user = userService.getUserByMobile(username);
        if(null == user){
            return null; //后续会抛出UnknownAccountException
        }else{
            //密码验证逻辑由shiro完成。密码不匹配会抛出密码不匹配异常。
            //密码正确，会把user对象传入SecurityUtils.getSubject();
//            return new SimpleAuthenticationInfo(user,user.getUserPass(),"");//密码不加密
            //这时密码要传加密串，如果密码不符合算法的格式，就会报错。
//            if("manager".equals(user.getUserPass())){
//                user.setUserPass("783b8dfcf9bbb559cdde3cebe4c6dfc6");
//            }
//            user.setUserPass(MyPassWordEncoder.getEncodedPassword(user.getUserPass()));
            return new SimpleAuthenticationInfo(user,MyPassWordEncoder.getEncodedPassword(new String(usertoken.getPassword())),
                    ByteSource.Util.bytes("salt"),"mobileRealm");
        }
    }

    @Override
    public CredentialsMatcher getCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //加密方式
        hashedCredentialsMatcher.setHashAlgorithmName(MyConstants.PASS_ALG);
        //加密次数
        hashedCredentialsMatcher.setHashIterations(MyConstants.PASS_HASH_ITER);
        //存储散列后的密码是否为16进制
        //hashedCredentialsMatcher.isStoredCredentialsHexEncoded();
        return hashedCredentialsMatcher;
    }
}
