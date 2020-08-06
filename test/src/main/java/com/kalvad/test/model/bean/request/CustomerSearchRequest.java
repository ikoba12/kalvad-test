package com.kalvad.test.model.bean.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomerSearchRequest {
    private String city;

    private String phonePrefix;
}
