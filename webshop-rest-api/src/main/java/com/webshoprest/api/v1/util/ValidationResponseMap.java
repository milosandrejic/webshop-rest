package com.webshoprest.api.v1.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ValidationResponseMap {

    private Map<String, String> errorsMap;

}
