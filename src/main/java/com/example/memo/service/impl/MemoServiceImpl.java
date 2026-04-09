package com.example.memo.service.impl;

import com.example.memo.dto.MemoRequest;
import com.example.memo.dto.MemoResponse;
import com.example.memo.entity.Memo;
import com.example.memo.exception.MemoNotFoundException;
import com.example.memo.repository.MemoRepository;
import com.example.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 備忘錄服務層實作
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemoServiceImpl implements MemoService {

    private final MemoRepository memoRepository;

    // ── 查詢 ──────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Page<MemoResponse> getAllMemos(Pageable pageable) {
        log.debug("取得所有備忘錄，頁碼：{}，每頁筆數：{}", pageable.getPageNumber(), pageable.getPageSize());
        // 預設排序：釘選的優先，再依修改時間降序
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Order.desc("isPinned"), Sort.Order.desc("updatedAt")));
        return memoRepository.findAll(sortedPageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MemoResponse getMemoById(Long id) {
        log.debug("查詢備忘錄 ID：{}", id);
        Memo memo = findMemoOrThrow(id);
        return toResponse(memo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemoResponse> searchMemos(String keyword, Pageable pageable) {
        log.debug("搜尋備忘錄，關鍵字：{}", keyword);
        return memoRepository.searchByKeyword(keyword, pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemoResponse> getMemosByCategory(String category, Pageable pageable) {
        log.debug("依分類查詢備忘錄，分類：{}", category);
        return memoRepository.findByCategory(category, pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemoResponse> getPinnedMemos() {
        log.debug("取得所有已釘選的備忘錄");
        return memoRepository.findByIsPinnedTrueOrderByUpdatedAtDesc()
                .stream()
                .map(memo -> toResponse(memo))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        log.debug("取得所有分類清單");
        return memoRepository.findAllCategories();
    }

    // ── 新增 ──────────────────────────────────────────────────

    @Override
    public MemoResponse createMemo(MemoRequest request) {
        log.info("新增備忘錄，標題：{}", request.getTitle());
        Memo memo = Memo.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .isPinned(request.getIsPinned() != null ? request.getIsPinned() : false)
                .build();
        Memo saved = memoRepository.save(memo);
        log.info("備忘錄已新增，ID：{}", saved.getId());
        return toResponse(saved);
    }

    // ── 更新 ──────────────────────────────────────────────────

    @Override
    public MemoResponse updateMemo(Long id, MemoRequest request) {
        log.info("更新備忘錄 ID：{}", id);
        Memo memo = findMemoOrThrow(id);
        memo.setTitle(request.getTitle());
        memo.setContent(request.getContent());
        memo.setCategory(request.getCategory());
        if (request.getIsPinned() != null) {
            memo.setIsPinned(request.getIsPinned());
        }
        Memo updated = memoRepository.save(memo);
        log.info("備忘錄已更新，ID：{}", updated.getId());
        return toResponse(updated);
    }

    @Override
    public MemoResponse togglePin(Long id) {
        log.info("切換備忘錄釘選狀態 ID：{}", id);
        Memo memo = findMemoOrThrow(id);
        memo.setIsPinned(!memo.getIsPinned());
        Memo updated = memoRepository.save(memo);
        log.info("備忘錄 ID：{} 釘選狀態已切換為：{}", id, updated.getIsPinned());
        return toResponse(updated);
    }

    // ── 刪除 ──────────────────────────────────────────────────

    @Override
    public void deleteMemo(Long id) {
        log.info("刪除備忘錄 ID：{}", id);
        Memo memo = findMemoOrThrow(id);
        memoRepository.delete(memo);
        log.info("備忘錄 ID：{} 已刪除", id);
    }

    // ── 私有工具方法 ──────────────────────────────────────────

    /**
     * 根據 ID 查詢備忘錄，找不到則拋出例外
     */
    private Memo findMemoOrThrow(Long id) {
        return memoRepository.findById(id)
                .orElseThrow(() -> new MemoNotFoundException("找不到備忘錄，ID：" + id));
    }

    /**
     * 將 Memo Entity 轉換為 MemoResponse DTO
     */
    private MemoResponse toResponse(Memo memo) {
        return MemoResponse.builder()
                .id(memo.getId())
                .title(memo.getTitle())
                .content(memo.getContent())
                .category(memo.getCategory())
                .isPinned(memo.getIsPinned())
                .createdAt(memo.getCreatedAt())
                .updatedAt(memo.getUpdatedAt())
                .build();
    }
}
