package com.andrii.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.andrii.demo.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	@Query("SELECT u FROM Customer u WHERE u.email = :email")
	Optional<Customer> findByEmail(@Param("email")String email);
}
