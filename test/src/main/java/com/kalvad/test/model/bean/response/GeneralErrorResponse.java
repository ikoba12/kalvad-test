package com.kalvad.test.model.bean.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class GeneralErrorResponse {

    private String errorCode;

    private String description;

}
