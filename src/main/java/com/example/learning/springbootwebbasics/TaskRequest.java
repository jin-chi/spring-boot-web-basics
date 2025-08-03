package com.example.learning.springbootwebbasics;

import jakarta.validation.constraints.NotBlank; // NOT NULL かつ空でない文字列であることを検証
import jakarta.validation.constraints.Size; // 文字列の長さを検証

// リクエストボディの JSON をマッピングするための DTO
public class TaskRequest {

    @NotBlank(message = "Title can not empty") // タイトルは必須で、必須で空文字であってはならない
    @Size(min = 3, max = 50, message = "Title must be 3 and 50 characters") // タイトルの長さを制限
    private String title;

    @Size(max = 200, message = "Description can not exceed 200 characters") // 説明の長さを制限（任意だが、長すぎる場合はエラー）
    private String description;

    // Getter / Setter（JSON デシリアライズに必要）
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
}
