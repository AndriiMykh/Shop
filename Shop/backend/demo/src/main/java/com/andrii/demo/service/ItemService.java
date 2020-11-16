package com.andrii.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.andrii.demo.entity.Category;
import com.andrii.demo.entity.Item;
import com.andrii.demo.exception.DataNotFoundException;
import com.andrii.demo.repository.ItemRepository;

@Service
public class ItemService {
	private final ItemRepository itemRepository;

	public ItemService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}
	
	public List<Item> retrieveAllItems() {
		return itemRepository.findAll();
	}
	
	public Optional<Item> retrieveItemById(long id) {
		return itemRepository.findById(id);
	}
	
	public List<Item> retrieveItemByCategory(Category category){
		return itemRepository.findItemsByCategory(category);
	}
}
