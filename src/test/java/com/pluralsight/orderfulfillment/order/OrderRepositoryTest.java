package com.pluralsight.orderfulfillment.order;

import static org.junit.Assert.*;

import java.util.*;

import javax.inject.*;

import org.junit.*;
import org.springframework.data.domain.*;

import com.pluralsight.orderfulfillment.catalog.*;
import com.pluralsight.orderfulfillment.customer.*;
import com.pluralsight.orderfulfillment.test.*;

public class OrderRepositoryTest extends BaseJpaRepositoryTest {

   @Inject
   private OrderRepository orderRepository;

   @Inject
   private CustomerRepository customerRepository;

   @Before
   public void setUp() throws Exception {
      CustomerEntity customer = new CustomerEntity();
      customer.setEmail("test");
      customer.setFirstName("test");
      customer.setLastName("test");
      customerRepository.save(customer);
      OrderEntity order = new OrderEntity();
      order.setLastUpdate(new Date(System.currentTimeMillis()));
      order.setOrderNumber("1234");
      order.setStatus("N");
      order.setTimeOrderPlaced(new Date(System.currentTimeMillis()));
      order.setCustomer(customer);
      orderRepository.save(order);
   }

   @After
   public void tearDown() throws Exception {
   }

   @Test
   public void test_findAllOrdersSuccess() throws Exception {
      Iterable<OrderEntity> orders = orderRepository.findAll();
      assertNotNull(orders);
      assertTrue(orders.iterator().hasNext());
   }

   @Test
   public void test_findOrderCustomerAndOrderItemsSuccess() throws Exception {
      Iterable<OrderEntity> orders = orderRepository.findAll();
      assertNotNull(orders);
      Iterator<OrderEntity> iterator = orders.iterator();
      assertTrue(iterator.hasNext());
      OrderEntity order = iterator.next();
      assertEquals("1234", order.getOrderNumber());
   }

   @Test
   public void test_findOrdersByOrderStatusOrderByTimeOrderPlacedAscSuccess()
         throws Exception {
      Iterable<OrderEntity> orders = orderRepository.findByStatus(
            OrderStatus.NEW.getCode(), new PageRequest(0, 5));
      assertNotNull(orders);
      assertTrue(orders.iterator().hasNext());
   }

   @Test
   public void test_findOrdersByOrderStatusOrderByTimeOrderPlacedAscFailInvalidStatus()
         throws Exception {
      Iterable<OrderEntity> orders = orderRepository.findByStatus("whefiehwi",
            new PageRequest(0, 5));
      assertNotNull(orders);
      assertFalse(orders.iterator().hasNext());
   }

}
