package com.andrii.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.andrii.demo.entity.Customer;
import com.andrii.demo.exception.DataNotFoundException;
import com.andrii.demo.exception.TheEmailAlreadyExistsException;
import com.andrii.demo.repository.CustomerRepository;

@Service
public class CustomerService {
	private final CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public List<Customer> retrieveAllCustomers() {
		return customerRepository.findAll();
	}

	public Optional<Customer> retrieveCustomerById(long id) {
		// TODO Auto-generated method stub
		return customerRepository.findById(id);
	}

	public Customer save(Customer customer) {
		Optional<Customer> customerCheck = customerRepository.findByEmail(customer.getEmail());
		if(customerCheck.isPresent())
			throw new TheEmailAlreadyExistsException("The email already exists");
		else
			return customerRepository.save(customer);
	}
	
	
	public Customer findByEmailAndPassword(String email, String password) {
		return customerRepository.findByEmailAndPassword(email, password);
	}
	
	public void deleteById(long id) {
		 customerRepository.deleteById(id);
	}
}
