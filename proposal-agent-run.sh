# unpack-cds-aot
RUN_JAR=proposal-agent-1.0-SNAPSHOT.jar
BUILD_FILE="./proposal-agent/build/libs/${RUN_JAR}"

cd proposal-agent-application
#
java -Xmx512m -Dspring.aot.enabled=true -XX:SharedArchiveFile=application.jsa -jar $RUN_JAR
