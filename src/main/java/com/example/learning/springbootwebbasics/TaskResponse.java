package com.example.learning.springbootwebbasics;

// レスポンスボディの JSON をマッピングするための DTO
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private boolean completed;

    // コンストラクタ（Task オブジェクトから TaskResponse を作成）
    public TaskResponse(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.completed = task.getCompleted();
    }

    // Getter（JSON シリアライズに必要）
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean getCompleted() {
        return completed;
    }
}
