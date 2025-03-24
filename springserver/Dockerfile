FROM openjdk:17-jdk-oracle
WORKDIR /app
COPY build/libs/springserver-0.0.1-SNAPSHOT-plain.jar springserver.jar
EXPOSE 8000
CMD ["java", "-jar", "springserver.jar"]