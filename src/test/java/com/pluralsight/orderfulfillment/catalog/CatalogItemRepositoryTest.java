package com.pluralsight.orderfulfillment.catalog;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.pluralsight.orderfulfillment.test.BaseJpaRepositoryTest;

public class CatalogItemRepositoryTest extends BaseJpaRepositoryTest {

   @Inject
   private CatalogItemRepository catalogItemRepository;

   @Before
   public void setUp() throws Exception {
      CatalogItemEntity catalogItem = new CatalogItemEntity();
      catalogItem.setItemName("TestItemName");
      catalogItem.setItemNumber("12345");
      catalogItem.setItemType("TestItemType");
      catalogItemRepository.save(catalogItem);
   }

   @After
   public void tearDown() throws Exception {
   }

   @Test
   public void testFindAllSuccess() {
      List<CatalogItemEntity> catalogItems = catalogItemRepository.findAll();
      assertNotNull(catalogItems);
      assertFalse(catalogItems.isEmpty());
      assertTrue(catalogItems.size() == 1);
   }

   @Test
   public void testOrderOrderItemsSuccess() {
      List<CatalogItemEntity> catalogItems = catalogItemRepository.findAll();
      assertNotNull(catalogItems);
      assertFalse(catalogItems.isEmpty());
      CatalogItemEntity catalogItem = catalogItems.get(0);
      assertEquals("TestItemName", catalogItem.getItemName());
      assertEquals("12345", catalogItem.getItemNumber());
      assertEquals("TestItemType", catalogItem.getItemType());

   }
}
