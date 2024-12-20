package com.uni.doit.home.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimerRequest {

    @JsonProperty("user_id")
    private String user_id;
    
    @JsonProperty("study_time")
    private Long study_time;
    
}
