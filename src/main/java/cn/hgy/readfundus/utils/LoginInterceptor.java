package cn.hgy.readfundus.utils;

import cn.hgy.readfundus.entity.DoctorRead;
import cn.hgy.readfundus.service.IDoctorReadService;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    private IDoctorReadService doctorReadService;


    public LoginInterceptor(IDoctorReadService doctorReadService) {
        this.doctorReadService = doctorReadService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截: {}", request.getRequestURI());
        String[] split = request.getRequestURI().split("/");

        Integer readId = Integer.parseInt(split[split.length-1]);
        DoctorRead doctor = doctorReadService.getById(readId);
        if (StrUtil.isBlank(doctor.getPassword())){
            return true;
        }
        if (!StrUtil.isBlank(doctor.getPassword()) && UserHolder.getUser() == null || !readId.toString().equals(UserHolder.getUser())){
            response.setStatus(401);
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
