package com.example.learning.springbootwebbasics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

@RestController // このクラスが RESTful API のコントローラーであることを示す
@RequestMapping("/api/tasks") // このコントローラーの全てのエンドポイントのベースパス
public class TaskContoller {

    // 簡易的なインメモリデータベースとしてListを使用
    private final List<Task> tasks = new ArrayList<>();

    // --- 1. POST: 新しいタスクを作成する（Create） ---
    // HTTP POST リクエストを /api/tasks にマッピング
    // @RequestBody でリクエストボディの JSON を TaskRequest オブジェクトに変換
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest taskRequest) {

        // TaskRequest から Task オブジェクトを作成
        Task newTask = new Task(taskRequest.getTitle(), taskRequest.getDescription());
        tasks.add(newTask); // リストに追加

        // 作成されたタスク情報を TaskResponse に変換して返す
        // HttpStatus.CREATED (201) を設定することで、リソースが正常に作成されたことを示す
        return new ResponseEntity<>(new TaskResponse(newTask), HttpStatus.CREATED);
    }

    // --- 2. GET: 全てのタスクを取得する（Read All） ---
    // HTTP GET リクエストを /api/tasks にマッピング
    @GetMapping
    public List<TaskResponse> getAllTasks() {
        // 全ての Task オブジェクトを TaskResponse のリストに変換して返す
        return tasks.stream()
                .map(TaskResponse::new) // Task -> TaskResponse
                .collect(Collectors.toList());
    }

    // --- 3. GET: 特定のタスクを取得する（Read One） ---
    // HTTP GET リクエストを /api/tasks/{id} にマッピング
    // @PathVariable で URL パスの ID を受け取る
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        Optional<Task> taskOptional = tasks.stream().filter(task -> task.getId().equals(id)).findFirst(); // ID
                                                                                                          // に一致するタスクを探す

        // タスクが見つかった場合は 200 OK とタスク情報を探す
        // 見つからない場合は 404 Not Found を返す
        return taskOptional.map(task -> new ResponseEntity<>(new TaskResponse(task), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // --- 4. PUT: 既存のタスクを更新する（Update） ---
    // HTTP PUT リクエストを /api/tasks/{id} にマッピング
    // @PathVariable で ID 、@RequestBody で更新データを受け取る
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest) {
        Optional<Task> taskOptional = tasks.stream().filter(task -> task.getId().equals(id)).findFirst();

        if (taskOptional.isPresent()) {
            Task existingTask = taskOptional.get();
            existingTask.setTitle(taskRequest.getTitle());
            existingTask.setDescription(taskRequest.getDescription());
            // isCompleted はここでは更新しないが、必要なら TaskRequest に追加して更新可能

            // 更新されたタスク情報を 200 OK で返す
            return new ResponseEntity<>(new TaskResponse(existingTask), HttpStatus.OK);
        } else {
            // タスクが見つからない場合は 404 Not Found を返す
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --- 5. DELETE: 特定のタスクを削除する（Delete） ---
    // HTTP DELETE リクエストを /api/tasks/{id} にマッピング
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        boolean removed = tasks.removeIf(task -> task.getId().equals(id)); // ID に一致するタスクを削除

        if (removed) {
            // 削除成功の場合は 204 No Content （レスポンスボディなし）
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            // タスクが見つからない場合は 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
