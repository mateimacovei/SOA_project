FROM amazoncorretto:21.0.2

RUN mkdir -p /app
WORKDIR /app

COPY target/app.jar /app/app.jar

ENTRYPOINT ["java" ,"-jar", "app.jar", "--spring.profiles.active=jwtServiceLocal", "--spring.datasource.url=jdbc:postgresql://postgres:5432/textboard_users"]