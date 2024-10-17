package com.uni.doit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassFindRequest {
    
	@JsonProperty("user_id")
    private String user_id;
	
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("password")
    private String password;

}
