# Use Maven to build the project
FROM maven:3.8.5-openjdk-17 AS builder

# Set the working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use a lightweight JDK image to run the app
FROM adoptium/openjdk17:latest


# Install required libraries for AWT/Swing (if necessary)
RUN apt-get update && apt-get install -y \
    libx11-dev libxext-dev libxrender-dev libxtst-dev libxi-dev \
    fonts-dejavu fonts-liberation fontconfig \
    && rm -rf /var/lib/apt/lists/*

# Set headless mode (optional)
ENV JAVA_OPTS="-Djava.awt.headless=true"

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
