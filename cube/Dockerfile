FROM maven:3-jdk-11

ADD . /cube
WORKDIR /cube

COPY /target/*.war /application.war

ENTRYPOINT ["java","-jar","/application.war"]