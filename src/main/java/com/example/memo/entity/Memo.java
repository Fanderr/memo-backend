package com.example.memo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 備忘錄實體類別，對應資料庫 memo 資料表
 */
@Entity
@Table(name = "memo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Memo {

    /** 備忘錄主鍵，自動遞增 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** 備忘錄標題，最長 200 字元，不可為空 */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /** 備忘錄內容，使用 nvarchar(MAX) 儲存長文字 */
    @Column(name = "content", columnDefinition = "NVARCHAR(MAX)")
    private String content;

    /** 備忘錄分類標籤 */
    @Column(name = "category", length = 50)
    private String category;

    /** 是否已釘選（置頂） */
    @Column(name = "is_pinned", nullable = false)
    @Builder.Default
    private Boolean isPinned = false;

    /** 建立時間，新增時自動設定 */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 最後修改時間 */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /** 在新增前自動設定建立時間與修改時間 */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /** 在更新前自動設定修改時間 */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
