package com.andrii.demo.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.andrii.demo.entity.Category;
import com.andrii.demo.entity.Item;
import com.andrii.demo.exception.DataNotFoundException;
import com.andrii.demo.service.CustomerServiceTest;
import com.andrii.demo.service.ItemService;
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

    
    @BeforeEach
    void setUp() {
    	this.itemList=new ArrayList<Item>();
    	this.itemList.add(new Item(5l, "bread", 6, 12.70,Category.FOOD));
    	this.itemList.add(new Item(6l, "chocolate", 0, 12.70,Category.FOOD));
    	this.itemList.add(new Item(7l, "Air Wick Electrical", 8, 12.70,Category.CHEMICALS));
    	this.itemList.add(new Item(10l, "When Breath Becomes Air", 3, 12.70,Category.BOOKS));
    	this.itemList.add(new Item(15l, "red wine", 1, 12.70,Category.BEVERAGE));
    }
    
    @Test
    void shouldReturnAllItemsOnGetRequest() throws Exception {
    	given(itemService.retrieveAllItems()).willReturn(itemList);
    	
    	this.mockMvc.perform(get("/api/items/"))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("$.size()", is(itemList.size())));
    }
    
    @Test
    void shouldReturnItemByIdOnGetRequestById() throws Exception {
    	final long itemId = 5l;
    	Item item = itemList.stream().filter(itemInside->itemInside.getId()==itemId).collect(CustomerServiceTest.toSingleton());
    	given(itemService.retrieveItemById(itemId)).willReturn(Optional.of(item));
    	
    	this.mockMvc.perform(get("/api/items/{id}",itemId))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("$.name", is(item.getName())))
    		.andExpect(jsonPath("$.category", is(item.getCategory().toString())));
    }
    
    @Test
    void shouldThrowDataNotFoundException() throws Exception {
    	final long itemId = 200l;
    	given(itemService.retrieveItemById(itemId)).willThrow(DataNotFoundException.class);
    	
    	this.mockMvc.perform(get("/api/items/{id}",itemId))
    		.andExpect(status().isNotFound())
    		.andExpect(result->assertThat(result.getResponse().getContentAsString().contains("Data not found")).isTrue());
    }
    
    @Test
    public void givenBadArgumentsWhenExpectedId() throws Exception {
    	String exceptionParam = "bad_arguments";
    	
    	this.mockMvc.perform(get("/api/items/{id}",exceptionParam)
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isBadRequest())
    			.andExpect(result->assertThat(result.getResponse().getContentAsString().contains("Bad arguments")).isTrue());
    }
    
    
}
