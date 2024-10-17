package com.uni.doit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdFindRequest {
    
    @JsonProperty("email")
    private String email;
}
