FROM openjdk:17
WORKDIR /app
COPY build/libs/nurim-0.0.1.jar nurim-0.0.1.jar
ENTRYPOINT ["java", "-jar", "nurim-0.0.1.jar"]
