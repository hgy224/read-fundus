package cn.hgy.readfundus.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RefreshTokenInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    //不用做拦截，就是看用户是否登录
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        //获取请求头中的token
        log.info(request.getRequestURI());
        String token = request.getHeader("authorization");
        log.info(token);
        if (StrUtil.isBlank(token)){
            return true;
        }
        //基于token获取redis中的用户
        String key = "login:token:" + token;
        String readId = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isBlank(readId)){
            return true;
        }
        //将查询到的Hash数据转化为UserDTO对象
        UserHolder.saveUser(readId);
        //刷新token有效期
        stringRedisTemplate.expire(key, 30L, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
