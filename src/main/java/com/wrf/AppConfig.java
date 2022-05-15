package com.wrf;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.InputStream;

/**
 * @program: bookStore
 * @description: 配置类
 * @author: Rifu Wu
 * @create: 2022-05-10 13:10
 **/
@Configuration
@ComponentScan
@EnableTransactionManagement
public class AppConfig {
    @Bean
    DataSource createDataSource() {
        String path = getClass().getClassLoader().getResource("hikari.properties").getPath();
        HikariConfig config = new HikariConfig(path);

        return new HikariDataSource(config);
    }

    @Bean
    QueryRunner createQueryRunner(@Autowired DataSource dataSource) {
        return new QueryRunner(dataSource);
    }

    @Bean
    PlatformTransactionManager createTxManger(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}