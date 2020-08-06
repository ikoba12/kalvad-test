package com.kalvad.test.controller;

import com.kalvad.test.model.bean.request.CustomerSearchRequest;
import com.kalvad.test.model.bean.response.CustomerResponse;
import com.kalvad.test.repository.CustomerRepository;
import com.kalvad.test.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    private CustomerService customerService;

    @GetMapping("/city/{name}")
    public List<CustomerResponse> findByCity(@PathVariable("name") String name){
        return customerService.list(CustomerSearchRequest.builder().city(name).build());
    }

    @GetMapping("/phone/{prefix}")
    public List<CustomerResponse> findByPhonePrefix(@PathVariable("prefix") String prefix){
        return customerService.list(CustomerSearchRequest.builder().phonePrefix(prefix).build());
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }
}
