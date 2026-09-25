package com.wrf.service;

import com.wrf.entity.User;
import com.wrf.service.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @program: bookStore
 * @description: 密码哈希工具（BCrypt，含随机盐）。
 *               存量明文密码在应用启动时自动迁移为哈希（见 migrateLegacyPlaintextPasswords），
 *               迁移失败不阻断启动，仅记录 ERROR。
 * @author: Rifu Wu
 **/
@Component
@Slf4j
@AllArgsConstructor
public class PasswordHasher {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private final UserMapper userMapper;

    /** 注册时调用：明文 → BCrypt 哈希 */
    public String hash(String rawPassword) {
        return ENCODER.encode(rawPassword);
    }

    /** 登录校验：明文与库中哈希比对（库中已是哈希） */
    public boolean matches(String rawPassword, String hashedInDb) {
        return ENCODER.matches(rawPassword, hashedInDb);
    }

    /**
     * 启动时迁移存量明文密码：凡是不以 $2a$ 开头的 password 字段
     * （老库中的明文）都重新哈希写回。BCrypt 哈希自带随机盐，
     * 已哈希的行（$2a$ 开头）跳过，保证幂等。
     */
    @PostConstruct
    void migrateLegacyPlaintextPasswords() {
        try {
            List<User> users = userMapper.queryAllUsers();
            int migrated = 0;
            for (User user : users) {
                if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
                    userMapper.updatePassword(user.getId(), ENCODER.encode(user.getPassword()));
                    migrated++;
                    log.info("migrated legacy plaintext password for user [{}]", user.getUsername());
                }
            }
            if (migrated > 0) {
                log.info("legacy password migration done: {}/{} users", migrated, users.size());
            }
        } catch (Exception e) {
            // 迁移失败不阻断启动（例如表结构变化），仅告警
            log.error("legacy password migration failed", e);
        }
    }
}
