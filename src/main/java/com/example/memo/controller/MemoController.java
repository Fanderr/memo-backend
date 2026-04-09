package com.example.memo.controller;

import com.example.memo.dto.ApiResponse;
import com.example.memo.dto.MemoRequest;
import com.example.memo.dto.MemoResponse;
import com.example.memo.service.MemoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 備忘錄 REST API 控制器
 *
 * <pre>
 * GET    /api/memos              取得所有備忘錄（分頁）
 * GET    /api/memos/{id}         依 ID 取得備忘錄
 * POST   /api/memos              新增備忘錄
 * PUT    /api/memos/{id}         更新備忘錄
 * DELETE /api/memos/{id}         刪除備忘錄
 * GET    /api/memos/search       搜尋備忘錄
 * GET    /api/memos/category     依分類查詢備忘錄
 * GET    /api/memos/pinned       取得所有釘選備忘錄
 * PATCH  /api/memos/{id}/pin     切換釘選狀態
 * GET    /api/memos/categories   取得所有分類清單
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/api/memos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")   // 允許跨域，正式環境請指定前端網址
public class MemoController {

    private final MemoService memoService;

    /**
     * 取得所有備忘錄（支援分頁）
     * 範例：GET /api/memos?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<MemoResponse>>> getAllMemos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MemoResponse> result = memoService.getAllMemos(pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * 依 ID 取得單筆備忘錄
     * 範例：GET /api/memos/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemoResponse>> getMemoById(@PathVariable Long id) {
        MemoResponse memo = memoService.getMemoById(id);
        return ResponseEntity.ok(ApiResponse.success(memo));
    }

    /**
     * 新增備忘錄
     * 範例：POST /api/memos
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MemoResponse>> createMemo(
            @Valid @RequestBody MemoRequest request) {
        MemoResponse created = memoService.createMemo(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("備忘錄新增成功", created));
    }

    /**
     * 更新備忘錄
     * 範例：PUT /api/memos/1
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MemoResponse>> updateMemo(
            @PathVariable Long id,
            @Valid @RequestBody MemoRequest request) {
        MemoResponse updated = memoService.updateMemo(id, request);
        return ResponseEntity.ok(ApiResponse.success("備忘錄更新成功", updated));
    }

    /**
     * 刪除備忘錄
     * 範例：DELETE /api/memos/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMemo(@PathVariable Long id) {
        memoService.deleteMemo(id);
        return ResponseEntity.ok(ApiResponse.success("備忘錄刪除成功", null));
    }

    /**
     * 依關鍵字搜尋備忘錄（搜尋標題與內容）
     * 範例：GET /api/memos/search?keyword=會議&page=0&size=10
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<MemoResponse>>> searchMemos(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MemoResponse> result = memoService.searchMemos(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * 依分類查詢備忘錄
     * 範例：GET /api/memos/category?name=工作&page=0&size=10
     */
    @GetMapping("/category")
    public ResponseEntity<ApiResponse<Page<MemoResponse>>> getMemosByCategory(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MemoResponse> result = memoService.getMemosByCategory(name, pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * 取得所有已釘選的備忘錄
     * 範例：GET /api/memos/pinned
     */
    @GetMapping("/pinned")
    public ResponseEntity<ApiResponse<List<MemoResponse>>> getPinnedMemos() {
        List<MemoResponse> pinned = memoService.getPinnedMemos();
        return ResponseEntity.ok(ApiResponse.success(pinned));
    }

    /**
     * 切換備忘錄釘選狀態
     * 範例：PATCH /api/memos/1/pin
     */
    @PatchMapping("/{id}/pin")
    public ResponseEntity<ApiResponse<MemoResponse>> togglePin(@PathVariable Long id) {
        MemoResponse updated = memoService.togglePin(id);
        String msg = Boolean.TRUE.equals(updated.getIsPinned()) ? "備忘錄已釘選" : "備忘錄已取消釘選";
        return ResponseEntity.ok(ApiResponse.success(msg, updated));
    }

    /**
     * 取得所有分類清單
     * 範例：GET /api/memos/categories
     */
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<String>>> getAllCategories() {
        List<String> categories = memoService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }
}
