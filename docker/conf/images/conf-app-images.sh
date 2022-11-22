JAVA_VERSION=17.0.5
JAVA_VERSION_X=${JAVA_VERSION}_8

# https://registry.hub.docker.com/r/azul/zulu-openjdk
#export JAVA_BASE_IMAGE=azul/zulu-openjdk:$JAVA_VERSION

# https://hub.docker.com/_/eclipse-temurin/tags
export JAVA_BASE_IMAGE=eclipse-temurin:${JAVA_VERSION_X}-jre

#export JOURNAL_IMAGE=journal:1.0-SNAPSHOT
#export JOURNAL_JAR=Journal-1.0-SNAPSHOT-spring-boot.jar

#export NOTIFICATIONS_IMAGE=notifications:1.0-SNAPSHOT
#export NOTIFICATIONS_JAR=Notifications-1.0-SNAPSHOT-spring-boot.jar


export ASYNC_RECEIVER_IMAGE=async_receiver:1.0-SNAPSHOT
export ASYNC_RECEIVER_JAR=AsyncReceiver-1.0-SNAPSHOT-spring-boot.jar

export SYNC_RECEIVER_IMAGE=sync_receiver:1.0-SNAPSHOT
export SYNC_RECEIVER_JAR=SyncReceiver-1.0-SNAPSHOT-spring-boot.jar

export PREPROCESSOR_IMAGE=preprocessor:1.0-SNAPSHOT
export PREPROCESSOR_JAR=PreProcessor-1.0-SNAPSHOT-spring-boot.jar

# export TEST_01_IMAGE=test_01:1.0-SNAPSHOT
# export TEST_01_JAR=Test_01-1.0-SNAPSHOT-spring-boot.jar

# export STAGING_01_IMAGE=staging_01:1.0-SNAPSHOT
# export STAGING_01_JAR=Staging_01-1.0-SNAPSHOT-spring-boot.jar

# export INPUT_02_IMAGE=input-02:1.0-SNAPSHOT
# export INPUT_02_JAR=Input_02-1.0-SNAPSHOT-spring-boot.jar

#export STAGING_02_IMAGE=staging-02:1.0-SNAPSHOT
#export STAGING_02_JAR=Staging_02-1.0-SNAPSHOT-spring-boot.jar

#export INPUT_DISI_IMAGE=input-disi:1.0-SNAPSHOT
#export INPUT_DISI_JAR=InputDISI-1.0-SNAPSHOT-spring-boot.jar

#export STAGING_DISI_IMAGE=staging-disi:1.0-SNAPSHOT
#export STAGING_DISI_JAR=StagingDISI-1.0-SNAPSHOT-spring-boot.jar

export CONTROLLER_IMAGE=controller:1.0-SNAPSHOT
export CONTROLLER_JAR=Controller-1.0-SNAPSHOT-spring-boot.jar

export EM_IMAGE=em:1.0-SNAPSHOT
export EM_JAR=EM-1.0-SNAPSHOT-spring-boot.jar

export LINKER_IMAGE=linker:1.0-SNAPSHOT
export LINKER_JAR=Linker-1.0-SNAPSHOT-spring-boot.jar

export API_IMAGE=api:1.0-SNAPSHOT
export API_JAR=API-1.0-SNAPSHOT-spring-boot.jar
