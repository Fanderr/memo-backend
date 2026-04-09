# 📝 備忘錄後端系統

使用 **Spring Boot 3.2 + Spring Data JPA + Spring MVC + SQL Server** 建構的 RESTful API 後端。

---

## 📁 專案結構

```
memo-backend/
├── pom.xml
├── sql/
│   └── init.sql                          # 資料庫初始化腳本
└── src/
    └── main/
        ├── java/com/example/memo/
        │   ├── MemoApplication.java       # 應用程式入口
        │   ├── controller/
        │   │   └── MemoController.java    # REST API 控制器
        │   ├── service/
        │   │   ├── MemoService.java       # 服務層介面
        │   │   └── impl/
        │   │       └── MemoServiceImpl.java  # 服務層實作
        │   ├── repository/
        │   │   └── MemoRepository.java    # 資料存取層
        │   ├── entity/
        │   │   └── Memo.java             # JPA 實體
        │   ├── dto/
        │   │   ├── MemoRequest.java      # 請求 DTO
        │   │   ├── MemoResponse.java     # 回應 DTO
        │   │   └── ApiResponse.java      # 統一回應包裝
        │   └── exception/
        │       ├── MemoNotFoundException.java      # 自訂例外
        │       └── GlobalExceptionHandler.java     # 全域例外處理
        └── resources/
            └── application.yml           # 應用程式設定
```

---

## ⚙️ 環境需求

- Java 17+
- Maven 3.6+
- SQL Server 2017+（含 SQL Server Express）

---

## 🚀 啟動步驟

### 1. 建立資料庫

在 SQL Server 執行以下指令建立資料庫：

```sql
CREATE DATABASE MemoDb;
```

接著執行 `sql/init.sql` 初始化資料表與測試資料。

### 2. 修改連線設定

編輯 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=MemoDb;encrypt=true;trustServerCertificate=true
    username: sa
    password: YourPassword123!  # ← 修改為你的密碼
```

### 3. 啟動專案

```bash
mvn spring-boot:run
```

或先打包再執行：

```bash
mvn clean package
java -jar target/memo-backend-1.0.0.jar
```

---

## 🔌 API 清單

| 方法     | 路徑                        | 說明                       |
|----------|-----------------------------|----------------------------|
| `GET`    | `/api/memos`                | 取得所有備忘錄（分頁）     |
| `GET`    | `/api/memos/{id}`           | 依 ID 取得備忘錄           |
| `POST`   | `/api/memos`                | 新增備忘錄                 |
| `PUT`    | `/api/memos/{id}`           | 更新備忘錄                 |
| `DELETE` | `/api/memos/{id}`           | 刪除備忘錄                 |
| `GET`    | `/api/memos/search`         | 依關鍵字搜尋               |
| `GET`    | `/api/memos/category`       | 依分類查詢                 |
| `GET`    | `/api/memos/pinned`         | 取得所有釘選備忘錄         |
| `PATCH`  | `/api/memos/{id}/pin`       | 切換釘選狀態               |
| `GET`    | `/api/memos/categories`     | 取得所有分類名稱清單       |

---

## 📌 API 使用範例

### 新增備忘錄

```http
POST /api/memos
Content-Type: application/json

{
  "title": "今日待辦事項",
  "content": "完成報告、傳送 Email",
  "category": "工作",
  "isPinned": false
}
```

### 回應格式

```json
{
  "success": true,
  "message": "備忘錄新增成功",
  "data": {
    "id": 1,
    "title": "今日待辦事項",
    "content": "完成報告、傳送 Email",
    "category": "工作",
    "isPinned": false,
    "createdAt": "2026-04-09T11:30:00",
    "updatedAt": "2026-04-09T11:30:00"
  }
}
```

### 搜尋備忘錄

```http
GET /api/memos/search?keyword=報告&page=0&size=10
```

### 依分類查詢

```http
GET /api/memos/category?name=工作&page=0&size=10
```

---

## 🗄️ 資料表結構

| 欄位名稱      | 型別             | 說明                   |
|---------------|------------------|------------------------|
| `id`          | `BIGINT` (PK)    | 主鍵，自動遞增         |
| `title`       | `NVARCHAR(200)`  | 標題（必填）           |
| `content`     | `NVARCHAR(MAX)`  | 內容                   |
| `category`    | `NVARCHAR(50)`   | 分類標籤               |
| `is_pinned`   | `BIT`            | 是否釘選（0/1）        |
| `created_at`  | `DATETIME2`      | 建立時間（自動設定）   |
| `updated_at`  | `DATETIME2`      | 修改時間（自動更新）   |
