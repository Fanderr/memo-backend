-- ============================================================
-- 備忘錄資料庫初始化腳本（SQL Server）
-- 執行此腳本前請先建立資料庫：CREATE DATABASE MemoDb;
-- ============================================================

USE MemoDb;
GO

-- 建立 memo 資料表
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[memo]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[memo] (
        [id]         BIGINT         NOT NULL IDENTITY(1,1),
        [title]      NVARCHAR(200)  NOT NULL,
        [content]    NVARCHAR(MAX)  NULL,
        [category]   NVARCHAR(50)   NULL,
        [is_pinned]  BIT            NOT NULL DEFAULT 0,
        [created_at] DATETIME2      NOT NULL DEFAULT GETDATE(),
        [updated_at] DATETIME2      NOT NULL DEFAULT GETDATE(),
        CONSTRAINT [PK_memo] PRIMARY KEY CLUSTERED ([id] ASC)
    );

    -- 建立分類索引（加速依分類查詢）
    CREATE NONCLUSTERED INDEX [IX_memo_category]
        ON [dbo].[memo] ([category] ASC);

    -- 建立修改時間索引（加速排序）
    CREATE NONCLUSTERED INDEX [IX_memo_updated_at]
        ON [dbo].[memo] ([updated_at] DESC);

    PRINT '資料表 memo 建立完成';
END
ELSE
BEGIN
    PRINT '資料表 memo 已存在，略過建立';
END
GO

-- ============================================================
-- 插入測試資料
-- ============================================================
INSERT INTO [dbo].[memo] ([title], [content], [category], [is_pinned], [created_at], [updated_at])
VALUES
    (N'歡迎使用備忘錄！', N'這是你的第一則備忘錄，可以在這裡記錄任何事情。', N'一般', 1, GETDATE(), GETDATE()),
    (N'今日會議重點', N'1. 確認Q2計畫進度\n2. 討論新功能需求\n3. 決定下次開會時間', N'工作', 0, GETDATE(), GETDATE()),
    (N'購物清單', N'- 牛奶\n- 雞蛋\n- 麵包\n- 蘋果', N'生活', 0, GETDATE(), GETDATE()),
    (N'學習計畫', N'Spring Boot 進階教學\n- JPA 關聯映射\n- Spring Security\n- Docker 部署', N'學習', 0, GETDATE(), GETDATE());
GO

PRINT '測試資料插入完成';
GO
