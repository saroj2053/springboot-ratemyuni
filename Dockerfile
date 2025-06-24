# 1. Base image with Java 21
FROM openjdk:26-jdk as base

# 2. Set working directory inside the container
WORKDIR /app

# 3. Copy the JAR file (replace with your actual JAR name)
COPY target/nepal-uni-reviews.jar app.jar

# 4. Copy the .env file
COPY .env .env

# 5. Set environment variable to indicate dev (optional)
ENV ENV=dev

EXPOSE 8080

# 6. Run the JAR
CMD ["java", "-jar", "app.jar"]
