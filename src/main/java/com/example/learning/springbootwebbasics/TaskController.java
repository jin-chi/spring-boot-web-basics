package com.example.learning.springbootwebbasics;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "タスク管理API", description = "タスクのCURD操作を提供するAPIです。")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // 全てのタスクを取得
    @GetMapping
    @Operation(summary = "全タスクの取得", description = "データベースに保存されている全てのタスクを取得します。")
    @ApiResponse(responseCode = "200", description = "タスク一覧を正常に取得しました。")
    public ResponseEntity<List<Task>> getAllTasks() {
        logger.info("全タスク取得リクエストを受信しました。");
        List<Task> tasks = taskService.findAll();
        logger.info("取得したリクエスト数: {}", tasks.size());
        return ResponseEntity.ok(tasks);
    }

    // ID でタスクを取得
    @GetMapping("/{id}")
    @Operation(summary = "特定のタスクの取得", description = "指定されたIDに一致するタスクを取得します。")
    @ApiResponse(responseCode = "200", description = "指定されたIDのタスクを正常に取得しました。")
    @ApiResponse(responseCode = "400", description = "指定されたIDのタスクが見つかりませんでした。", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        logger.info("ID: {} のタスク取得リクエストを受信しました。", id);
        // ResponseEntity.notFound().build() は 404 Not Content を返す
        return taskService.findTaskById(id)
                .map(task -> {
                    logger.debug("ID: {} タスクが見つかりました。", id);
                    return ResponseEntity.ok(task);
                })
                .orElseThrow(() -> {
                    logger.warn("ID: {} のタスクが見つかりませんでした。", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " not found");
                });
    }

    // 新しいタスクを作成
    @PostMapping
    @Operation(summary = "新しいタスクの作成", description = "新しいタスクをデータベースに追加します。")
    @ApiResponse(responseCode = "201", description = "タスクを正常に作成しました。")
    @ApiResponse(responseCode = "400", description = "リクエストの形式が不正です。入力内容を確認してください。", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    public ResponseEntity<Task> createTask(@RequestBody @Valid TaskRequest taskRequest) {
        logger.info("新しいタスク作成リクエストを受信しました。タイトル: {}", taskRequest.getTitle());
        // TaskRequest から Task エンティティを作成
        Task newTask = new Task(taskRequest.getTitle(), taskRequest.getDescription(), taskRequest.isCompleted());
        Task savedTask = taskService.createTask(newTask);
        logger.info("新しいタスクがID: {} として保存されました。", savedTask.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    // タスクを更新
    @PutMapping("/{id}")
    @Operation(summary = "タスクの更新", description = "指定されたIDのタスクを更新します。")
    @ApiResponse(responseCode = "200", description = "タスクを正常に更新しました。")
    @ApiResponse(responseCode = "404", description = "指定されたIDのタスクが見つからず、更新できませんでした。", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    @ApiResponse(responseCode = "400", description = "リクエストの形式が不正です。入力内容を確認してください。", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody @Valid TaskRequest taskRequest) {
        logger.info("ID: {} のタスク更新リクエストを受信しました。", id);
        return taskService.udateTask(id, taskRequest)
                .map(task -> {
                    logger.info("ID: {} のタスクが正常に更新されました。", id);
                    return ResponseEntity.ok(task);
                })
                .orElseThrow(() -> {
                    logger.warn("ID: {} のタスクが見つかりませんでした。更新をキャンセルします。");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Task with ID " + id + " not found for update");
                });
    }

    // タスクを削除
    @DeleteMapping("/{id}")
    @Operation(summary = "タスクの削除", description = "指定されたIDのタスクを削除します。")
    @ApiResponse(responseCode = "204", description = "タスクを正常に削除しました。")
    @ApiResponse(responseCode = "404", description = "指定されたIDのタスクが見つからず、削除できませんでした。", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        logger.info("ID: {} のタスク削除リクエストを受信しました。", id);
        // 204 No Content
        if (taskService.deleteTask(id)) {
            logger.info("ID: {} のタスクが正常に削除されました。");
            return ResponseEntity.noContent().build();
        }
        // 404 Not Found
        logger.warn("ID: {} のタスクが見つかりませんでした。削除をキャンセルします。", id);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " not found for deletion");
    }
}
