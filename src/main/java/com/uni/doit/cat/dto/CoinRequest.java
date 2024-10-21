package com.uni.doit.cat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoinRequest {

    @JsonProperty("user_id")
    private String user_id;
    
    @JsonProperty("total_points")
    private String total_points;

}
