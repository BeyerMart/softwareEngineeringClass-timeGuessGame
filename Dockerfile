FROM maven:3-jdk-11

ADD . /timeguess
WORKDIR /timeguess

COPY /backend/target/*.war /application.war
EXPOSE 8080

ENTRYPOINT ["java","-jar","/application.war"]

