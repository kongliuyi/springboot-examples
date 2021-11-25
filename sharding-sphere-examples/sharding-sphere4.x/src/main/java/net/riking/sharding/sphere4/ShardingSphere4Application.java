package net.riking.sharding.sphere4;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author kongLiuYi
 * @since 2021-11-25
 */
@SpringBootApplication
@MapperScan("net.riking.sharding.sphere4.mapper")
public class ShardingSphere4Application {
    public static void main(String[] args) {
        SpringApplication.run(ShardingSphere4Application.class, args);
    }
}
