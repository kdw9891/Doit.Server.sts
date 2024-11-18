package com.uni.doit.todo.controller;

import com.uni.doit.framework.utils.BaseController;
import com.uni.doit.framework.utils.ParamUtils;
import com.uni.doit.todo.service.TodoService;
import com.uni.doit.todo.dto.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Tag(name = "Todo")
@RestController
@RequestMapping("/todo")
public class TodoController extends BaseController {
	
	@Autowired
    private TodoService toDoService;

    public TodoController(TodoService toDoService) {
        this.toDoService = toDoService;
    }
    
    @GetMapping("/list")
    public ResponseEntity<?> TodoList(@RequestParam String user_id) throws IOException {
    	Map<String, Object> params = ParamUtils.createParams("user_id", user_id);       
        
        return toDoService.todoList(params);
    }
    
    @PostMapping("/insert")
    public ResponseEntity<?> TodoInsert(@RequestBody TodoInsertRequest toDoInsertRequest) throws IOException {
    	ResponseEntity<Map<String, Object>> validationResult = validateDto(toDoInsertRequest, "user_id", "task_id", "task_title", "task_date");       
        
    	if (validationResult.getStatusCode().is4xxClientError()) {
            return validationResult;
        }

        Map<String, Object> params = validationResult.getBody();
    	
        return toDoService.todoInsert(params);
    }
    
    @PutMapping("/update")
    public ResponseEntity<?> TodoUpdate(@RequestBody TodoUpdateRequest todoUpdateRequest) throws IOException {
    	ResponseEntity<Map<String, Object>> validationResult = validateDto(todoUpdateRequest, "user_id", "task_id", "task_title");       
        
    	if (validationResult.getStatusCode().is4xxClientError()) {
            return validationResult;
        }

        Map<String, Object> params = validationResult.getBody();
    	
        return toDoService.todoUpdate(params);
    }
    
    @PutMapping("/completed")
    public ResponseEntity<?> CompletedUpdate(@RequestParam String user_id, @RequestParam String task_id) throws IOException {
    	Map<String, Object> params = ParamUtils.createParams("user_id", user_id, "task_id", task_id);     
    	
        return toDoService.completedUpdate(params);
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<?> TodoDelete(@RequestBody TodoDeleteRequest toDoDeleteRequest) throws IOException {
    	ResponseEntity<Map<String, Object>> validationResult = validateDto(toDoDeleteRequest, "user_id", "task_id");       
        
    	if (validationResult.getStatusCode().is4xxClientError()) {
            return validationResult;
        }

        Map<String, Object> params = validationResult.getBody();
    	
        return toDoService.todoDelete(params);
    }
}
