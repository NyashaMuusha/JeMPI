package org.jembi.jempi.em

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.{
  ClassTagExtensions,
  DefaultScalaModule
}
import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.common.serialization.{Serde, Serdes}
import org.apache.kafka.streams.kstream.{Consumed, KStream}
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}
import org.jembi.jempi.em.kafka.Config.{
  CFG_KAFKA_APPLICATION_ID,
  CFG_KAFKA_BOOTSTRAP_SERVERS,
  CFG_KAFKA_CLIENT_ID,
  CFG_KAFKA_TOPIC_INTERACTION_EM
}
import org.jembi.jempi.em.kafka.Producer

import java.util.Properties
import scala.collection.immutable.ArraySeq
import scala.collection.mutable.ArrayBuffer
import scala.collection.parallel.immutable.ParVector

object EM_Scala extends LazyLogging {

  private val mapper = new ObjectMapper() with ClassTagExtensions
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  private val buffer = new ArrayBuffer[ArraySeq[String]]()

  def main(args: Array[String]): Unit = {

    val props = loadConfig()
    val stringSerde: Serde[String] = Serdes.String()
    val streamsBuilder: StreamsBuilder = new StreamsBuilder()
    val patientRecordKStream: KStream[String, String] = streamsBuilder.stream(
      CFG_KAFKA_TOPIC_INTERACTION_EM,
      Consumed.`with`(stringSerde, stringSerde)
    )
    patientRecordKStream.foreach((_, json) => {
      val interactionEnvelop =
        mapper.readValue(json, classOf[InteractionEnvelop])
      interactionEnvelop.contentType match {
        case "BATCH_START_SENTINEL" => buffer.clearAndShrink()
        case "BATCH_END_SENTINEL" =>
          val parVector = new ParVector(buffer.toVector)
          buffer.clearAndShrink()
          val emRunnable: EM_Runnable = new EM_Runnable(parVector)
          val thread: Thread = new Thread(emRunnable)
          thread.start();
        case "BATCH_INTERACTION" =>
          if (interactionEnvelop.interaction.isDefined) {
            val interaction =
              interactionEnvelop.interaction.get.demographicData.fields.map(f =>
                f.value
              )
            buffer += interaction
            logger.info("{}", interaction.mkString(", "))
          }
      }
    })
    val patientKafkaStreams: KafkaStreams =
      new KafkaStreams(streamsBuilder.build(), props)
    patientKafkaStreams.cleanUp()
    patientKafkaStreams.start()

  }

  private def loadConfig(): Properties = {
    val props = new Properties()
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, CFG_KAFKA_APPLICATION_ID)
    props.put(StreamsConfig.CLIENT_ID_CONFIG, CFG_KAFKA_CLIENT_ID)
    props.put(
      StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
      CFG_KAFKA_BOOTSTRAP_SERVERS
    )
    props
  }

  private class EM_Runnable(val interactions: ParVector[ArraySeq[String]])
      extends Runnable {

    def run(): Unit = {
      val (mu, ms) = Profile.profile(EM_Task.run(interactions))

      Fields.FIELDS.zipWithIndex.foreach(x =>
        Utils.printMU(x._1.name, mu(x._2))
      )
      logger.info(s"$ms ms")
      Producer.send(mu);
    }

  }

}
