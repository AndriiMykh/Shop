package com.andrii.demo.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CustomerTest {
	
	@Test
	void shouldCreateCustomerWithConstructor() {
		Customer customer = new Customer(7l, "Andrii", "Mykhalchuk", "380679724615mail.ru", "1234",
				new Address( "Wroclaw", "Buforowa", "12", "23-412"),
				new Card("2133431254315321", "12/20", "Andrii mykhalchuk", "123"));
			
		assertAll(
					()->assertThat(customer).isNotNull(),
					()->assertThat(customer.getAddress()).isNotNull(),
					()->assertThat(customer.getCard()).isNotNull(),
					()->assertThat(customer).isInstanceOf(Customer.class),
					()->assertThat(customer.getAddress()).isInstanceOf(Address.class),
					()->assertThat(customer.getCard()).isInstanceOf(Card.class)
				);
	}

}
