package com.kalvad.test.repository;

import com.kalvad.test.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long>{
}
