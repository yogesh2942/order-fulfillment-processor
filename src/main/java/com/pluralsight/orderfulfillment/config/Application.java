package com.pluralsight.orderfulfillment.config;

import org.springframework.context.annotation.*;

/**
 * Main application configuration for the order fulfillment processor.
 * 
 * @author Michael Hoffman, Pluralsight
 * 
 */
@Configuration
@ComponentScan(basePackages = "com.pluralsight.orderfulfillment")
public class Application {

   @Configuration
   @Profile("standard")
   @PropertySource("classpath:order-fulfillment.properties")
   static class StandardProfile {
      
   }

   @Configuration
   @Profile("test")
   @PropertySource("classpath:order-fulfillment-test.properties")
   static class TestProfile {
      
   }

   
}
