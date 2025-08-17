package com.example.learning.springbootwebbasics;

import jakarta.validation.constraints.NotBlank; // NOT NULL かつ空でない文字列であることを検証
import jakarta.validation.constraints.Size; // 文字列の長さを検証

// リクエストボディの JSON をマッピングするための DTO
public class TaskRequest {

    @NotBlank(message = "タイトルは必須です。")
    @Size(min = 3, max = 50, message = "タイトルは100文字以内で入力してください。")
    private String title;

    @Size(max = 200, message = "説明は500文字以内で入力してください。")
    private String description;

    private boolean completed; // タスク完了状態もリクエストで受け取る

    // デフォルトコンストラクタ（JSON デシリアライズで必要）
    public TaskRequest() {
    }

    // コンストラクタ
    public TaskRequest(String title, String description, boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    // --- Getter / Setter---

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
