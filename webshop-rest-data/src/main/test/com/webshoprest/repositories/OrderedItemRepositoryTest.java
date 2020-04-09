package com.webshoprest.repositories;

import com.webshoprest.domain.OrderedItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
class OrderedItemRepositoryTest {

    @Autowired
    private OrderedItemRepository orderedItemRepository;

    @BeforeAll
    private void initData(){
        OrderedItem orderedItem1 = new OrderedItem();
        orderedItem1.setOrderedItemName("order1");

        OrderedItem orderedItem2 = new OrderedItem();
        orderedItem2.setOrderedItemName("order2");

        OrderedItem orderedItem3 = new OrderedItem();
        orderedItem3.setOrderedItemName("order3");

        List<OrderedItem> orderedItemList = new ArrayList<>();
        orderedItemList.add(orderedItem1);
        orderedItemList.add(orderedItem2);
        orderedItemList.add(orderedItem3);

        orderedItemRepository.saveAll(orderedItemList);
    }

    @Test
    void testFindByOrderedItemName() {
        OrderedItem orderedItem = orderedItemRepository.findByOrderedItemName("order1");
        assertThat(orderedItem.getOrderedItemName()).isEqualTo("order1");
    }
}