package com.chengxi.p2p.config;

import com.chengxi.p2p.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
@Configuration
public class JspWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    private UserInterceptor userInterceptor;

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        // 处理所有请求
        viewResolver.setPrefix("/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setViewClass(JstlView.class);
        return viewResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                // addPathPatterns 用于添加拦截规则
                .addPathPatterns("/loan/**")
                // excludePathPatterns 用于排除拦截
                .excludePathPatterns("/loan/loan")
                // 首页
                .excludePathPatterns("/loan/loanInfo")
                // 查询平台关键数据
                .excludePathPatterns("/loan/loadStat")
                // 检查验证码
                .excludePathPatterns("/loan/checkCaptcha")
                // 检查手机号码
                .excludePathPatterns("/loan/checkPhone")
                // 登录
                .excludePathPatterns("/loan/login")
                // 用户注册
                .excludePathPatterns("/loan/register");
        super.addInterceptors(registry);
    }

}
