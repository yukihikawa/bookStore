package com.wrf;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @program: bookStore
 * @description: 配置类
 * @author: Rifu Wu
 * @create: 2022-05-10 13:10
 **/
@Configuration
@ComponentScan

@EnableTransactionManagement
@PropertySource(value = "classpath:jdbc.properties")

public class AppConfig {
        //数据库访问==============================================
    //读取配置文件
    @Value("${jdbc.url}")
    String jdbcUrl;

    @Value("${jdbc.username}")
    String jdbcUsername;

    @Value("${jdbc.password}")
    String jdbcPassword;

    //DataSource
    @Bean
    DataSource createDataSource(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUsername);
        config.setPassword(jdbcPassword);
        config.addDataSourceProperty("autoCommit", "true");
        config.addDataSourceProperty("connectionTimeout", "5");
        config.addDataSourceProperty("idleTimeout", "60");
        return new HikariDataSource(config);
    }

    /*
    @Bean
    SqlSessionFactoryBean createSessionFactory(@Autowired DataSource dataSource){
        var sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }
    */

    @Bean
    QueryRunner createQueryRunner(@Autowired DataSource dataSource){
        return new QueryRunner(dataSource);
    }

    //JdbcTemplate
    @Bean
    JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    //事务管理器
    @Bean
    PlatformTransactionManager createTxManger(@Autowired DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
    //数据库 结束=========================================


    //MVC相关 ==============================================
    /*@Bean
    WebMvcConfigurer createWebMvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/static/**").addResourceLocations("/static/");
            }
        };
    }*/

    //使用模板引擎，此处使用Pebble
   /* @Bean
    ViewResolver createViewResolver(@Autowired ServletContext servletContext){
        PebbleEngine engine = new PebbleEngine.Builder().autoEscaping(true)
                .cacheActive(false)
                .loader(new ServletLoader(servletContext))
                .extension(new SpringExtension())
                .build();
        //使用Pebble引擎
        PebbleViewResolver viewResolver = new PebbleViewResolver();
        //指定模板文件存放在/WEB-INF/templates/目录下。
        viewResolver.setPrefix("/WEB-INF/templates/");
        viewResolver.setSuffix("");
        viewResolver.setPebbleEngine(engine);
        return viewResolver;
    }*/

    //MCV相关 结束===============================================


    //Log支持
    /*@Bean
    Logger createLogger(){
        return LoggerFactory.getLogger(AppConfig.class);
    }*/

}