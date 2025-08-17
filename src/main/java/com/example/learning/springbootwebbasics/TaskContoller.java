package com.example.learning.springbootwebbasics;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskContoller {

    private final TaskService taskService;

    public TaskContoller(TaskService taskService) {
        this.taskService = taskService;
    }

    // 全てのタスクを取得
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.findAll();
        return ResponseEntity.ok(tasks);
    }

    // ID でタスクを取得
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        // ResponseEntity.notFound().build() は 404 Not Content を返す
        return taskService.findTaskById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " not found"));
    }

    // 新しいタスクを作成
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody @Valid TaskRequest taskRequest) {
        // TaskRequest から Task エンティティを作成
        Task newTask = new Task(taskRequest.getTitle(), taskRequest.getDescription(), taskRequest.isCompleted());
        Task savedTask = taskService.createTask(newTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    // タスクを更新
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody @Valid TaskRequest taskRequest) {
        return taskService.udateTask(id, taskRequest)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " not found for update"));
    }

    // タスクを削除
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        // 204 No Content
        if (taskService.deleteTask(id)) {
            return ResponseEntity.noContent().build();
        }
        // 404 Not Found
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " not found for deletion");
    }
}
