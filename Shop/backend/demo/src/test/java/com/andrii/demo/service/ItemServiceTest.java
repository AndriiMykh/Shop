package com.andrii.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.andrii.demo.entity.Category;
import com.andrii.demo.entity.Item;
import com.andrii.demo.repository.ItemRepository;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

	@Mock
	private ItemRepository itemRepository;
	
	@InjectMocks
	private ItemService itemService;
	@Test
	void shouldRetrieveAllItems() {
		given(itemService.retrieveAllItems()).willReturn(listItems());
		
		List<Item> getItems = itemService.retrieveAllItems();
		
		assertThat(getItems).hasSize(5);

	}
	
	@Test
	void shouldRetrieveAnItemById() {
		long id = 5l;
		Optional<Item> filteredItems;
		List<Item> items = listItems();
		items=items.stream().filter(item->item.getId()==id).collect(Collectors.toList());
		
		filteredItems=Optional.of(items.get(0));
		given(itemService.retrieveItemById(id)).willReturn(filteredItems);
		
		Optional<Item> item = itemService.retrieveItemById(id);
		
		assertThat(item.get()).isInstanceOf(Item.class);
	}
	
	@Test
	void shouldFindItemsByCategory() {
		String category = "food";
		List<Item> items=listItems().stream().filter(item->item.getCategory().toString()
				.equals(category.toUpperCase())).collect(Collectors.toList());
		given(itemService.retrieveItemByCategory(Category.valueOf(category.toUpperCase()))).willReturn(items);
		List<Item> resultListItems = itemService.retrieveItemByCategory(Category.valueOf(category.toUpperCase()));
		
		assertThat(resultListItems).isNotEmpty();
	} 
	
	@Test
	void shouldReturnAllAvailableItemsWhereAvailabilityIsMoreThanZero() {
		List<Item> availableItems= listItems().stream().filter(item->item.getAvailability()>0).collect(Collectors.toList());
		given(itemService.retrieveAllAvailableItems()).willReturn(availableItems);
		
		List<Item> items = itemService.retrieveAllAvailableItems();
		
		assertThat(items).hasSize(4);
	}
	
	@Test
	void shouldSaveUserSuccessfully() {
		when(itemService.saveItem(Mockito.any(Item.class)))
        .thenAnswer(i -> i.getArguments()[0]);
		Item item = new Item(6l, "chocolate", 0, 12.70,Category.FOOD);
		
		assertThat(itemService.saveItem(item)).isInstanceOf(Item.class);
		
	}
	
	@Test
	void shouldDeleteItemById() {
		itemService.deleteITemById(1l);
		verify(itemRepository, atLeastOnce()).deleteById(1l);
		verify(itemRepository, never()).deleteById(5L);
	}
	
	public List<Item> listItems(){
		ArrayList<Item> items = new ArrayList<>();
		items.add(new Item(5l, "bread", 6, 12.70,Category.FOOD));
		items.add(new Item(6l, "chocolate", 0, 12.70,Category.FOOD));
		items.add(new Item(7l, "Air Wick Electrical", 8, 12.70,Category.CHEMICALS));
		items.add(new Item(10l, "When Breath Becomes Air", 3, 12.70,Category.BOOKS));
		items.add(new Item(15l, "red wine", 1, 12.70,Category.BEVERAGE));
		
		return items;
	}
	
	
}
