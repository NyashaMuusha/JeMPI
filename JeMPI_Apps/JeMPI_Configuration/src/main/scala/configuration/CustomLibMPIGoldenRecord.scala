package org.jembi.jempi
package configuration

import java.io.{File, PrintWriter}

private object CustomLibMPIGoldenRecord {

  private val classLocation = "../JeMPI_Shared_Source/custom"
  private val customClassName = "CustomLibMPIGoldenRecord"
  private val packageText = "org.jembi.jempi.libmpi.dgraph"

  def generate(fields: Array[Field]): Unit =
    val classFile: String = classLocation + File.separator + customClassName + ".java"
    println("Creating " + classFile)
    val file: File = new File(classFile)
    val writer: PrintWriter = new PrintWriter(file)
    val margin = 33
    writer.println(
      s"""package $packageText;
         |
         |import com.fasterxml.jackson.annotation.JsonInclude;
         |import com.fasterxml.jackson.annotation.JsonProperty;
         |
         |import java.util.List;
         |
         |import org.jembi.jempi.shared.models.CustomGoldenRecord;
         |
         |@JsonInclude(JsonInclude.Include.NON_NULL)
         |record $customClassName (@JsonProperty("uid") String uid,
         |${" "* margin}@JsonProperty("GoldenRecord.source_id") List<LibMPISourceId> sourceId,""".stripMargin)
    fields.zipWithIndex.foreach {
      case (field, idx) =>
        val propertyName = "GoldenRecord." + field.fieldName
        val parameterType =
          (if (field.isList.isDefined && field.isList.get) "List<" else "") +
            field.fieldType +
            (if (field.isList.isDefined && field.isList.get) ">" else "")
        val parameterName = Utils.snakeCaseToCamelCase(field.fieldName)
        writer.println(
          s"""${" " * margin}@JsonProperty("$propertyName") $parameterType $parameterName${
            if (idx + 1 < fields.length) ","
            else ") {"
          }""".stripMargin)
    }
    writer.println(
      s"""
         |${" " * 3}$customClassName(final CustomLibMPIDGraphEntity dgraphEntity) {
         |${" " * 6}this(null,
         |${" " * 11}List.of(dgraphEntity.sourceId()),""".stripMargin)
    fields.zipWithIndex.foreach {
      case (field, idx) =>
        val arg = (if (field.isList.isDefined && field.isList.get) "List.of(" else "") +
          "dgraphEntity." + Utils.snakeCaseToCamelCase(field.fieldName) +
          "()" +
          (if (field.isList.isDefined && field.isList.get) ")" else "")
        writer.println(
          s"""${" " * 11}$arg${if (idx + 1 < fields.length) "," else ");"}""".stripMargin)
    }
    writer.println(s"   }")

    writer.println(
      s"""
         |   CustomGoldenRecord toCustomGoldenRecord() {
         |      return new CustomGoldenRecord(this.uid(),
         |                                    this.sourceId() != null
         |                                      ? this.sourceId().stream().map(LibMPISourceId::toSourceId).toList()
         |                                      : List.of(),""".stripMargin)
    fields.zipWithIndex.foreach {
      (field, idx) =>
        writer.println(
          s"${" " * 36}this.${Utils.snakeCaseToCamelCase(field.fieldName)}()" +
            (if (idx + 1 < fields.length) "," else ");"))
    }
    writer.println("   }")
    writer.println()
//    writer.println(
//      """   MpiGoldenRecord toMpiGoldenRecord() {
//        |      return new MpiGoldenRecord(this.toCustomGoldenRecord());
//        |   }
//        |""".stripMargin)
    writer.println("}")
    writer.flush()
    writer.close()
  end generate

}