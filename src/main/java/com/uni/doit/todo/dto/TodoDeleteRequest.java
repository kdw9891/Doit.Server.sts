package com.uni.doit.todo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoDeleteRequest {
    
    @JsonProperty("user_id")
    private String user_id;
    
    @JsonProperty("task_id")
    private Long task_id;    
}
