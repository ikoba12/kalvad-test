package com.kalvad.test.model.bean.response;

import com.kalvad.test.model.entity.Address;
import com.kalvad.test.model.enums.AddressType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class AddressResponse {

    private Long id;

    private AddressType addressType;

    private String city;

    private String country;

    private String addressLine;

    public static AddressResponse fromAddress(Address address) {
        AddressResponse response = new AddressResponse();
        BeanUtils.copyProperties(address, response);
        return response;
    }
}
