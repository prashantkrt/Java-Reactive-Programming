FROM bellsoft/liberica-openjdk-alpine:21
WORKDIR app
ADD https://github.com/vinsguru/java-reactive-programming-course/raw/master/02-external-services/external-services.jar external-services.jar
EXPOSE 8080
CMD ["java", "-jar", "external-services.jar", "--server.port=8080"]

# docker build -t reactiveapp:v1 .
# docker run -p 8080:8080 reactiveapp:v1

# http://localhost:8080/swagger-ui.html