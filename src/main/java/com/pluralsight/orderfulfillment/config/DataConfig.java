package com.pluralsight.orderfulfillment.config;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Data configuration for repositories.
 * 
 * @author Michael Hoffman
 * 
 */
@Configuration
@EnableJpaRepositories(basePackages = { "com.pluralsight.orderfulfillment" })
@EnableTransactionManagement
public class DataConfig {

   @Inject
   private Environment environment;

   @Inject
   private DataSource dataSource;

   @Bean
   public EntityManagerFactory entityManagerFactory() {
      final HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
      jpaVendorAdapter.setDatabasePlatform(environment.getProperty("hibernate.dialect"));
      jpaVendorAdapter.setShowSql(false);
      final Map<String, String> jpaProperties = new HashMap<String, String>();
      jpaProperties.put("hibernate.jdbc.batch_size",
            environment.getProperty("hibernate.jdbc.batch_size"));
      jpaProperties.put("hibernate.default_schema",
            environment.getProperty("hibernate.default_schema"));
      LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
      factory.setPackagesToScan("com.pluralsight.orderfulfillment");
      factory.setJpaVendorAdapter(jpaVendorAdapter);
      factory.setDataSource(dataSource);
      factory.setJpaPropertyMap(jpaProperties);
      factory.afterPropertiesSet();
      return factory.getObject();
   }

   @Bean
   public PlatformTransactionManager transactionManager() {
      JpaTransactionManager transactionManager = new JpaTransactionManager();
      transactionManager.setEntityManagerFactory(entityManagerFactory());
      return transactionManager;
   }

   @Bean
   public HibernateExceptionTranslator hibernateExceptionTranslator() {
      return new HibernateExceptionTranslator();
   }

   @Configuration
   @Profile("standard")
   static class StandardProfile {

      @Inject
      private Environment environment;

      @Bean
      public DataSource dataSource() {
         BasicDataSource dataSource = new BasicDataSource();
         dataSource.setDriverClassName(environment.getProperty("db.driver"));
         dataSource.setUrl(environment.getProperty("db.url"));
         dataSource.setUsername(environment.getProperty("db.user"));
         dataSource.setPassword(environment.getProperty("db.password"));
         return dataSource;
      }
   }

   @Configuration
   @Profile("test")
   static class TestProfile {

      @Inject
      private Environment environment;

      @Bean
      public DataSource dataSource() {
         BasicDataSource dataSource = new BasicDataSource();
         dataSource.setDriverClassName(environment.getProperty("db.driver"));
         dataSource.setUrl(environment.getProperty("db.url"));
         return dataSource;
      }

      /**
       * Spring JDBC Template used for querying the Derby database.
       * 
       * @return
       */
      @Bean
      public JdbcTemplate jdbcTemplateDerby() {
         JdbcTemplate jdbc = new JdbcTemplate(dataSource());
         return jdbc;
      }

      /**
       * Derby database bean used for creating and destroying the derby database
       * as part of the Spring container lifecycle. Note that the bean
       * annotation sets initMethod equal to the DerbyDatabaseBean method create
       * and sets destroyMethod to the DerbyDatabaseBean method destroy.
       * 
       * @return
       */
      @Bean(initMethod = "create", destroyMethod = "destroy")
      public DerbyDatabaseBean derbyDatabaseBean() {
         DerbyDatabaseBean derby = new DerbyDatabaseBean();
         derby.setJdbcTemplate(jdbcTemplateDerby());
         return derby;
      }

   }

}
