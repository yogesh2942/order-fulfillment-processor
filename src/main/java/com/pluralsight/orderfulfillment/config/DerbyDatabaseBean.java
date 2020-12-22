package com.pluralsight.orderfulfillment.config;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Bean for creating and destroying the orders database inside Apache Derby.
 * 
 * @author Michael Hoffman, Pluralsight
 * 
 */
public class DerbyDatabaseBean {

   private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

   /**
    * Called by Spring bean initialization. Creates the schema and database
    * table structure for the orders database.
    * 
    * @throws Exception
    */
   public void create() throws Exception {

      try {
         jdbcTemplate.execute("drop table orders.orderItem");
         jdbcTemplate.execute("drop table orders.\"order\"");
         jdbcTemplate.execute("drop table orders.catalogitem");
         jdbcTemplate.execute("drop table orders.customer");
         jdbcTemplate.execute("drop schema orders");
      } catch (Throwable e) {
      }

      jdbcTemplate.execute("CREATE SCHEMA orders");
      jdbcTemplate
            .execute("create table orders.customer (id bigint not null, firstName varchar(200) not null, lastName varchar(200) not null, email varchar(200) not null, primary key (id))");
      jdbcTemplate
            .execute("create table orders.catalogitem (id bigint not null, itemNumber varchar(200) not null, itemName varchar(200) not null, itemType varchar(200) not null, primary key (id))");
      jdbcTemplate
            .execute("create table orders.\"order\" (id bigint not null, customer_id bigint not null, orderNumber varchar(200) not null, timeOrderPlaced timestamp not null, lastUpdate timestamp not null, status varchar(200) not null, primary key (id))");
      jdbcTemplate
            .execute("alter table orders.\"order\" add constraint orders_fk_1 foreign key (customer_id) references orders.customer (id)");
      jdbcTemplate
            .execute("create table orders.orderItem (id bigint not null, order_id bigint not null, catalogitem_id bigint not null, status varchar(200) not null, price decimal(20,5), lastUpdate timestamp not null, quantity integer not null, primary key (id))");
      jdbcTemplate
            .execute("alter table orders.orderItem add constraint orderItem_fk_1 foreign key (order_id) references orders.\"order\" (id)");
      jdbcTemplate
            .execute("alter table orders.orderItem add constraint orderItem_fk_2 foreign key (catalogitem_id) references orders.catalogitem (id)");
      jdbcTemplate
            .execute("create sequence orders.CUSTOMER_ID_SEQ as integer START WITH 100 INCREMENT BY 1");
      jdbcTemplate
            .execute("create sequence orders.CATALOGITEM_ID_SEQ as integer START WITH 100 INCREMENT BY 1");
      jdbcTemplate
            .execute("create sequence orders.ORDER_ID_SEQ as integer START WITH 100 INCREMENT BY 1");
      jdbcTemplate
            .execute("create sequence orders.ORDERITEM_ID_SEQ as integer START WITH 100 INCREMENT BY 1");
   }

   /**
    * Tears down the orders database in Apache Derby as part of the Spring
    * container life-cycle.
    * 
    * @throws Exception
    */
   public void destroy() throws Exception {

      try {
         jdbcTemplate.execute("drop table orders.orderItem");
         jdbcTemplate.execute("drop table orders.\"order\"");
         jdbcTemplate.execute("drop table orders.catalogitem");
         jdbcTemplate.execute("drop table orders.customer");
         jdbcTemplate.execute("drop schema orders");
      } catch (Throwable e) {
         // ignore
      }
   }

   /**
    * @param jdbcTemplate
    *           the jdbcTemplate to set
    */
   public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
   }

}
