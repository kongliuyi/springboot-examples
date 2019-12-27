package net.riking.mybatis.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@MapperScan("net.riking.mybatis.mapper")
public class MyBatisConfig {

/*
    @Bean
    public DataSource dataSource(){
        DruidDataSource dataSource=   new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("woaini520");
        dataSource.setUrl("jdbc:mysql://localhost:3306/oauth?characterEncoding=UTF-8");
        dataSource.setDriverClassName("com.mysql.cjdbc.driver");
        return dataSource;
    }

    @Bean
    public SqlSessionFactory getSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        return sqlSessionFactoryBean.getObject();
    }
*/

}
