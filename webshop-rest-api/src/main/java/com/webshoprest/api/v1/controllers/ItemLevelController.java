package com.webshoprest.api.v1.controllers;

import com.webshoprest.api.v1.services.ItemLevelService;
import com.webshoprest.domain.Item;
import com.webshoprest.domain.ItemLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(ItemLevelController.BASE_PATH)
@RestController
public class ItemLevelController {

    private ItemLevelService itemLevelService;

    public static final String BASE_PATH = "/api/v1/item-levels";

    @Autowired
    public ItemLevelController(ItemLevelService itemLevelService) {
        this.itemLevelService = itemLevelService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ItemLevel> getAllItemLevels(){
        return itemLevelService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{itemLevelId}")
    public ItemLevel getItemLevelById(@PathVariable Long itemLevelId){
        return itemLevelService.findById(itemLevelId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{itemLevelId}/items")
    public List<Item> getItemLevelItems(@PathVariable Long itemLevelId){
        return itemLevelService.getItems(itemLevelId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{itemLevelId}/items-count")
    public Long getItemLevelItemsCount(@PathVariable Long itemLevelId){
        return itemLevelService.numberOfItems(itemLevelId);
    }

}
