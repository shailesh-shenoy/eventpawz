package com.info6250.eventpawz.config;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.cfg.Environment;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Getter
@Setter
@Configuration
@EnableTransactionManagement
@ConfigurationProperties(prefix = "hibernate")
public class HibernateConfig {

    private String url;
    private String user;
    private String driver;
    private String password;
    private String dialect;
    private String showsql;
    private String formatsql;
    private String hbm2ddlauto;
//    private String context;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.info6250.eventpawz.model");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, dialect);
        properties.put(Environment.SHOW_SQL, showsql);
        properties.put(Environment.FORMAT_SQL, formatsql);
        properties.put(Environment.HBM2DDL_AUTO, hbm2ddlauto);

        return properties;
    }

    @Bean
    public HibernateTransactionManager transactionManager(LocalSessionFactoryBean sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory((sessionFactory.getObject()));
        return transactionManager;
    }
}
