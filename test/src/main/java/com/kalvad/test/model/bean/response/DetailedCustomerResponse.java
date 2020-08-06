package com.kalvad.test.model.bean.response;

import com.kalvad.test.model.entity.Customer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class DetailedCustomerResponse extends CustomerResponse{

    List<AddressResponse> addresses;

    public static DetailedCustomerResponse fromCustomer(Customer customer) {
        DetailedCustomerResponse response = new DetailedCustomerResponse();
        BeanUtils.copyProperties(customer, response);
        response.setAddresses(customer
                .getAddresses()
                .stream()
                .map(AddressResponse::fromAddress)
                .collect(Collectors.toList()));
        return response;
    }
}
