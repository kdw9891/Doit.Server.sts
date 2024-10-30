package com.uni.doit.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseRequest {
    
    @JsonProperty("user_id")
    private String user_id;
    
    @JsonProperty("item_id")
    private Long item_id;
    
    @JsonProperty("quantity")
    private Long quantity;
}
