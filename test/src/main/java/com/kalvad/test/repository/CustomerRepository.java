package com.kalvad.test.repository;

import com.kalvad.test.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("Select c from Customer c join c.addresses a where a.city= :city")
    List<Customer> findAllByCity(String city);

    List<Customer> findAllByPhoneNumberStartingWith(String phonePrefix);
}
