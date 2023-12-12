package org.jembi.jempi.libconfig.linker;

import org.jembi.jempi.libconfig.shared.models.CustomDemographicData;
import org.jembi.jempi.libconfig.shared.models.CustomMU;

import java.util.List;

import static org.jembi.jempi.libconfig.linker.LinkerProbabilistic.JARO_WINKLER_SIMILARITY;

public final class CustomLinkerProbabilistic {

   public static final int METRIC_MIN = 0;
   public static final int METRIC_MAX = 1;
   public static final int METRIC_SCORE = 2;
   public static final int METRIC_MISSING_PENALTY = 3;
   public static final boolean PROBABILISTIC_DO_LINKING = true;
   public static final boolean PROBABILISTIC_DO_VALIDATING = false;
   public static final boolean PROBABILISTIC_DO_MATCHING = false;

   public static LinkFields updatedLinkFields = null;



   private CustomLinkerProbabilistic() {
   }

   static CustomMU getMU() {
      return new CustomMU(
         LinkerProbabilistic.getProbability(currentLinkFields.givenName),
         LinkerProbabilistic.getProbability(currentLinkFields.familyName),
         LinkerProbabilistic.getProbability(currentLinkFields.gender),
         LinkerProbabilistic.getProbability(currentLinkFields.dob),
         LinkerProbabilistic.getProbability(currentLinkFields.city),
         LinkerProbabilistic.getProbability(currentLinkFields.phoneNumber),
         LinkerProbabilistic.getProbability(currentLinkFields.nationalId));
   }

   private record LinkFields(
         LinkerProbabilistic.Field givenName,
         LinkerProbabilistic.Field familyName,
         LinkerProbabilistic.Field gender,
         LinkerProbabilistic.Field dob,
         LinkerProbabilistic.Field city,
         LinkerProbabilistic.Field phoneNumber,
         LinkerProbabilistic.Field nationalId) {
   }

   public static LinkFields currentLinkFields =
      new LinkFields(
         new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), 0.8806329F, 0.0026558F),
         new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), 0.9140443F, 6.275E-4F),
         new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), 0.9468393F, 0.4436446F),
         new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), 0.7856196F, 4.65E-5F),
         new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), 0.8445694F, 0.0355741F),
         new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), 0.84085F, 4.0E-7F),
         new LinkerProbabilistic.Field(JARO_WINKLER_SIMILARITY, List.of(0.92F), 0.8441029F, 2.0E-7F));

   public static float linkProbabilisticScore(
         final CustomDemographicData goldenRecord,
         final CustomDemographicData interaction) {
      // min, max, score, missingPenalty
      final float[] metrics = {0, 0, 0, 1.0F};
      LinkerProbabilistic.updateMetricsForStringField(metrics,
                                                      goldenRecord.fields[0].value(), interaction.fields[0].value(), currentLinkFields.givenName);
      LinkerProbabilistic.updateMetricsForStringField(metrics,
                                                      goldenRecord.fields[1].value(), interaction.fields[1].value(), currentLinkFields.familyName);
      LinkerProbabilistic.updateMetricsForStringField(metrics,
                                                      goldenRecord.fields[2].value(), interaction.fields[2].value(), currentLinkFields.gender);
      LinkerProbabilistic.updateMetricsForStringField(metrics,
                                                      goldenRecord.fields[3].value(), interaction.fields[3].value(), currentLinkFields.dob);
      LinkerProbabilistic.updateMetricsForStringField(metrics,
                                                      goldenRecord.fields[4].value(), interaction.fields[4].value(), currentLinkFields.city);
      LinkerProbabilistic.updateMetricsForStringField(metrics,
                                                      goldenRecord.fields[5].value(), interaction.fields[5].value(), currentLinkFields.phoneNumber);
      LinkerProbabilistic.updateMetricsForStringField(metrics,
                                                      goldenRecord.fields[6].value(), interaction.fields[6].value(), currentLinkFields.nationalId);
      return ((metrics[METRIC_SCORE] - metrics[METRIC_MIN]) / (metrics[METRIC_MAX] - metrics[METRIC_MIN])) * metrics[METRIC_MISSING_PENALTY];

   }

   public static float validateProbabilisticScore(
         final CustomDemographicData goldenRecord,
         final CustomDemographicData interaction) {
      return 0.0F;
   }

   static float matchNotificationProbabilisticScore(
         final CustomDemographicData goldenRecord,
         final CustomDemographicData interaction) {
      return 0.0F;
   }
   public static void updateMU(final CustomMU mu) {
      if (mu.givenName().m() > mu.givenName().u()
          && mu.familyName().m() > mu.familyName().u()
          && mu.gender().m() > mu.gender().u()
          && mu.dob().m() > mu.dob().u()
          && mu.city().m() > mu.city().u()
          && mu.phoneNumber().m() > mu.phoneNumber().u()
          && mu.nationalId().m() > mu.nationalId().u()) {
         updatedLinkFields = new LinkFields(
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
