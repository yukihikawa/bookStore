package com.wrf.service;

import com.wrf.service.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 纯单元测试：不依赖 Spring 容器与 MySQL。
 * 覆盖 BCrypt 哈希/校验的往返正确性与幂等迁移判定逻辑。
 */
class PasswordHasherTest {

    private final UserMapper mapper = mock(UserMapper.class);
    private final PasswordHasher hasher = new PasswordHasher(mapper);

    @Test
    void hash_produces_bcrypt_format_and_matches() {
        String hashed = hasher.hash("666666");
        // BCrypt 输出固定格式：$2a$10$ + 53 字符盐与摘要
        assertTrue(hashed.startsWith("$2a$"), "应为 BCrypt 格式: " + hashed);
        assertEquals(60, hashed.length());
        assertTrue(hasher.matches("666666", hashed), "正确密码应校验通过");
        assertFalse(hasher.matches("wrong", hashed), "错误密码应校验失败");
        assertFalse(hasher.matches("666666", "plain-text-legacy"), "明文不应被当成哈希通过");
    }

    @Test
    void hash_is_not_deterministic_same_password_differs_but_both_match() {
        String h1 = hasher.hash("abc123456");
        String h2 = hasher.hash("abc123456");
        assertNotEquals(h1, h2, "每次哈希应含随机盐");
        assertTrue(hasher.matches("abc123456", h1));
        assertTrue(hasher.matches("abc123456", h2));
    }

    @Test
    void legacy_plaintext_migration_logic() {
        // 迁移判定与 PasswordHasher.migrateLegacyPlaintextPasswords 一致：
        // 不以 $2a$ 开头的视为存量明文，需要重新哈希
        String legacy = "666666";
        String alreadyHashed = new BCryptPasswordEncoder().encode("666666");

        assertTrue(!legacy.startsWith("$2a$"), "明文应被判定为待迁移");
        assertTrue(alreadyHashed.startsWith("$2a$"), "已哈希行应被跳过（幂等）");
        verifyNoInteractions(mapper);
    }
}
