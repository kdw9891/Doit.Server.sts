package com.uni.doit.cat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CatRequest {

    @JsonProperty("user_id")
    private String user_id;
    
    @JsonProperty("cat_level")
    private String cat_level;
    
    @JsonProperty("xp_total")
    private String xp_total;
}
