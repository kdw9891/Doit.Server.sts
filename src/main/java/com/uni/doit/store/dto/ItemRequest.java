package com.uni.doit.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRequest {
    
    @JsonProperty("item_category")
    private String item_category;

}
