package com.andrii.demo.controller;



import java.awt.event.ItemEvent;
import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.andrii.demo.entity.Category;
import com.andrii.demo.entity.Item;
import com.andrii.demo.exception.DataNotFoundException;
import com.andrii.demo.service.ItemService;

@RestController
@RequestMapping("/api/items")
public class ItemController {
	
	private final ItemService itemService;
	
	public ItemController(ItemService itemService) {
		this.itemService=itemService;
	}
	
	@GetMapping("/")
	private List<Item> getAllItems(){
		return itemService.retrieveAllItems();
	}
	
	@GetMapping("/{id}")
	private Item getItemById(@PathVariable("id") long id) {
		Optional<Item> item = itemService.retrieveItemById(id);
		if(item.isEmpty())
			throw new DataNotFoundException("Such a user was'nt found");
		else
			return item.get();
	}
	
	@GetMapping("/categories/{category}")
	private List<Item> getItemsByID(@PathVariable("category")String category ){
		return itemService.retrieveItemByCategory(Category.valueOf(category.toUpperCase()));
		
	}

	@GetMapping("/available")
	private List<Item> getAllAvailableItems(){
		return itemService.retrieveAllAvailableItems();
	}
	
	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	private Item createNewItem(@RequestBody Item item){
		return itemService.saveItem(item);
	} 
}
