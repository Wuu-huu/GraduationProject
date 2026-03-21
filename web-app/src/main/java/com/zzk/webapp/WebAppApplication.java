package com.zzk.webapp;

import com.zzk.common.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 统一启动模块，聚合所有业务模块并提供单体应用入口。
 */
@SpringBootApplication(scanBasePackages = "com.zzk")
@EnableTransactionManagement
@EnableConfigurationProperties(JwtProperties.class)
public class WebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebAppApplication.class, args);
    }
}
