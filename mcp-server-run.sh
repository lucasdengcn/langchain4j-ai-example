rm -fr mcp-server-application

gradle mcp-server:clean mcp-server:build -x test

RUN_JAR=mcp-server-1.0-SNAPSHOT.jar
BUILD_FILE="./build/libs/${RUN_JAR}"

java -Djarmode=tools -jar $BUILD_FILE extract --destination mcp-server-application

cd mcp-server-application

java -XX:ArchiveClassesAtExit=application.jsa -Dspring.context.exit=onRefresh -jar $RUN_JAR

java -Xmx512m -Dspring.aot.enabled=true -XX:SharedArchiveFile=application.jsa -jar $RUN_JAR
