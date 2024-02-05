FROM amazoncorretto:21.0.2


ARG SOA_APPLICATION_PORT=4202
ENV SOA_APPLICATION_PORT_ENV=$SOA_APPLICATION_PORT

RUN mkdir -p /app
WORKDIR /app

COPY target/app.jar /app/app.jar

ENTRYPOINT java -jar app.jar --server.port=${SOA_APPLICATION_PORT_ENV} --spring.datasource.url=jdbc:postgresql://postgres:5432/textboard_application