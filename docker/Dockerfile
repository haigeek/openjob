FROM 192.168.1.181:12316/middleware/openjdk:8-jdk-alpine

LABEL maintainer="stelin <swoft@qq.com>"

ENV BASE_DIR="/home/openjob" \
    JVM_XMX="2048m" \
    JVM_XMS="2048m" \
    JVM_XMN="1024m" \
    JVM_XSS="512k" \
    JVM_MetaspaceSize="128m" \
    JVM_MaxMetaspaceSize="256m" \
    JVM_MaxDirectMemorySize="1024m" \
    OPENJOB_CONFIG_PATH="" \
    TIME_ZONE="Asia/Shanghai"

WORKDIR ${BASE_DIR}

# copy jar (build context is backend root, e.g. openjob/)
COPY openjob-server/openjob-server-starter/target/openjob-server.jar target/

# copy config
COPY openjob-server/openjob-server-starter/release/conf/ conf/

# copy startup script
COPY docker/docker-startup.sh bin/
RUN chmod +x bin/docker-startup.sh

# logs directory
RUN mkdir -p logs

# timezone
RUN ln -snf /usr/share/zoneinfo/${TIME_ZONE} /etc/localtime && echo "${TIME_ZONE}" > /etc/timezone

EXPOSE 8080

ENTRYPOINT ["sh","bin/docker-startup.sh"]
