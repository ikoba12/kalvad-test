package com.kalvad.test.service.impl;

import com.kalvad.test.model.bean.request.CreateAddressRequest;
import com.kalvad.test.model.bean.request.CreateCustomerRequest;
import com.kalvad.test.model.bean.request.CustomerSearchRequest;
import com.kalvad.test.model.bean.response.CustomerResponse;
import com.kalvad.test.model.bean.response.DetailedCustomerResponse;
import com.kalvad.test.model.entity.Address;
import com.kalvad.test.model.entity.Customer;
import com.kalvad.test.model.enums.ErrorCode;
import com.kalvad.test.model.exception.ApiException;
import com.kalvad.test.repository.AddressRepository;
import com.kalvad.test.repository.CustomerRepository;
import com.kalvad.test.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

    private AddressRepository addressRepository;

    @Override
    public List<CustomerResponse> list(CustomerSearchRequest request) {
        List<Customer> customers;
        if (!StringUtils.isEmpty(request.getCity())) {
            customers = customerRepository.findAllByCity(request.getCity());
        } else if (!StringUtils.isEmpty(request.getPhonePrefix())) {
            customers = customerRepository.findAllByPhoneNumberStartingWith(request.getPhonePrefix());

        } else {
            throw new ApiException(ErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
        }

        return customers.stream()
                .map(CustomerResponse::fromCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> list() {
        return customerRepository
                .findAll()
                .stream()
                .map(CustomerResponse::fromCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public DetailedCustomerResponse one(Long id) {
        return customerRepository.findById(id).map(DetailedCustomerResponse::fromCustomer).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, NOT_FOUND));
    }

    @Override
    public void create(CreateCustomerRequest request) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(request, customer);
        customerRepository.save(customer);
    }

    @Override
    public void createAddress(Long customerId, CreateAddressRequest request) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ApiException(
                ErrorCode.NOT_FOUND.getCode(),
                String.format("Customer not found with given id : %s", customerId),
                NOT_FOUND));
        Address address = new Address();
        BeanUtils.copyProperties(request, address);
        address.setCustomer(customer);
        addressRepository.save(address);
    }

    @Override
    public void deleteAddress(Long customerId, Long addressId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new ApiException(
                        ErrorCode.NOT_FOUND.getCode(),
                        String.format("Customer not found with given id : %d", customerId),
                        NOT_FOUND));
        Address address = customer.getAddresses().stream().filter(a -> a.getId().equals(addressId)).findAny().orElseThrow(
                () -> new ApiException(
                        ErrorCode.NOT_FOUND.getCode(),
                        String.format("Address not found with given id : %d for given customer : %d", addressId, customerId),
                        NOT_FOUND));
        addressRepository.delete(address);
    }

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Autowired
    public void setAddressRepository(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
}
