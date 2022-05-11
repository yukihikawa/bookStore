package com.wrf.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @program: bookStore
 * @description: BaseDao
 * @author: Rifu Wu
 * @create: 2022-05-10 13:31
 **/

public abstract class BaseDao extends JdbcDaoSupport {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        super.setJdbcTemplate(jdbcTemplate);
    }

    public int update(String sql, Object... args){
        try{
            return jdbcTemplate.update((Connection conn) ->{
                PreparedStatement ps = conn.prepareStatement(sql);
                for (int i = 0; i < args.length; i++) {
                    ps.setObject(i + 1, args[i]);
                }
                return ps;
            });
        } catch (DataAccessException e) {
            return -1;
        }
    }


    /**
     * 查询返回一个 javaBean 的 sql 语句
     *
     * @param rowMapper 返回的对象类型
     * @param sql 执行的 sql 语句
     * @param args sql 对应的参数值
     * @param <T> 返回的类型的泛型
     * @return
     */

    public <T> T queryForOne(RowMapper<T> rowMapper, String sql, Object... args){
        try{
            return jdbcTemplate.queryForObject(sql, args, rowMapper);
        } catch (DataAccessException e) {
            return null;
        }
    }

    /**
     * 查询返回多个 javaBean 的 sql 语句
     *
     * @param rowMapper 返回的对象的rowMapper
     * @param sql 执行的 sql 语句
     * @param args sql 对应的参数值
     * @param <T> 返回的类型的泛型
     * @return
     */
    public <T> List<T> queryForList(RowMapper<T> rowMapper, String sql, Object... args){
        try{
            List<T> result = jdbcTemplate.query(sql, rowMapper, args);
            if(!result.isEmpty())
                return result;
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 执行返回一行一列的 sql 语句
     * @param sql 执行的 sql 语句
     * @param args sql 对应的参数值
     * @return
     */

    public Object queryForSingleValue(String sql, Object... args){
        final Object[] result = {null};
        try{
            jdbcTemplate.query(sql, args, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet resultSet) throws SQLException {
                    result[0] = resultSet.getObject(1);
                }
            });
            return result[0];
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return result[0];
    }



}