# 基础镜像：官方 OpenJDK 8 精简版（alpine 轻量级系统）
FROM amazoncorretto:8-alpine-jre

RUN mkdir /app

WORKDIR /app
# 复制本地 jar 包到容器内
COPY target/springboot-demo-1.0-SNAPSHOT.jar /app

EXPOSE 8080
# 容器启动命令：运行 jar 包
ENTRYPOINT ["java", "-jar", "springboot-demo-1.0-SNAPSHOT.jar"]