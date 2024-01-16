package com.project.springbootneo4j.config;


import com.project.springbootneo4j.core.CoreProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>全局Bean配置类</p>
 *
 * @author Appleyk
 * @version V.0.1.2
 * @blob https://blog.csdn.net/Appleyk
 * @date created on 21:21 2020/3/31
 */
@Configuration
public class ConfigBean {


    @Bean
    public CoreProcessor modelProcess() throws Exception{
        return new CoreProcessor();
    }

}
