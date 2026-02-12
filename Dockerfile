# Multi-stage build for Bus Reservation System
# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the project
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM tomcat:10.1

# Remove default ROOT application
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Configure Tomcat to listen on 8081 instead of 8080
RUN sed -i 's/port="8080"/port="8081"/' /usr/local/tomcat/conf/server.xml

# Copy WAR file from builder stage
COPY --from=builder /build/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose port
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8081/ || exit 1

# Start Tomcat
CMD ["catalina.sh", "run"]
