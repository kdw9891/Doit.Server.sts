package com.uni.doit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdfindRequest {
    
    @JsonProperty("email")
    private String email;
}
