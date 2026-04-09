package com.example.memo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 統一 API 回應包裝類別
 *
 * @param <T> 回應資料的型別
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    /** 是否成功 */
    private boolean success;

    /** 訊息 */
    private String message;

    /** 回應資料 */
    private T data;

    /**
     * 建立成功回應（含資料）
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("操作成功")
                .data(data)
                .build();
    }

    /**
     * 建立成功回應（含自訂訊息與資料）
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 建立失敗回應
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}
