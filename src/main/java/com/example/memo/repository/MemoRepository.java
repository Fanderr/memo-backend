package com.example.memo.repository;

import com.example.memo.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 備忘錄資料存取層介面
 * 繼承 JpaRepository 自動提供 CRUD 操作
 */
@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

    /**
     * 依分類查詢備忘錄
     *
     * @param category 分類名稱
     * @param pageable 分頁設定
     * @return 分頁查詢結果
     */
    Page<Memo> findByCategory(String category, Pageable pageable);

    /**
     * 查詢所有已釘選的備忘錄
     *
     * @return 已釘選的備忘錄清單
     */
    List<Memo> findByIsPinnedTrueOrderByUpdatedAtDesc();

    /**
     * 依關鍵字搜尋標題或內容（不分大小寫）
     *
     * @param keyword  搜尋關鍵字
     * @param pageable 分頁設定
     * @return 分頁查詢結果
     */
    @Query("SELECT m FROM Memo m WHERE " +
           "LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY m.isPinned DESC, m.updatedAt DESC")
    Page<Memo> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 查詢所有不重複的分類清單
     *
     * @return 分類名稱清單
     */
    @Query("SELECT DISTINCT m.category FROM Memo m WHERE m.category IS NOT NULL ORDER BY m.category")
    List<String> findAllCategories();
}
