# unpack-cds-aot
rm -fr proposal-agent-application

gradle proposal-agent:clean proposal-agent:build -x test

RUN_JAR=proposal-agent-1.0-SNAPSHOT.jar
BUILD_FILE="./proposal-agent/build/libs/${RUN_JAR}"

java -Djarmode=tools -jar $BUILD_FILE extract --destination proposal-agent-application

cd proposal-agent-application
#
java -XX:ArchiveClassesAtExit=application.jsa -Dspring.context.exit=onRefresh -jar $RUN_JAR
#
#java -Xmx512m -Dspring.aot.enabled=true -XX:SharedArchiveFile=application.jsa -jar $RUN_JAR
