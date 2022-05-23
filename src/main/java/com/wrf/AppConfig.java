package com.wrf;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;

/**
 * @program: bookStore
 * @description: 配置类
 * @author: Rifu Wu
 * @create: 2022-05-10 13:10
 **/
@Configuration
@ComponentScan
@MapperScan("com.wrf.service.mapper")
@EnableTransactionManagement
@EnableWebMvc
public class AppConfig {

    //ORM相关
    //HIkari连接池的datasource
    @Bean
    DataSource createDataSource() {
        String path = getClass().getClassLoader().getResource("hikari.properties").getPath();
        HikariConfig config = new HikariConfig(path);
        return new HikariDataSource(config);
    }

    //MyBatis
    @Bean
    SqlSessionFactoryBean createSqlSessionFactoryBean(@Autowired DataSource dataSource) {
        var sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    //事务管理
    @Bean
    PlatformTransactionManager createTxManger(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    //SpringMVC相关


}