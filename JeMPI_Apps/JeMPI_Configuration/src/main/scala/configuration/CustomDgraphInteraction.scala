package configuration

import java.io.{File, PrintWriter}

private object CustomDgraphInteraction {

  private val classLocation = "../JeMPI_LibMPI/src/main/java/org/jembi/jempi/libmpi/dgraph"
  private val customClassName = "CustomDgraphInteraction"
  private val packageText = "org.jembi.jempi.libmpi.dgraph"

  def generate(fields: Array[CommonField]): Unit =
    val classFile: String = classLocation + File.separator + customClassName + ".java"
    println("Creating " + classFile)
    val file: File = new File(classFile)
    val writer: PrintWriter = new PrintWriter(file)
    val margin = 32
    writer.println(
      s"""package $packageText;
         |
         |import com.fasterxml.jackson.annotation.JsonInclude;
         |import com.fasterxml.jackson.annotation.JsonProperty;
         |import org.jembi.jempi.shared.models.InteractionWithScore;
         |import org.jembi.jempi.shared.models.CustomDemographicData;
         |import org.jembi.jempi.shared.models.Interaction;
         |
         |@JsonInclude(JsonInclude.Include.NON_NULL)
         |record $customClassName(
         |      @JsonProperty("uid") String interactionId,
         |      @JsonProperty("Interaction.source_id") DgraphSourceId sourceId,""".stripMargin)
    fields.zipWithIndex.foreach {
      case (field, _) =>
        val propertyName = s"CustomDgraphConstants.PREDICATE_INTERACTION_${field.fieldName.toUpperCase}"
        val parameterName = Utils.snakeCaseToCamelCase(field.fieldName)
        val parameterType = field.fieldType
        writer.println(
          s"""${" " * 6}@JsonProperty($propertyName) $parameterType $parameterName,""".stripMargin)
    }
    writer.println(
      s"""${" " * 6}@JsonProperty("GoldenRecord.interactions|score") Float score) {
         |   $customClassName(
         |         final Interaction interaction,
         |         final Float score) {
         |      this(interaction.interactionId(),
         |           new DgraphSourceId(interaction.sourceId()),""".stripMargin)
    fields.zipWithIndex.foreach {
      case (field, _) =>
        writer.println(s"${" " * 11}interaction.demographicData().${Utils.snakeCaseToCamelCase(field.fieldName)},")
    }
    writer.println(
      s"""${" " * 11}score);
         |   }""".stripMargin)


    writer.print(
      """
        |   Interaction toInteraction() {
        |      return new Interaction(this.interactionId(),
        |                             this.sourceId() != null
        |                                   ? this.sourceId().toSourceId()
        |                                   : null,
        |                             new CustomDemographicData(""".stripMargin)
    fields.zipWithIndex.foreach {
      (field, idx) =>
        writer.println(
          s"${" " * (if (idx == 0) 0 else 57)}this.${Utils.snakeCaseToCamelCase(field.fieldName)}" +
            (if (idx + 1 < fields.length) "," else "));"))
    }
    writer.println("   }")
    writer.println(
      """
        |   InteractionWithScore toInteractionWithScore() {
        |      return new InteractionWithScore(toInteraction(), this.score());
        |   }""".stripMargin)
    writer.println(
      """
        |}""".stripMargin)
    writer.flush()
    writer.close()
  end generate

}