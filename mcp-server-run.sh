# unpack-cds-aot
#
RUN_JAR=mcp-server-1.0-SNAPSHOT.jar
#
cd mcp-server-application

java -Xmx512m -Dspring.aot.enabled=true -XX:SharedArchiveFile=application.jsa -jar $RUN_JAR
