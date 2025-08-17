package com.example.learning.springbootwebbasics;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // このクラスがサービス層のコンポーネントであることを示す
@Transactional // クラス内の全てのパブリックメソッドにトランザクションを適用
public class TaskService {
    
    private final TaskRepository taskRepository;

    // コンストラクタインジェクション（ Spring が TaskRepository のインスタンスを自動的に提供）
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // 全てのタスクを取得する
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    // ID でタスクを取得する
    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // 新しいタスクを作成する
    public Task createTask(Task task) {
        // ここにビジネスロジックを追加できる（例：タスク名の重複チェックなど）
        return taskRepository.save(task);
    }

    // タスクを更新する
    public Optional<Task> udateTask(Long id, TaskRequest taskRequest) {
        return taskRepository.findById(id)
                .map(existingTask -> {
                    existingTask.setTitle(taskRequest.getTitle());
                    existingTask.setDescription(taskRequest.getDescription());
                    existingTask.setCompleted(taskRequest.isCompleted());
                    return taskRepository.save(existingTask);
                });

    }

    // タスクを削除する
    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
