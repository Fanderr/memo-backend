package com.example.memo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 備忘錄回應 DTO（回傳給前端的資料格式）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoResponse {

    private Long id;
    private String title;
    private String content;
    private String category;
    private Boolean isPinned;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
