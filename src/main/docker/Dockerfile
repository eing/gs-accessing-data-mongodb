############################################################
# Dockerfile 
############################################################

# Base image 
FROM java:8-jre

RUN apt-get update

# Define working directory.
WORKDIR /data

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

# Define default command.
CMD ["bash"]

VOLUME /tmp
ADD gs-accessing-data-mongodb-0.1.0.jar target/app.jar
RUN sh -c 'touch target/app.jar'
ENTRYPOINT ["java","-jar","target/app.jar"]

# Expose HTTP
EXPOSE 8080 8081
