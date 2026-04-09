package com.example.memo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 建立或更新備忘錄的請求 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoRequest {

    /** 備忘錄標題（必填，最長 200 字元） */
    @NotBlank(message = "標題不可為空")
    @Size(max = 200, message = "標題最長 200 字元")
    private String title;

    /** 備忘錄內容 */
    private String content;

    /** 分類標籤（最長 50 字元） */
    @Size(max = 50, message = "分類標籤最長 50 字元")
    private String category;

    /** 是否釘選 */
    private Boolean isPinned;
}
