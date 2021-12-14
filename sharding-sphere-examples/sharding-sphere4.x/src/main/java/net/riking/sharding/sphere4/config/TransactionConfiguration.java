package net.riking.sharding.sphere4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class TransactionConfiguration {

    /**
     * 使用 sharding 分布式事务会注入 JtaTransactionManager(JtaAutoConfiguration.AtomikosJtaConfiguration)
     * 导致 DataSourceTransactionManagerAutoConfiguration
     * .DataSourceTransactionManagerConfiguration
     * .transactionManager() 自动装配失效, 所以注册 txManager 排除 JtaTransactionManager
     *
     * @param dataSource ShardingDataSource
     * @return /
     */
    @Bean
    public PlatformTransactionManager txManager(final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 如果不使用 jdbctemplate 就可以不注入。
     *
     * @param dataSource ShardingDataSource
     * @return /
     */
    @Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


}
