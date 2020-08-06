package com.kalvad.test.model.bean.request;

import com.kalvad.test.model.enums.AddressType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateAddressRequest {

    @NotNull
    private AddressType addressType;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    @NotBlank
    private String addressLine;
}
