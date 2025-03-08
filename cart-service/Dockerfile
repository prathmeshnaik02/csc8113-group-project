# 第一阶段: 构建 CartService JAR
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# 复制 Maven 配置文件
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# 复制源码并编译
COPY src src
RUN ./mvnw package -DskipTests

# 第二阶段: 运行 Spring Boot 应用
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 创建非 root 用户
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# 复制已编译的 JAR 文件
COPY --from=build /app/target/*.jar /app/app.jar

# 暴露端口
EXPOSE 8080

# 运行 Spring Boot 应用
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
