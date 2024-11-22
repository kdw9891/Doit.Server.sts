package com.uni.doit.todo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TodoInsertRequest {
    
    @JsonProperty("user_id")
    private String user_id;
    
    @JsonProperty("task_id")
    private Long task_id;
    
    @JsonProperty("task_title")
    private String task_title;
    
}
