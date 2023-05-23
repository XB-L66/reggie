package com.xb.reggie.config;

import com.alibaba.fastjson.support.spring.messaging.MappingFastJsonMessageConverter;
import com.xb.reggie.common.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
@Configuration
public class MyConfig implements WebMvcConfigurer {
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        List<String> list=new ArrayList<>();
//        list.add("/backend/api/**");
//        list.add("/backend/images/**");
//        list.add("/backend/js/**");
//        list.add("/backend/plugins/**");
//        list.add("/backend/styles/**");
//        list.add("/backend/page/login/login.html");
//        list.add("/employee/login");
//        list.add("/user/sendMsg");
//        list.add("/user/login");
//        list.add("/front/**");
//        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns(list);
//    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter=new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0,messageConverter);
        WebMvcConfigurer.super.extendMessageConverters(converters);
    }
}
