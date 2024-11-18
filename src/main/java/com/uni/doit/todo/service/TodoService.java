package com.uni.doit.todo.service;

import java.util.Map;

import com.uni.doit.framework.utils.BaseService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
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
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "TodoInsert.InsertTodo", param, "Result"));
        }
        catch (Exception e) {
            return handleDatabaseError(e, "TodoInsert");
        }
    }
    
    // Todo Update 
    public ResponseEntity<?> todoUpdate(Map<String, Object> param) {
        try {
            SqlSession session = getSession();
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "TodoUpdate.UpdateTodo", param, "Result"));
        }
        catch (Exception e) {
            return handleDatabaseError(e, "TodoUpdate");
        }
    }
    
    // Completed Update 
    public ResponseEntity<?> completedUpdate(Map<String, Object> param) {
        try {
            SqlSession session = getSession();
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "TodoUpdate.UpdateCompleted", param, "Result"));
        }
        catch (Exception e) {
            return handleDatabaseError(e, "CompletedUpdate");
        }
    }

    // Todo Delete 
    public ResponseEntity<?> todoDelete(Map<String, Object> param) {
        try {
            SqlSession session = getSession();
            return ResponseEntity.ok(jsonResultUtils.getJsonResult(session, "TodoDelete.DeleteTodo", param, "Result"));
        }
        catch (Exception e) {
            return handleDatabaseError(e, "TodoDelete");
        }
    }
    
}
