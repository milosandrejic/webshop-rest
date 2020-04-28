package com.webshoprest.api.v1.controllers;

import com.webshoprest.api.v1.exceptions.NullIdException;
import com.webshoprest.api.v1.services.ItemCategoryService;
import com.webshoprest.api.v1.validators.ItemCategoryValidator;
import com.webshoprest.domain.Item;
import com.webshoprest.domain.ItemCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.webshoprest.api.v1.security.SecurityConstants.ADMIN_AUTHORITY_STRING;

@RequestMapping(ItemCategoryController.BASE_URL)
@RestController
public class ItemCategoryController {

    public final static String BASE_URL = "/api/v1/categories";

    private ItemCategoryService itemCategoryService;
    private ItemCategoryValidator itemCategoryValidator;

    @Autowired
    public ItemCategoryController(ItemCategoryService itemCategoryService, ItemCategoryValidator itemCategoryValidator) {
        this.itemCategoryService = itemCategoryService;
        this.itemCategoryValidator = itemCategoryValidator;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ItemCategory> getAllItemCategories(){
        return itemCategoryService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{itemCategoryId}")
    public ItemCategory getItemCategoryById(@PathVariable Long itemCategoryId){
        return itemCategoryService.findById(itemCategoryId);
    }

    @PreAuthorize(ADMIN_AUTHORITY_STRING)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemCategory createNewItemCategory(@Valid @RequestBody ItemCategory itemCategory,
                                              BindingResult itemCategoryBindingResult){
        itemCategoryValidator.validate(itemCategory, itemCategoryBindingResult);
        return itemCategoryService.saveOrUpdateCategory(itemCategory);
    }

    @PreAuthorize(ADMIN_AUTHORITY_STRING)
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public ItemCategory updateExistingItemCategory(@Valid @RequestBody ItemCategory itemCategory,
                                                   BindingResult itemCategoryBindingResult){

        if(itemCategory.getItemCategoryId() == null){
            throw new NullIdException();
        }

        itemCategoryValidator.validate(itemCategory, itemCategoryBindingResult);
        return itemCategoryService.saveOrUpdateCategory(itemCategory);
    }

    @PreAuthorize(ADMIN_AUTHORITY_STRING)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{itemCategoryId}")
    public void deleteItemCategory(@PathVariable Long itemCategoryId){
        itemCategoryService.deleteById(itemCategoryId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{itemCategoryId}/items")
    public List<Item> getItemCategoryItems(@PathVariable Long itemCategoryId){
        return itemCategoryService.getItems(itemCategoryId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{itemCategoryId}/items-count")
    public Long getItemCategoryItemsCount(@PathVariable Long itemCategoryId){
        return itemCategoryService.numberOfItems(itemCategoryId);
    }

}
