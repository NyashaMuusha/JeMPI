package org.jembi.jempi.linker.backend;

import org.jembi.jempi.shared.models.CustomDemographicData;
import org.jembi.jempi.shared.models.CustomMU;

import java.util.Arrays;
import java.util.List;

import static org.jembi.jempi.linker.backend.LinkerProbabilistic.EXACT_SIMILARITY;
import static org.jembi.jempi.linker.backend.LinkerProbabilistic.JACCARD_SIMILARITY;
import static org.jembi.jempi.linker.backend.LinkerProbabilistic.JARO_SIMILARITY;
import static org.jembi.jempi.linker.backend.LinkerProbabilistic.JARO_WINKLER_SIMILARITY;

final class CustomLinkerProbabilistic {

   static Fields updatedFields = null;

   private CustomLinkerProbabilistic() {
   }

   static CustomMU getMU() {
      return new CustomMU(
         LinkerProbabilistic.getProbability(currentFields.givenName),
         LinkerProbabilistic.getProbability(currentFields.familyName),
         LinkerProbabilistic.getProbability(currentFields.gender),
         LinkerProbabilistic.getProbability(currentFields.dob),
         LinkerProbabilistic.getProbability(currentFields.city),
         LinkerProbabilistic.getProbability(currentFields.phoneNumber),
         LinkerProbabilistic.getProbability(currentFields.nationalId));
   }

   private record Fields(
         LinkerProbabilistic.Field givenName,
         LinkerProbabilistic.Field familyName,
         LinkerProbabilistic.Field gender,
         LinkerProbabilistic.Field dob,
         LinkerProbabilistic.Field city,
         LinkerProbabilistic.Field phoneNumber,
         LinkerProbabilistic.Field nationalId) {
   }

   static Fields currentFields =
      new Fields(new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), 0.6572374F, 5.061E-4F),
                 new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), 0.6410375F, 0.0016179F),
                 new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), 0.9268863F, 0.3888039F),
                 new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), 0.7428104F, 4.52E-5F),
                 new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), 0.8023774F, 0.8023774F),
                 new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), 0.8210417F, 3.55E-5F),
                 new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), 0.8181957F, 3.88E-5F));

   public static float probabilisticScore(
         final CustomDemographicData goldenRecord,
         final CustomDemographicData interaction) {
      // min, max, score, missingPenalty
      final float[] metrics = {0, 0, 0, 1.0F};
      LinkerProbabilistic.updateMetricsForStringField(metrics,
                                                      goldenRecord.givenName, interaction.givenName, currentFields.givenName);
      LinkerProbabilistic.updateMetricsForStringField(metrics,
                                                      goldenRecord.familyName, interaction.familyName, currentFields.familyName);
      LinkerProbabilistic.updateMetricsForStringField(metrics,
                                                      goldenRecord.gender, interaction.gender, currentFields.gender);
      LinkerProbabilistic.updateMetricsForStringField(metrics,
                                                      goldenRecord.dob, interaction.dob, currentFields.dob);
      LinkerProbabilistic.updateMetricsForStringField(metrics,
                                                      goldenRecord.city, interaction.city, currentFields.city);
      LinkerProbabilistic.updateMetricsForStringField(metrics,
                                                      goldenRecord.phoneNumber, interaction.phoneNumber, currentFields.phoneNumber);
      LinkerProbabilistic.updateMetricsForStringField(metrics,
                                                      goldenRecord.nationalId, interaction.nationalId, currentFields.nationalId);
      return ((metrics[2] - metrics[0]) / (metrics[1] - metrics[0])) * metrics[3];
   }

   public static void updateMU(final CustomMU mu) {
      if (mu.givenName().m() > mu.givenName().u()
          && mu.familyName().m() > mu.familyName().u()
          && mu.gender().m() > mu.gender().u()
          && mu.dob().m() > mu.dob().u()
          && mu.city().m() > mu.city().u()
          && mu.phoneNumber().m() > mu.phoneNumber().u()
          && mu.nationalId().m() > mu.nationalId().u()) {
         updatedFields = new Fields(
            new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), mu.givenName().m(), mu.givenName().u()),
            new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), mu.familyName().m(), mu.familyName().u()),
            new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), mu.gender().m(), mu.gender().u()),
            new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), mu.dob().m(), mu.dob().u()),
            new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), mu.city().m(), mu.city().u()),
            new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), mu.phoneNumber().m(), mu.phoneNumber().u()),
            new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), mu.nationalId().m(), mu.nationalId().u()));
      }
   }

}