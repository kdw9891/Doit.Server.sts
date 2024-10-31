package com.uni.doit.inventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvRequest {
    
    @JsonProperty("user_id")
    private String user_id;
    
    @JsonProperty("inventory_id")
    private String inventory_id;
    
    @JsonProperty("item_id")
    private String item_id;
}
