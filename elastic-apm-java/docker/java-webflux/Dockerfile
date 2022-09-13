FROM maven:3.8.4-jdk-11 as build

ENV APP_NAME app
ENV APP_HOME /home/app
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

WORKDIR $APP_HOME

COPY ../../../spring-webflux-apm-app/pom.xml .
COPY ../../../spring-webflux-apm-app/src $APP_HOME/src

RUN mvn dependency:go-offline

RUN mvn package -Dmaven.test.skip=true


#Run application Stage
#We only need java
FROM adoptopenjdk:14-jre-hotspot AS base

ENV APP_NAME app
ENV APP_HOME /home/app
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

WORKDIR $APP_HOME

COPY --from=build /home/app/target/*.jar $APP_HOME/app.jar

EXPOSE 8080

# RUN wget -O apm-agent.jar https://search.maven.org/remotecontent?filepath=co/elastic/apm/elastic-apm-agent/1.20.0/elastic-apm-agent-1.20.0.jar

# CMD java -javaagent:$APP_HOME/apm-agent.jar $JVM_OPTIONS -jar $APP_HOME/app.jar
CMD java $JVM_OPTIONS -jar $APP_HOME/app.jar
