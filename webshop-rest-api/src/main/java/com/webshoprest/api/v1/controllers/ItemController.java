package com.webshoprest.api.v1.controllers;

import com.webshoprest.api.v1.exceptions.NullIdException;
import com.webshoprest.api.v1.services.ImageService;
import com.webshoprest.api.v1.services.ItemService;
import com.webshoprest.api.v1.util.FileConverter;
import com.webshoprest.api.v1.validators.ImageValidator;
import com.webshoprest.api.v1.validators.ItemValidator;
import com.webshoprest.domain.Item;
import com.webshoprest.domain.ItemCategory;
import com.webshoprest.domain.ItemLevel;
import com.webshoprest.domain.OrderedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.webshoprest.api.v1.security.SecurityConstants.ADMIN_AUTHORITY_STRING;

@RequestMapping(ItemController.BASE_PATH)
@RestController
public class ItemController {

    public static final String BASE_PATH = "/api/v1/items";

    private ItemService itemService;
    private ItemValidator itemValidator;
    private ImageService imageService;

    @Autowired
    public ItemController(ItemService itemService, ItemValidator itemValidator, ImageService imageService) {
        this.itemService = itemService;
        this.itemValidator = itemValidator;
        this.imageService = imageService;
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Item> getAllItems() {
        return itemService.findAllItems();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable Long itemId) {
        return itemService.findById(itemId);
    }

    @PreAuthorize(ADMIN_AUTHORITY_STRING)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Item saveNewItem(@Valid @RequestBody Item item, BindingResult itemBindingResult) {
        itemValidator.validate(item, itemBindingResult);
        return itemService.saveOrUpdateItem(item);
    }

    @PreAuthorize(ADMIN_AUTHORITY_STRING)
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{itemId}/upload-image")
    public String uploadItemImage(@RequestPart("image") MultipartFile image,
                                  @PathVariable Long itemId) throws IOException {

        Item item = itemService.findById(itemId);
        ImageValidator.validateImage(image);
        File file = FileConverter.convertFromMultipartToFile(image, item.getItemId());
        item.setImageUrl(imageService.uploadImage(file));
        itemService.saveOrUpdateItem(item);

        return item.getImageUrl();
    }

    @PreAuthorize(ADMIN_AUTHORITY_STRING)
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{itemId}")
    public Item updateUser(@PathVariable Long itemId,
                             @Valid @RequestBody Item item, BindingResult itemBindingResult){

        if(item.getItemId() == null){
            throw new NullIdException();
        }

        itemValidator.validate(item, itemBindingResult);

        return itemService.saveOrUpdateItem(item);
    }

    @PreAuthorize(ADMIN_AUTHORITY_STRING)
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{itemId}/update-image")
    public String updateItemImage(@PathVariable Long itemId,
                             @RequestPart("image") MultipartFile image) throws IOException {

        Item item = itemService.findById(itemId);
        ImageValidator.validateImage(image);
        File file = FileConverter.convertFromMultipartToFile(image, item.getItemId());
        item.setImageUrl(imageService.uploadImage(file));
        itemService.saveOrUpdateItem(item);

        return item.getImageUrl();
    }

    @PreAuthorize(ADMIN_AUTHORITY_STRING)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{itemId}/delete-image")
    public void deleteItemImage(@PathVariable Long itemId){
        Item item = itemService.findById(itemId);
        imageService.deleteImage(item.getItemId());
    }

    @PreAuthorize(ADMIN_AUTHORITY_STRING)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId){
        itemService.deleteItemById(itemId);
    }

    @PreAuthorize(ADMIN_AUTHORITY_STRING)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{itemId}/item-level")
    public ItemLevel getItemLevel(@PathVariable Long itemId){
        return itemService.getLevel(itemId);
    }

    @PreAuthorize(ADMIN_AUTHORITY_STRING)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{itemId}/item-category")
    public ItemCategory getItemCategory(@PathVariable Long itemId){
        return itemService.getCategory(itemId);
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/top-selling")
    public List<OrderedItem> getTopSellingItems(@RequestParam Long limit){
        return itemService.topSellingItems(limit);
    }
}
