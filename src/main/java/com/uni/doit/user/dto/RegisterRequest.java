package com.uni.doit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @JsonProperty("user_id")
    private String user_id;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("user_nickname")
    private String user_nickname;
    
    @JsonProperty("email")
    private String email;
}
