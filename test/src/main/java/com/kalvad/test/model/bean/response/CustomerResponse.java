package com.kalvad.test.model.bean.response;

import com.kalvad.test.model.entity.Customer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class CustomerResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;

    public static CustomerResponse fromCustomer(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        BeanUtils.copyProperties(customer, response);
        return response;
    }

}
