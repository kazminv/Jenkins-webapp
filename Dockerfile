FROM openjdk:11
EXPOSE 8080
EXPOSE 3000

COPY ./target/first_java_project-*.jar /usr/app/
WORKDIR /usr/app

CMD java -jar first_java_project-*.jar