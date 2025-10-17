package com.link.core.web.email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Link
 * @Description 配置方法获取邮箱配置
 * @since 2025/8/18 09:59
 **/

@Component
@ConfigurationProperties(prefix = "email")
public class EmailProperties {

    // 激活的邮箱标识（qq/163）
    private String active;

    // QQ邮箱配置
    private MailConfig qq;

    // 163邮箱配置
    private MailConfig wy163;  // 数字开头的字段名需用下划线转义

    // 内部类：封装单个邮箱的配置（host/username/password）
    public static class MailConfig {
        private String host;
        private String username;
        private String password;

        // getter/setter
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // 根据激活标识，动态返回当前使用的邮箱配置
    public MailConfig getCurrentConfig() {
        switch (active) {
            case "qq":
                return qq;
            case "wy163":
                return wy163;
            default:
                throw new IllegalArgumentException("未知的邮箱类型：" + active);
        }
    }

    // 外部类的getter/setter
    public String getActive() { return active; }
    public void setActive(String active) { this.active = active; }

    public MailConfig getQq() { return qq; }
    public void setQq(MailConfig qq) { this.qq = qq; }

    public MailConfig getWy163() { return wy163; }

    public void setWy163(MailConfig wy163) { this.wy163 = wy163; }

}
