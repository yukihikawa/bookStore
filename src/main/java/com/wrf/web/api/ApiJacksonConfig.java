package com.wrf.web.api;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @program: bookStore
 * @description: SPA REST 接口的 JSON 序列化配置：
 *   - 使用 classpath 上的 jackson-datatype-jsr310 模块（builder 自动探测注册）
 *   - LocalDateTime 等 Java 8 时间类型序列化为 ISO-8601 字符串（禁用时间戳/数组）
 * @author: Rifu Wu
 **/
@Configuration
public class ApiJacksonConfig {

    @Bean
    public WebMvcConfigurer apiJacksonConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                converters.add(0, new MappingJackson2HttpMessageConverter(
                        Jackson2ObjectMapperBuilder.json()
                                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                .build()));
            }
        };
    }
}
