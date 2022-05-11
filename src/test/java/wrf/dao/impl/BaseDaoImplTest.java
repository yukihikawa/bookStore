package wrf.dao.impl;

import com.wrf.dao.impl.BaseDaoImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.wrf.AppConfig;
import com.wrf.pojo.mapper.UserMapper;


class BaseDaoImplTest {
    //日志
    Log log = LogFactory.getLog(getClass());
    //配置项目
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    BaseDaoImpl td = context.getBean(BaseDaoImpl.class);


    //测试BaseDao.update()
    @Test
    public void daoUpdateTest(){
        String sql = "INSERT INTO t_user(`username`,`password`,`email`) VALUES(?, ?, ?)";
        log.info("result: " + td.update(sql, "admin1", "admin1", "a1dmin@atguigu.com"));
    }

    //测试BaseDao.queryForOne()
    @Test
    public void daoQueryForOneTest(){
        String sql = "SELECT * FROM t_user WHERE id = ?";
        try {
            log.info(td.queryForOne(new UserMapper(), sql, 1).toString());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void daoQueryForListTest(){
        String sql = "SELECT * FROM t_user";
        System.out.println(td.queryForList(new UserMapper(), sql));
    }

    @Test
    public void daoQueryForSingleValue(){
        String sql = "SELECT email FROM t_user WHERE id = ?";
        System.out.println(td.queryForSingleValue(sql, 1));
    }

}