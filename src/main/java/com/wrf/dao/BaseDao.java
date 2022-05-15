package com.wrf.dao;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * @program: bookStore
 * @description:
 * @author: Rifu Wu
 * @create: 2022-05-13 20:33
 **/
@Component
public abstract class BaseDao {

    static Logger logger;

    @Autowired
    QueryRunner queryRunner;

    static {
        logger = LoggerFactory.getLogger(BaseDao.class);
    }


    //执行一条update语句
    public int update(String sql, Object... args) {
        int result = -1;
        try {
            result = queryRunner.update(sql, args);
        }catch (Exception e){
            logger.error("", e);
        }
        return result;
    }

    //查找并返回一个对象
    public <T> T queryForOne(Class<T> type, String sql, Object... args){
        try{
            return queryRunner.query(sql, new BeanHandler<>(type), args);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return null;
    }

    //查找，并将结果以对象列表的形式返回
    public <T> List<T> queryForList(Class<T> type, String sql, Object... args){
        try{
            return queryRunner.query(sql, new BeanListHandler<>(type), args);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return null;
    }

    public Object queryForSingleValue(String sql, Object... args){
        try{
            return queryRunner.query(sql, new ScalarHandler(), args);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return null;
    }
}