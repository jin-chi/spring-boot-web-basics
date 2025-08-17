package com.example.learning.springbootwebbasics;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity // このクラスが JPA エンティティであることを示す
@Table(name = "tasks")
public class Task {

    @Id // 主キーであることを示す
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID がデータベースによって自動生成されることを示す
    private Long id;

    @Column(nullable = false) // このカラムが NULL を許容しないことを示す
    private String title;

    private String description; // 説明は NULL を許容する

    @Column(nullable = false)
    private boolean completed;

    @Column(name = "created_at", nullable = false, updatable = false) // 作成日時、更新不可
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false) // 更新日時
    private LocalDateTime updatedAt;

    // デフォルトコンストラクタ（ JPA で必要）
    public Task() {
    }

    // 全フィールドを持つコンストラクタ（ ID とタイムスタンプは自動生成のため含めない）
    public Task(String title, String description, boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    // --- Getter / Setter ---

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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // エンティティのライフサイクルコールバック（保存前／更新前に日時を自動設定）
    @PrePersist // エンティティが永続化される前に実行
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate // エンティティが更新される直前に実行
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
