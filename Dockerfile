# 使用輕量級的 JDK 17 鏡像 (根據你的專案版本調整，如 8, 11, 17, 21)
FROM eclipse-temurin:17-jdk-alpine

# 設定容器內的工作目錄
WORKDIR /app

# 將本地打包好的 jar 檔複製到容器內
# 假設你用 Maven，路徑通常在 target/*.jar
COPY app.jar app.jar

# 執行指令
ENTRYPOINT ["java", "-jar", "app.jar"]