From ubuntu:16.04

ADD ZAP_2.9.0 /ZAP_2.9.0
ADD entrypoint.sh /entrypoint.sh

# Install OpenJDK-8
RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y ant && \
    apt-get clean;

# Fix certificate issues
RUN apt-get update && \
    apt-get install ca-certificates-java && \
    apt-get clean && \
    update-ca-certificates -f;

# Setup JAVA_HOME -- useful for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JAVA_HOME

RUN chmod -R 777 ZAP_2.9.0
RUN chmod 777 /entrypoint.sh

# set port
EXPOSE 8080

CMD /ZAP_2.9.0/zap.sh -daemon -host 0.0.0.0 -config api.key=3343 -config api.addrs.addr.name=.* -config api.addrs.addr.regex=true
