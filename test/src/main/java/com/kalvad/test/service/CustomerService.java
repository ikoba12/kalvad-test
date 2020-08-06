package com.kalvad.test.service;

import com.kalvad.test.model.bean.request.CreateAddressRequest;
import com.kalvad.test.model.bean.request.CreateCustomerRequest;
import com.kalvad.test.model.bean.request.CustomerSearchRequest;
import com.kalvad.test.model.bean.response.CustomerResponse;
import com.kalvad.test.model.bean.response.DetailedCustomerResponse;

import java.util.List;

public interface CustomerService {

    List<CustomerResponse> list();

    DetailedCustomerResponse one(Long id);

    void create(CreateCustomerRequest request);

    void createAddress(Long customerId, CreateAddressRequest request);

    void deleteAddress(Long customerId, Long addressId);

    List<CustomerResponse> list(CustomerSearchRequest request);
}
