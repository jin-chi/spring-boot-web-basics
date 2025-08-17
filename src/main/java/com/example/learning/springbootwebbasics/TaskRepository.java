package com.example.learning.springbootwebbasics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // このインターフェースがリポジトリであることを示す
public interface TaskRepository extends JpaRepository<Task, Long> {
    /*
     * JpaRepository を継承するだけで、以下のメソッドが自動的に提供される。
     * - save(T entity): エンティティの保存（ 新規作成 または 更新 ）
     * - findById(ID id): エンティティの保存（ 新規作成 または 更新 ）
     * - findAll(): 全権取得
     * - deleteById(ID id): ID による削除
     * など
     *
     * 必要に応じて、独自のメソッドを定義することも可能
     * 例：List<Task> findByCompleted(boolean completed): // completed フィールドでタスクを検索
     */
    
}
