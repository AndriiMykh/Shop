package com.andrii.demo.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.andrii.demo.entity.Address;
import com.andrii.demo.entity.Card;
import com.andrii.demo.entity.Customer;
import com.andrii.demo.exception.TheEmailAlreadyExistsException;
import com.andrii.demo.repository.CustomerRepository;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
	
	@Mock
	private CustomerRepository customerRepository;
	
	@InjectMocks
	private CustomerService customerService;
	
	@Test
	void shouldReturnAllCustomersWhenCallGetAll() {
		given(customerService.retrieveAllCustomers()).willReturn(customerList());
		
		List<Customer> customerList = customerService.retrieveAllCustomers();
		
		assertThat(customerList).isNotEmpty();
	}
	
	@Test
	void shouldReturnUserById() {
		long id = 6l;
		Optional<Customer> customer =Optional.of(customerList().stream().filter(customerToFilter->customerToFilter.getId()==id).collect(toSingleton()));
		given(customerService.retrieveCustomerById(id)).willReturn(customer);
		
		Optional<Customer> getCustomerFromMethod = customerService.retrieveCustomerById(id);
		
		assertThat(getCustomerFromMethod).isNotEmpty();
	}
	
	@Test
	void shouldThrowTheEmailAlreadyExistsException() {
		Customer customer=new Customer(7l, "Andrii", "Mykhalchuk", "380679724615@mail.ru", "1234",
				new Address( "Wroclaw", "Buforowa", "12", "23-412"),
				new Card("2133431254315321", "12/20", "Andrii mykhalchuk", "123"));
		
		given(customerService.save(customer)).willThrow(new TheEmailAlreadyExistsException("The email already exists"));
		
		assertThatThrownBy(()->customerService.save(customer))
			.isInstanceOf(TheEmailAlreadyExistsException.class)
			.hasMessageContaining("The email already exists");
	}
	
	@Test
	void shouldReturnSaveCustomerWithUniqueEmail() {
		Customer customer=new Customer(13l, "Zaur", "Makharadze", "Zaur@mail.ru", "1234",
				new Address( "Wroclaw", "Buforowa", "12", "23-412"),
				new Card("2133431254315321", "12/20", "Andrii mykhalchuk", "123"));
		given(customerService.save(customer)).willAnswer(i -> i.getArguments()[0]);
		
		
		assertThat(customerService.save(customer)).isInstanceOf(Customer.class);
		
	}
	
	@Test
	void shouldFindByEmailAndPassword() {
		String email="380679724615@mail.ru";
		String password = "1234";
		Customer foundCustomer=customerList().stream().filter(customer->customer.getEmail().equals(email)
				&&customer.getPassword().equals(password)).collect(toSingleton());
		given(customerService.findByEmailAndPassword(email,password)).willReturn(foundCustomer);

		Customer getCustomer=customerService.findByEmailAndPassword(email, password);
		
		assertThat(getCustomer).isNotNull();
		
	}
	
	@Test
	void shouldDeleteCustomerById() {
		customerService.deleteById(1l);
		customerService.deleteById(2l);
		customerService.deleteById(3l);
		verify(customerRepository, times(3)).deleteById(Mockito.anyLong());
		verify(customerRepository, never()).deleteById(5l);
		
	}
	
	public List<Customer> customerList(){
		List<Customer> customerlist= new ArrayList<Customer>();
		customerlist.add(new Customer(7l, "Andrii", "Mykhalchuk", "380679724615@mail.ru", "1234",
				new Address( "Wroclaw", "Buforowa", "12", "23-412"),
				new Card("2133431254315321", "12/20", "Andrii mykhalchuk", "123")));
		customerlist.add(new Customer(6l, "Sergii", "Petrov", "sergii@mail.ru", "312",
				new Address( "Warszawa", "Buforowa", "15", "23-412"),
				new Card("2133431254315321", "12/20", "Andrii mykhalchuk", "123")));
		customerlist.add(new Customer(9l, "Petro", "Petrov", "Petrov@mail.ru", "1234",
				new Address( "Wroclaw", "Buforowa", "Uznanskiego", "23-412"),
				new Card("2133431254315321", "12/20", "Andrii mykhalchuk", "123")));
		customerlist.add(new Customer(10l, "Max", "Maksimov", "Maksimov@mail.ru", "1234",
				new Address( "Wroclaw", "Buforowa", "Uznanskiego", "23-412"),
				new Card("2133431254315321", "12/20", "Andrii mykhalchuk", "123")));
		customerlist.add(new Customer(241l, "Vadim", "Bur", "Vadim@mail.ru", "1234",
				new Address( "Wroclaw", "Buforowa", "Uznanskiego", "23-412"),
				new Card("2133431254315321", "12/20", "Andrii mykhalchuk", "123")));
		
		return customerlist;
	}
	public static <T> Collector<T, ?, T> toSingleton() {
	    return Collectors.collectingAndThen(
	            Collectors.toList(),
	            list -> {
	                if (list.size() != 1) {
	                    throw new IllegalStateException();
	                }
	                return list.get(0);
	            }
	    );
	}
	
}
