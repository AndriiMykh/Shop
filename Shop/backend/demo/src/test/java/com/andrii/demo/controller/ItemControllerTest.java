package com.andrii.demo.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.andrii.demo.entity.Category;
import com.andrii.demo.entity.Item;
import com.andrii.demo.exception.DataNotFoundException;
import com.andrii.demo.service.CustomerServiceTest;
import com.andrii.demo.service.ItemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;


@WebMvcTest(controllers = ItemController.class)
@ActiveProfiles("test")
class ItemControllerTest {

    @Autowired                           
    private MockMvc mockMvc;  
    
    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper objectMapper;
    private List<Item> itemList;
    MediaType json;
    String startUrl;
    @BeforeEach
    void setUp() {
    	this.itemList=new ArrayList<Item>();
    	this.itemList.add(new Item(5l, "bread", 6, 12.70,Category.FOOD));
    	this.itemList.add(new Item(6l, "chocolate", 0, 12.70,Category.FOOD));
    	this.itemList.add(new Item(7l, "Air Wick Electrical", 8, 12.70,Category.CHEMICALS));
    	this.itemList.add(new Item(10l, "When Breath Becomes Air", 3, 12.70,Category.BOOKS));
    	this.itemList.add(new Item(15l, "red wine", 1, 12.70,Category.BEVERAGE));
    	json=MediaType.APPLICATION_JSON;
    	startUrl="/api/items/";
    }
    
    @Test
    void shouldReturnAllItemsOnGetRequest() throws Exception {
    	given(itemService.retrieveAllItems()).willReturn(itemList);
    	
    	this.mockMvc.perform(get(startUrl))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("$.size()", is(itemList.size())));
    }
    
    @Test
    void shouldReturnItemByIdOnGetRequestById() throws Exception {
    	final long itemId = 5l;
    	Item item = itemList.stream().filter(itemInside->itemInside.getId()==itemId).collect(CustomerServiceTest.toSingleton());
    	given(itemService.retrieveItemById(itemId)).willReturn(Optional.of(item));
    	
    	this.mockMvc.perform(get(startUrl+"{id}",itemId))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("$.name", is(item.getName())))
    		.andExpect(jsonPath("$.category", is(item.getCategory().toString())));
    }
    
    @Test
    void shouldThrowDataNotFoundException() throws Exception {
    	final long itemId = 200l;
    	given(itemService.retrieveItemById(itemId)).willThrow(DataNotFoundException.class);
    	
    	this.mockMvc.perform(get(startUrl+"{id}",itemId))
    		.andExpect(status().isNotFound())
    		.andExpect(result->assertThat(result.getResponse().getContentAsString().contains("Data not found")).isTrue());
    }
    
    @Test
    public void givenBadArgumentsWhenExpectedId() throws Exception {
    	String exceptionParam = "bad_arguments";
    	
    	this.mockMvc.perform(get(startUrl+"{id}",exceptionParam)
    			.contentType(json))
    			.andExpect(status().isBadRequest())
    			.andExpect(result->assertThat(result.getResponse().getContentAsString().contains("Bad arguments")).isTrue());
    }
    
    @Test
    public void shouldReturnItemsListByCategory() throws Exception {
    	String category="food";
    	List<Item> filteredItemsByCategory =this.itemList.stream()
    			.filter(item->item.getCategory().toString().equals(category.toUpperCase())).collect(Collectors.toList());
    	given(itemService.retrieveItemByCategory(Category.valueOf(category.toUpperCase()))).willReturn(filteredItemsByCategory);
    	
    	this.mockMvc.perform(get(startUrl+"categories/{category}",category)
    			.contentType(json))
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.size()", is(filteredItemsByCategory.size())));
    }
    
    @Test
    public void shouldReturnAllAvailableItems() throws Exception {
    	List<Item> filteredItemsByCategory =this.itemList.stream().filter(item->item.getAvailability()>0).collect(Collectors.toList());
    	given(itemService.retrieveAllAvailableItems()).willReturn(filteredItemsByCategory);
    	
    	this.mockMvc.perform(get(startUrl+"available")
    			.contentType(json))
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.size()",is(filteredItemsByCategory.size())));
    }
    
    @Test
    public void shouldCreateNewItem() throws Exception {
    	given(itemService.saveItem(any(Item.class))).willAnswer(invocation->invocation.getArgument(0));
    	
    	Item item = new Item("White wine", 4, 13.65, Category.BEVERAGE);
    	
        this.mockMvc.perform(post(startUrl)
                .contentType(json)
                .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.availability", is(item.getAvailability())))
                .andExpect(jsonPath("$.price", is(item.getPrice())))
                .andExpect(jsonPath("$.category", is(item.getCategory().toString())));
    }
    
    @Test
    public void shouldUpdateUser() throws JsonProcessingException, Exception {
    	long itemId=1l;
    	Item item = new Item(itemId,"White wine", 4, 13.65, Category.BEVERAGE);
    	given(itemService.retrieveItemById(itemId)).willReturn(Optional.of(item));
    	given(itemService.saveItem(any(Item.class))).willAnswer((invocation) -> invocation.getArgument(0));
    	
    	this.mockMvc.perform(put(startUrl+"{id}",item.getId())
        .contentType(json)
        .content(objectMapper.writeValueAsString(item)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(item.getName())))
        .andExpect(jsonPath("$.availability", is(item.getAvailability())))
        .andExpect(jsonPath("$.price", is(item.getPrice())))
        .andExpect(jsonPath("$.category", is(item.getCategory().toString())));
    }			
    
    @Test 
    public void shouldReturn404WhenUpdatingNotExistingItem() throws Exception {
    	long id =32l;
    	Item item = new Item(id,"White wine", 4, 13.65, Category.BEVERAGE);
    	given(itemService.retrieveItemById(id)).willReturn(Optional.empty());
    	this.mockMvc.perform(put(startUrl+"{id}",id)
    			.contentType(json)
    			.content(objectMapper.writeValueAsString(item)))
    			.andExpect(status().isNotFound())
    			;
    }
    
    @Test
    public void shouldDeleteUserById() throws Exception {
    	long id =32l;
    	Item item = new Item(id,"White wine", 4, 13.65, Category.BEVERAGE);
    	given(itemService.retrieveItemById(id)).willReturn(Optional.of(item));
    	doNothing().when(itemService).deleteITemById(item.getId());
    	
    	this.mockMvc.perform(delete(startUrl+"{id}",item.getId()))
    		.andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(item.getName())))
            .andExpect(jsonPath("$.availability", is(item.getAvailability())))
            .andExpect(jsonPath("$.price", is(item.getPrice())))
            .andExpect(jsonPath("$.category", is(item.getCategory().toString())));
    }
    
    @Test
    public void shouldReturn404WhenDeletingUnexistingItem() throws Exception {
    	
    	long id=32l;
		given(itemService.retrieveItemById(id)).willReturn(Optional.empty());
		
		this.mockMvc.perform(delete(startUrl+"{id}",id))
				.andExpect(status().isNotFound());
		
    }
    
}
