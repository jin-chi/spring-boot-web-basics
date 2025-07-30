package com.example.learning.springbootwebbasics;

// リクエストボディの JSON をマッピングするための DTO
public class TaskRequest {

    private String title;
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
