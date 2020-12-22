package com.pluralsight.orderfulfillment.order;

import static org.junit.Assert.*;

import java.math.*;
import java.util.*;

import javax.inject.*;

import org.junit.*;

import com.pluralsight.orderfulfillment.catalog.*;
import com.pluralsight.orderfulfillment.customer.*;
import com.pluralsight.orderfulfillment.test.*;

public class OrderItemRepositoryTest extends BaseJpaRepositoryTest {

   @Inject
   private OrderRepository orderRepository;

   @Inject
   private OrderItemRepository orderItemRepository;

   @Inject
   private CustomerRepository customerRepository;

   @Inject
   private CatalogItemRepository catalogItemRepository;

   @Before
   public void setUp() throws Exception {
      CustomerEntity customer = new CustomerEntity();
      customer.setEmail("test");
      customer.setFirstName("test");
      customer.setLastName("test");
      customerRepository.save(customer);
      CatalogItemEntity catalogItem = new CatalogItemEntity();
      catalogItem.setItemName("test");
      catalogItem.setItemNumber("1234");
      catalogItem.setItemType("test");
      catalogItemRepository.save(catalogItem);
      OrderEntity order = new OrderEntity();
      order.setLastUpdate(new Date(System.currentTimeMillis()));
      order.setOrderNumber("1234");
      order.setStatus("N");
      order.setTimeOrderPlaced(new Date(System.currentTimeMillis()));
      order.setCustomer(customer);
      orderRepository.save(order);
      OrderItemEntity orderItem = new OrderItemEntity();
      orderItem.setStatus("N");
      orderItem.setLastUpdate(new Date(System.currentTimeMillis()));
      orderItem.setPrice(new BigDecimal(1));
      orderItem.setQuantity(1);
      orderItem.setOrder(order);
      orderItem.setCatalogItem(catalogItem);
      orderItemRepository.save(orderItem);
   }

   @After
   public void tearDown() throws Exception {
   }

   @Test
   public void test_findAllOrderItemsSuccess() throws Exception {
      List<OrderItemEntity> orderItems = orderItemRepository.findAll();
      assertNotNull(orderItems);
      assertFalse(orderItems.isEmpty());
   }

   @Test
   public void test_findOrderItemOrderCatalogItemSuccess() throws Exception {
      List<OrderItemEntity> orderItems = orderItemRepository.findAll();
      assertNotNull(orderItems);
      assertFalse(orderItems.isEmpty());
      OrderItemEntity orderItem = orderItems.get(0);
      assertEquals("N", orderItem.getStatus());
   }

}
