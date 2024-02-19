#BUILD
FROM gradle:8.5-jdk21-alpine AS builder

WORKDIR /app

COPY src/main/kotlin/com/woozy/untitled/service ./

RUN gradle clean bootJar

#APP
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/untitled-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "untitled-0.0.1-SNAPSHOT.jar"]