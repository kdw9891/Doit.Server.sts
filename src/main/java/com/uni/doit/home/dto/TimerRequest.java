package com.uni.doit.home.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimerRequest {

    @JsonProperty("user_id")
    private String user_id;
    
    @JsonProperty("week_number")
    private String week_number;
    
    @JsonProperty("total_study_time")
    private String total_study_time;
    
}
