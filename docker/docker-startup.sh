#!/bin/sh
#===========================================================================================
# JVM Configuration
#===========================================================================================
JAVA_OPTS="-server -Xmx${JVM_XMX:-2048m} -Xms${JVM_XMS:-2048m} -Xmn${JVM_XMN:-1024m} -Xss${JVM_XSS:-512k}"
JAVA_OPTS="${JAVA_OPTS} -XX:MetaspaceSize=${JVM_MetaspaceSize:-128m} -XX:MaxMetaspaceSize=${JVM_MaxMetaspaceSize:-256m}"
JAVA_OPTS="${JAVA_OPTS} -XX:MaxDirectMemorySize=${JVM_MaxDirectMemorySize:-1024m}"
JAVA_OPTS="${JAVA_OPTS} -XX:-OmitStackTraceInFastThrow -XX:-UseAdaptiveSizePolicy"
JAVA_OPTS="${JAVA_OPTS} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${BASE_DIR}/logs/java_heapdump.hprof"
JAVA_OPTS="${JAVA_OPTS} -XX:-UseLargePages"

# GC logging
JAVA_MAJOR_VERSION=$(java -version 2>&1 | sed -E -n 's/.* version "([0-9]*).*$/\1/p')
if [ "$JAVA_MAJOR_VERSION" -ge 9 ]; then
  JAVA_OPTS="${JAVA_OPTS} -Xlog:gc*:file=${BASE_DIR}/logs/openjob_gc.log:time,tags:filecount=10,filesize=100m"
else
  JAVA_OPTS="${JAVA_OPTS} -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection"
  JAVA_OPTS="${JAVA_OPTS} -XX:CMSInitiatingOccupancyFraction=70 -XX:+CMSParallelRemarkEnabled"
  JAVA_OPTS="${JAVA_OPTS} -XX:SoftRefLRUPolicyMSPerMB=0 -XX:+CMSClassUnloadingEnabled -XX:SurvivorRatio=8"
  JAVA_OPTS="${JAVA_OPTS} -Xloggc:${BASE_DIR}/logs/openjob_gc.log -verbose:gc"
  JAVA_OPTS="${JAVA_OPTS} -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps"
  JAVA_OPTS="${JAVA_OPTS} -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M"
fi

# Spring config
CUSTOM_CONFIG="${OPENJOB_CONFIG_PATH:-file:${BASE_DIR}/conf/}"

# logs directory
mkdir -p ${BASE_DIR}/logs

echo "Openjob server is starting, you can docker logs your container"
exec java ${JAVA_OPTS} \
  -Dopenjob.home=${BASE_DIR} \
  -jar ${BASE_DIR}/target/openjob-server.jar \
  ${JAVA_OPT_EXT} \
  --spring.config.additional-location=${CUSTOM_CONFIG} \
  --logging.config=${BASE_DIR}/conf/logback.xml \
  --server.max-http-header-size=524288
