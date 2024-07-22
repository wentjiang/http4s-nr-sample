docker-compose down
docker-compose up -d

sleep 5

sbt -Dsbt.ci=true assembly

export JAVA_VERSION="your java path"
export NEW_RELIC_LICENSE_KEY="your nr key"
export NEW_RELIC_APP_NAME="your app name"
export NR_JAR_PATH="your newrelic.jar's path" # make sure put the NR config into this jar path
export JAR_PATH="./target/app.jar"

$JAVA_VERSION -agentlib:jdwp=transport=dt_socket,address=127.0.0.1:8082,suspend=n,server=y -javaagent:${NR_JAR_PATH} -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath ${JAR_PATH} Main
