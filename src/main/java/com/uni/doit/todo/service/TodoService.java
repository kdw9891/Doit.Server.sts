package com.uni.doit.todo.service;

import java.util.Map;

import com.uni.doit.framework.utils.BaseService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.http.ResponseEntity;

public class TodoService extends BaseService {
	
	// Todo List 
    public ResponseEntity<?> todoList(Map<String, Object> param) {
        try {
            SqlSession session = getSession();
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "TodoList.SelectTodo", param, "Result"));
        }
        catch (Exception e) {
            return handleDatabaseError(e, "TodoList");
        }
    }
    
 // Todo Insert 
    public ResponseEntity<?> todoInsert(Map<String, Object> param) {
        try {
            SqlSession session = getSession();
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "TodoList.InsertTodo", param, "Result"));
        }
        catch (Exception e) {
            return handleDatabaseError(e, "TodoInsert");
        }
    }

}
