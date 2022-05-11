package wrf.dao.impl;

import com.wrf.dao.impl.UserDaoImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.wrf.AppConfig;
import com.wrf.dao.UserDao;
import com.wrf.pojo.User;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoImplTest {

    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    UserDao userDao = context.getBean(UserDaoImpl.class);

    @Test
    void queryUserByUsername() {
        if(userDao.queryUserByUsername("admin") == null){
            System.out.println("可用！");
        }
        else
            System.out.println("已存在！");
    }

    @Test
    void queryUserByUsernameAndPassword() {
        if(userDao.queryUserByUsernameAndPassword("admin", "admin") == null)
            System.out.println("失败！");
        else
            System.out.println("成功！");
    }

    @Test
    void saveUser() {
        System.out.println( userDao.saveUser(new User(null,"wzg168", "123456", "wzg168@qq.com")) );
    }
}