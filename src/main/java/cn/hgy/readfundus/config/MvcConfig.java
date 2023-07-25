package cn.hgy.readfundus.config;

import cn.hgy.readfundus.service.IDoctorReadService;
import cn.hgy.readfundus.utils.LoginInterceptor;
import cn.hgy.readfundus.utils.RefreshTokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IDoctorReadService doctorReadService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //token刷新拦截器
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).order(0);
        //登录拦截器
        registry.addInterceptor(new LoginInterceptor(doctorReadService))
                .addPathPatterns(
                        "/outcome/info/**",
                        "/outcome/infoLabel/**"
                        )
                .order(0);
    }
}
