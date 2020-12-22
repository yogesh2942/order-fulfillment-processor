package com.pluralsight.orderfulfillment.order;

import static org.junit.Assert.*;

import javax.inject.*;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.jdbc.core.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.web.*;

import com.pluralsight.orderfulfillment.config.*;

/**
 * Test case for testing the execution of the SQL component-based route for
 * routing orders from the orders database to a log component.
 * 
 * @author Michael Hoffman, Pluralsight
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Application.class })
@ActiveProfiles("test")
@WebAppConfiguration
public class NewWebsiteOrderRouteTest {

   @Inject
   private JdbcTemplate jdbcTemplateDerby;

   /**
    * Set up test fixture
    * 
    * @throws Exception
    */
   @Before
   public void setUp() throws Exception {
      // Insert catalog and customer data
      jdbcTemplateDerby
            .execute("insert into orders.catalogitem (id, itemnumber, itemname, itemtype) "
                  + "values (1, '078-1344200444', 'Build Your Own JavaScript Framework in Just 24 Hours', 'Book')");
      jdbcTemplateDerby
            .execute("insert into orders.customer (id, firstname, lastname, email) "
                  + "values (1, 'Larry', 'Horse', 'larry@hello.com')");
   }

   /**
    * Tear down all test data.
    * 
    * @throws Exception
    */
   @After
   public void tearDown() throws Exception {
      jdbcTemplateDerby.execute("delete from orders.orderItem");
      jdbcTemplateDerby.execute("delete from orders.\"order\"");
      jdbcTemplateDerby.execute("delete from orders.catalogitem");
      jdbcTemplateDerby.execute("delete from orders.customer");
   }

   /**
    * Test the successful routing of a new website order.
    * 
    * @throws Exception
    */
   @Test
   public void testNewWebsiteOrderRouteSuccess() throws Exception {
      jdbcTemplateDerby
            .execute("insert into orders.\"order\" (id, customer_id, orderNumber, timeorderplaced, lastupdate, status) "
                  + "values (1, 1, '1001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'N')");
      jdbcTemplateDerby
            .execute("insert into orders.orderitem (id, order_id, catalogitem_id, status, price, quantity, lastupdate) "
                  + "values (1, 1, 1, 'N', 20.00, 1, CURRENT_TIMESTAMP)");
      Thread.sleep(5000);
      int total = jdbcTemplateDerby.queryForObject(
            "select count(id) from orders.\"order\" where status = 'P'", Integer.class);
      assertEquals(1, total);

   }
}
