package com.kalvad.test.controller;

import com.kalvad.test.model.bean.request.CreateAddressRequest;
import com.kalvad.test.model.bean.request.CreateCustomerRequest;
import com.kalvad.test.model.bean.response.CustomerResponse;
import com.kalvad.test.model.bean.response.DetailedCustomerResponse;
import com.kalvad.test.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private CustomerService customerService;

    @GetMapping
    public List<CustomerResponse> list(){
        return customerService.list();
    }

    @GetMapping("/{id}")
    public DetailedCustomerResponse one(@PathVariable("id") Long id){
        return customerService.one(id);
    }

    @PostMapping
    @ResponseStatus(NO_CONTENT)
    public void create(@RequestBody @Valid CreateCustomerRequest request) {
        customerService.create(request);
    }

    @PostMapping("/{id}/address")
    @ResponseStatus(NO_CONTENT)
    public void createAddress(@PathVariable("id") Long id, @RequestBody @Valid CreateAddressRequest request) {
        customerService.createAddress(id, request);
    }

    @DeleteMapping("/{id}/address/{addressId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteAddress(@PathVariable("id") Long id, @PathVariable("addressId") Long addressId) {
        customerService.deleteAddress(id, addressId);
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }
}
