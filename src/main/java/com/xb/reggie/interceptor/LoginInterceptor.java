//package com.xb.reggie.interceptor;
//
//import com.xb.reggie.common.BaseContext;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//@Slf4j
//public class LoginInterceptor implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        Boolean flag=false;
//        if(request.getSession().getAttribute("id")==null){
//            response.sendRedirect(request.getContextPath()+"/backend/page/login/login.html");
//            flag=true;
//        }
//        if(request.getSession().getAttribute("user")==null&&flag){
//            response.sendRedirect(request.getContextPath()+"/front/page/login.html");
//            return false;
//        }
//        BaseContext.setCurrentId(Long.parseLong(request.getSession().getAttribute("id").toString()));
//        return true;
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//
//    }
//}
