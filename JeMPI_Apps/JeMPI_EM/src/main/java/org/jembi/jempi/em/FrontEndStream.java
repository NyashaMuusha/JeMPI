package org.jembi.jempi.em;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.AskPattern;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jembi.jempi.AppConfig;
import org.jembi.jempi.shared.models.BatchEntity;
import org.jembi.jempi.shared.models.GlobalConstants;
import org.jembi.jempi.shared.serdes.JsonPojoDeserializer;
import org.jembi.jempi.shared.serdes.JsonPojoSerializer;

import java.util.Properties;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FrontEndStream {

    private static final Logger LOGGER = LogManager.getLogger(FrontEndStream.class);
    private KafkaStreams patientKafkaStreams;

    FrontEndStream() {
        LOGGER.info("FrontEndStream constructor");
    }

    void addEntity(final ActorSystem<Void> system,
                   final ActorRef<BackEnd.Event> backEnd,
                   final String key,
                   final BatchEntity batchEntity) {
        if (batchEntity.entityType() == BatchEntity.EntityType.BATCH_RECORD) {
            final CompletionStage<BackEnd.EventEntityRsp> result =
                    AskPattern.ask(
                            backEnd,
                            replyTo -> new BackEnd.EventEntityReq(key, batchEntity, replyTo),
                            java.time.Duration.ofSeconds(3),
                            system.scheduler());
            final var completableFuture = result.toCompletableFuture();
            try {
                final var reply = completableFuture.get(5, TimeUnit.SECONDS);
                if (reply != null) {
                    if (!reply.result()) {
                        LOGGER.error("BACK END RESPONSE(ERROR)");
                    }
                } else {
                    LOGGER.error("Incorrect class response");
                }
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    public void open(final ActorSystem<Void> system,
                     final ActorRef<BackEnd.Event> backEnd) {
        LOGGER.info("EM Stream Processor");
        final Properties props = loadConfig();
        final Serde<String> stringSerde = Serdes.String();
        final Serde<BatchEntity> batchEntitySerde = Serdes.serdeFrom(new JsonPojoSerializer<>(),
                                                                     new JsonPojoDeserializer<>(BatchEntity.class));
        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        final KStream<String, BatchEntity> entityStream = streamsBuilder.stream(
                GlobalConstants.TOPIC_PATIENT_EM,
                Consumed.with(stringSerde, batchEntitySerde));
        entityStream.foreach((key, entity) -> addEntity(system, backEnd, key, entity));
        patientKafkaStreams = new KafkaStreams(streamsBuilder.build(), props);
        patientKafkaStreams.cleanUp();
        patientKafkaStreams.start();
        LOGGER.info("KafkaStreams started");
    }

    public void close() {
        LOGGER.warn("Stream closed");
        patientKafkaStreams.close();
    }

    private Properties loadConfig() {
        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, AppConfig.KAFKA_APPLICATION_ID);
        props.put(StreamsConfig.CLIENT_ID_CONFIG, AppConfig.KAFKA_CLIENT_ID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.KAFKA_BOOTSTRAP_SERVERS);
        return props;
    }

}