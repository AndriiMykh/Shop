package com.andrii.demo.controller;



import java.awt.event.ItemEvent;
import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
//	@GetMapping("/{id}")
//	private Optional<Item> getItemById(@PathVariable("id") long id) {
//		return itemService.retrieveItemById(id);
//	}
	@GetMapping("/{id}")
	private Item getItemById(@PathVariable("id") long id) {
		Optional<Item> item = itemService.retrieveItemById(id);
		if(item.isEmpty())
			throw new DataNotFoundException("Such a user was'nt found");
		else
			return item.get();
	}
}
