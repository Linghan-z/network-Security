package com.zlhhh.networksecurity.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zlhhh.networksecurity.entity.User;
import com.zlhhh.networksecurity.service.IUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class TokenUtils {

    // 静态对象用于静态方法public static User getCurrentUser()
    private static IUserService staticUserService;

    // tokenUtils注册为springboot的一个bean，
    @Resource
    private IUserService userService;

    //在项目启动的时候@PostConstruct，将userService对象赋值给静态对象staticUserService
    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }


    /**
     * 生成Token
     *
     * @return
     */
    public static String genToken(String userId, String sign) {
        // 将用户的id保存到token里面，作为载荷
        return JWT.create().withAudience(userId)  // 将用户的id保存到token里面，作为载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))  // 2h 后 token 过期
                .sign(Algorithm.HMAC256(sign));  // 以password作为token的密钥
    }

    /**
     * 通过静态方法获取当前登陆的用户信息
     *
     * @return User user
     */
    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();  // 获取当前请求的request
            String token = request.getHeader("token");  // 获取到request之后拿token
            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
