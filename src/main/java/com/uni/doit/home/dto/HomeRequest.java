package com.uni.doit.home.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeRequest {

    @JsonProperty("user_id")
    private String user_id;
    
    @JsonProperty("cat_level")
    private String cat_level;
    
    @JsonProperty("xp_total")
    private String xp_total;
    
    @JsonProperty("total_points")
    private String total_points;
    
    @JsonProperty("total_study_time")
    private String total_study_time;
    
}
