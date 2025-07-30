package com.example.learning.springbootwebbasics;

import java.util.concurrent.atomic.AtomicLong;

public class Task {

    private static final AtomicLong counter = new AtomicLong();
    private Long id;
    private String title;
    private String description;
    private boolean completed;

    // コンストラクタ（IDは自動生成）
    public Task(String title, String description) {
        this.id = counter.incrementAndGet();
        this.title = title;
        this.description = description;
        this.completed = false; // デフォルトで未完了
    }

    // デフォルトコンストラクタ（JSON デシリアライズ用）
    public Task() {
    }

    // Getter / Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                '}';

    }
}
