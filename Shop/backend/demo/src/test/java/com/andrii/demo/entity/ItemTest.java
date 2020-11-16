package com.andrii.demo.entity;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.andrii.demo.entity.Item;

class ItemTest {

	@Test
	void shouldCreateNewObjectWithConstructor() {
		Item constructorItem = new Item(5l, "bread", 6, 12.70,Category.FOOD);
		
		assertAll(
					()->assertEquals(5l, constructorItem.getId()),
					()->assertEquals("bread", constructorItem.getName()),
					()->assertEquals( 12.70, constructorItem.getPrice()),
					()->assertEquals(6, constructorItem.getAvailability()),
					()->assertEquals(Category.FOOD, constructorItem.getCategory())
				);
	}
	
	@Test
	void shouldSetParametersWithSetters() {
		Item setterItem = new Item();
		setterItem.setId(2l);
		setterItem.setName("Chocolate");
		setterItem.setPrice(9.54);
		setterItem.setAvailability(6);
		setterItem.setCategory(Category.FOOD);
		
		assertAll(
				()->assertEquals(2l, setterItem.getId()),
				()->assertEquals("Chocolate", setterItem.getName()),
				()->assertEquals( 9.54, setterItem.getPrice()),
				()->assertEquals(6, setterItem.getAvailability()),
				()->assertEquals(Category.FOOD, setterItem.getCategory())
			);
	}

	@Test
	void objectsShouldBeEqual() {
		Item firstItemToCompare = new Item();
		Item secondItemToCompare = new Item();
		firstItemToCompare.setId(5l);
		firstItemToCompare.setName("When Breath Becomes Air");
		firstItemToCompare.setCategory(Category.BOOKS);
		secondItemToCompare.setId(5l);
		secondItemToCompare.setName("When Breath Becomes Air");
		secondItemToCompare.setCategory(Category.BOOKS);
		
		assertEquals(firstItemToCompare, secondItemToCompare);
		
	}
	
	@Test
	void objectsShouldBeNotEqual() {
		Item firstItemToCompare = new Item();
		Item secondItemToCompare = new Item();
		firstItemToCompare.setId(6l);
		firstItemToCompare.setName("A Brief History of Humankind");
		firstItemToCompare.setCategory(Category.BOOKS);
		secondItemToCompare.setId(5l);
		secondItemToCompare.setName("When Breath Becomes Air");
		secondItemToCompare.setCategory(Category.BOOKS);
		
		assertNotEquals(firstItemToCompare, secondItemToCompare);
	}
}
