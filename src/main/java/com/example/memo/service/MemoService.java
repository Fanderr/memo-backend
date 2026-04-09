package com.example.memo.service;

import com.example.memo.dto.MemoRequest;
import com.example.memo.dto.MemoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 備忘錄服務層介面
 */
public interface MemoService {

    /**
     * 取得所有備忘錄（分頁）
     *
     * @param pageable 分頁設定
     * @return 分頁備忘錄清單
     */
    Page<MemoResponse> getAllMemos(Pageable pageable);

    /**
     * 依 ID 取得備忘錄
     *
     * @param id 備忘錄 ID
     * @return 備忘錄資料
     */
    MemoResponse getMemoById(Long id);

    /**
     * 新增備忘錄
     *
     * @param request 備忘錄請求資料
     * @return 新增後的備忘錄資料
     */
    MemoResponse createMemo(MemoRequest request);

    /**
     * 更新備忘錄
     *
     * @param id      備忘錄 ID
     * @param request 更新資料
     * @return 更新後的備忘錄資料
     */
    MemoResponse updateMemo(Long id, MemoRequest request);

    /**
     * 刪除備忘錄
     *
     * @param id 備忘錄 ID
     */
    void deleteMemo(Long id);

    /**
     * 依關鍵字搜尋備忘錄
     *
     * @param keyword  搜尋關鍵字
     * @param pageable 分頁設定
     * @return 搜尋結果
     */
    Page<MemoResponse> searchMemos(String keyword, Pageable pageable);

    /**
     * 依分類查詢備忘錄
     *
     * @param category 分類名稱
     * @param pageable 分頁設定
     * @return 分頁查詢結果
     */
    Page<MemoResponse> getMemosByCategory(String category, Pageable pageable);

    /**
     * 取得所有已釘選的備忘錄
     *
     * @return 已釘選備忘錄清單
     */
    List<MemoResponse> getPinnedMemos();

    /**
     * 切換備忘錄釘選狀態
     *
     * @param id 備忘錄 ID
     * @return 更新後的備忘錄資料
     */
    MemoResponse togglePin(Long id);

    /**
     * 取得所有分類清單
     *
     * @return 分類名稱清單
     */
    List<String> getAllCategories();
}
