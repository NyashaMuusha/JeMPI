package org.jembi.jempi.api;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.Route;
import ch.megard.akka.http.cors.javadsl.settings.CorsSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jembi.jempi.AppConfig;
import org.jembi.jempi.libapi.ApiBase;
import org.jembi.jempi.libapi.BackEnd;
import org.jembi.jempi.shared.models.GlobalConstants;
import org.jembi.jempi.shared.models.RecordType;
import org.json.simple.JSONArray;

import java.util.regex.Pattern;

import static akka.http.javadsl.server.PathMatchers.segment;
import static ch.megard.akka.http.cors.javadsl.CorsDirectives.cors;

public final class HttpServer extends ApiBase {

   private static final Logger LOGGER = LogManager.getLogger(HttpServer.class);

   private HttpServer() {
   }

   static HttpServer create() {
      return new HttpServer();
   }

   /*
    *************************** ASK BACKEND ***************************
    */

/*
   private CompletionStage<BackEnd.GetGoldenRecordCountResponse> askGetGoldenRecordCount(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      CompletionStage<BackEnd.GetGoldenRecordCountResponse> stage = AskPattern
            .ask(backEnd,
                 BackEnd.GetGoldenRecordCountRequest::new,
                 java.time.Duration.ofSeconds(10),
                 actorSystem.scheduler());
      return stage.thenApply(response -> response);
   }
*/

/*
   private CompletionStage<BackEnd.GetInteractionCountResponse> askGetInteractionCount(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      LOGGER.debug("getInteractionCount");
      CompletionStage<BackEnd.GetInteractionCountResponse> stage = AskPattern
            .ask(backEnd,
                 BackEnd.GetInteractionCountRequest::new,
                 java.time.Duration.ofSeconds(10),
                 actorSystem.scheduler());
      return stage.thenApply(response -> response);
   }

   private CompletionStage<BackEnd.GetNumberOfRecordsResponse> askGetNumberOfRecords(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      LOGGER.debug("getNumberOfRecords");
      CompletionStage<BackEnd.GetNumberOfRecordsResponse> stage = AskPattern
            .ask(backEnd,
                 BackEnd.GetNumberOfRecordsRequest::new,
                 java.time.Duration.ofSeconds(10),
                 actorSystem.scheduler());
      return stage.thenApply(response -> response);
   }

   private CompletionStage<BackEnd.GetGoldenIdsResponse> askGetGoldenIds(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      LOGGER.debug("getGoldenIds");
      CompletionStage<BackEnd.GetGoldenIdsResponse> stage = AskPattern
            .ask(backEnd,
                 BackEnd.GetGoldenIdsRequest::new,
                 java.time.Duration.ofSeconds(30),
                 actorSystem.scheduler());
      return stage.thenApply(response -> response);
   }

   private CompletionStage<BackEnd.FindMatchesForReviewResponse> askFindMatchesForReview(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      CompletionStage<BackEnd.FindMatchesForReviewResponse> stage = AskPattern
            .ask(backEnd,
                 BackEnd.FindMatchesForReviewRequest::new,
                 java.time.Duration.ofSeconds(30),
                 actorSystem.scheduler());
      return stage.thenApply(response -> response);
   }

   private CompletionStage<BackEnd.FindExpandedGoldenRecordResponse> askFindExpandedGoldenRecord(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final String goldenId) {
      LOGGER.debug("findGoldenRecordById");
      final CompletionStage<BackEnd.FindExpandedGoldenRecordResponse> stage = AskPattern
            .ask(backEnd,
                 replyTo -> new BackEnd.FindExpandedGoldenRecordRequest(replyTo, goldenId),
                 java.time.Duration.ofSeconds(5),
                 actorSystem.scheduler());
      return stage.thenApply(response -> response);
   }
*/

//   private CompletionStage<BackEnd.FindInteractionResponse> askFindPatientRecord(
//         final ActorSystem<Void> actorSystem,
//         final ActorRef<BackEnd.Event> backEnd,
//         final String patientId) {
//      LOGGER.debug("findPatientRecordById : " + patientId);
//      final CompletionStage<BackEnd.FindInteractionResponse> stage = AskPattern
//            .ask(backEnd,
//                 replyTo -> new BackEnd.FindInteractionRequest(replyTo, patientId),
//                 java.time.Duration.ofSeconds(5),
//                 actorSystem.scheduler());
//      return stage.thenApply(response -> response);
//   }

/*
   private CompletionStage<BackEnd.GetPatientResourceResponse> askFindPatientResource(
           final ActorSystem<Void> actorSystem,
           final ActorRef<BackEnd.Event> backEnd,
           final String patientResourceId) {
      LOGGER.debug("findPatientRecordById : " + patientResourceId);
      final CompletionStage<BackEnd.GetPatientResourceResponse> stage = AskPattern
              .ask(backEnd,
                      replyTo -> new BackEnd.GetPatientResourceRequest(replyTo, patientResourceId),
                      java.time.Duration.ofSeconds(5),
                      actorSystem.scheduler());
      return stage.thenApply(response -> response);
   }
*/

/*
   private CompletionStage<BackEnd.FindCandidatesResponse> askFindCandidates(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final String patientId,
         final CustomMU mu) {
      LOGGER.debug("getCandidates");
      CompletionStage<BackEnd.FindCandidatesResponse> stage = AskPattern
            .ask(backEnd,
                 replyTo -> new BackEnd.FindCandidatesRequest(replyTo, patientId, mu),
                 java.time.Duration.ofSeconds(5),
                 actorSystem.scheduler());
      return stage.thenApply(response -> response);
   }

   private CompletionStage<BackEnd.FindExpandedGoldenRecordsResponse> askFindExpandedGoldenRecords(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final List<String> goldenIds) {
      LOGGER.debug("getExpandedGoldenRecords");
      CompletionStage<BackEnd.FindExpandedGoldenRecordsResponse> stage = AskPattern
            .ask(backEnd,
                 replyTo -> new BackEnd.FindExpandedGoldenRecordsRequest(replyTo, goldenIds),
                 java.time.Duration.ofSeconds(6),
                 actorSystem.scheduler());
      return stage.thenApply(response -> response);
   }

   private CompletionStage<BackEnd.FindExpandedPatientRecordsResponse> askFindExpandedPatientRecords(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final List<String> uidList) {
      LOGGER.debug("getExpandedPatients");
      CompletionStage<BackEnd.FindExpandedPatientRecordsResponse> stage = AskPattern
            .ask(backEnd,
                 replyTo -> new BackEnd.FindExpandedPatientRecordsRequest(replyTo, uidList),
                 java.time.Duration.ofSeconds(6),
                 actorSystem.scheduler());
      return stage.thenApply(response -> response);
   }

   private CompletionStage<BackEnd.UpdateGoldenRecordFieldsResponse> askUpdateGoldenRecordFields(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final String goldenId,
         final GoldenRecordUpdateRequestPayload payload) {
      LOGGER.debug("updateGoldenRecord");
      CompletionStage<BackEnd.UpdateGoldenRecordFieldsResponse> stage = AskPattern
            .ask(backEnd,
                 replyTo -> new BackEnd.UpdateGoldenRecordFieldsRequest(replyTo, goldenId, payload.fields()),
                 java.time.Duration.ofSeconds(6),
                 actorSystem.scheduler());
      return stage.thenApply(response -> response);
   }

   private CompletionStage<BackEnd.UpdateLinkToExistingGoldenRecordResponse> askUpdateLinkToExistingGoldenRecord(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final String currentGoldenId,
         final String newGoldenId,
         final String patientId,
         final Float score) {
      LOGGER.debug("patchLink");
      final CompletionStage<BackEnd.UpdateLinkToExistingGoldenRecordResponse> stage = AskPattern
            .ask(backEnd,
                 replyTo -> new BackEnd.UpdateLinkToExistingGoldenRecordRequest(replyTo,
                                                                                currentGoldenId,
                                                                                newGoldenId,
                                                                                patientId,
                                                                                score),
                 java.time.Duration.ofSeconds(6),
                 actorSystem.scheduler());
      return stage.thenApply(response -> response);
   }

   private CompletionStage<BackEnd.UpdateLinkToNewGoldenRecordResponse> askUpdateLinkToNewGoldenRecord(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final String currentGoldenId,
         final String patientId) {
      LOGGER.debug("patchUnLink");
      final CompletionStage<BackEnd.UpdateLinkToNewGoldenRecordResponse> stage = AskPattern
            .ask(backEnd,
                 replyTo -> new BackEnd.UpdateLinkToNewGoldenRecordRequest(replyTo, currentGoldenId, patientId, 2.0F),
                 java.time.Duration.ofSeconds(6),
                 actorSystem.scheduler());
      return stage.thenApply(response -> response);
   }

   private CompletionStage<ApiPaginatedResultSet> askSimpleSearchGoldenRecords(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final SimpleSearchRequestPayload searchRequestPayload) {
      CompletionStage<BackEnd.SearchGoldenRecordsResponse> stage = AskPattern
            .ask(backEnd,
                 replyTo -> new BackEnd.SimpleSearchGoldenRecordsRequest(replyTo, searchRequestPayload),
                 java.time.Duration.ofSeconds(11),
                 actorSystem.scheduler());
      return stage.thenApply(response -> ApiExpandedGoldenRecordsPaginatedResultSet.fromLibMPIPaginatedResultSet(response
      .records()));
   }

   private CompletionStage<ApiPaginatedResultSet> askSimpleSearchInteractions(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final SimpleSearchRequestPayload simpleSearchRequestPayload) {
      CompletionStage<BackEnd.SearchInteractionsResponse> stage = AskPattern
            .ask(backEnd,
                 replyTo -> new BackEnd.SimpleSearchInteractionsRequest(replyTo, simpleSearchRequestPayload),
                 java.time.Duration.ofSeconds(11),
                 actorSystem.scheduler());
      return stage.thenApply(response -> ApiPatientRecordsPaginatedResultSet.fromLibMPIPaginatedResultSet(response.records()));
   }

   private CompletionStage<ApiPaginatedResultSet> askCustomSearchGoldenRecords(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final CustomSearchRequestPayload customSearchRequestPayload) {
      CompletionStage<BackEnd.SearchGoldenRecordsResponse> stage = AskPattern
            .ask(backEnd,
                 replyTo -> new BackEnd.CustomSearchGoldenRecordsRequest(replyTo, customSearchRequestPayload),
                 java.time.Duration.ofSeconds(11),
                 actorSystem.scheduler());
      return stage.thenApply(response -> ApiExpandedGoldenRecordsPaginatedResultSet.fromLibMPIPaginatedResultSet(response
      .records()));
   }

   private CompletionStage<ApiPaginatedResultSet> askCustomSearchInteractions(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final CustomSearchRequestPayload customSearchRequestPayload) {
      CompletionStage<BackEnd.SearchInteractionsResponse> stage = AskPattern
            .ask(backEnd,
                 replyTo -> new BackEnd.CustomSearchInteractionsRequest(replyTo, customSearchRequestPayload),
                 java.time.Duration.ofSeconds(11),
                 actorSystem.scheduler());
      return stage.thenApply(response -> ApiPatientRecordsPaginatedResultSet.fromLibMPIPaginatedResultSet(response.records()));
   }

   private CompletionStage<BackEnd.UpdateNotificationStateRespnse> askUpdateNotificationState(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final NotificationRequest notificationRequest) {
      CompletionStage<BackEnd.UpdateNotificationStateRespnse> stage = AskPattern
            .ask(backEnd,
                 replyTo -> new BackEnd.UpdateNotificationStateRequest(replyTo,
                                                                       notificationRequest.notificationId(),
                                                                       notificationRequest.state()),
                 java.time.Duration.ofSeconds(11),
                 actorSystem.scheduler());
      return stage.thenApply(response -> response);
   }

*/
/*
   private CompletionStage<BackEnd.LoginWithKeycloakResponse> askLoginWithKeycloak(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final OAuthCodeRequestPayload body) {
      CompletionStage<BackEnd.LoginWithKeycloakResponse> stage = AskPattern
            .ask(backEnd,
                 replyTo -> new BackEnd.LoginWithKeycloakRequest(replyTo, body),
                 java.time.Duration.ofSeconds(11),
                 actorSystem.scheduler());
      return stage.thenApply(response -> response);
   }
*//*


   private CompletionStage<BackEnd.UploadCsvFileResponse> askUploadCsvFile(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final FileInfo info,
         final File file) {
      CompletionStage<BackEnd.UploadCsvFileResponse> stage = AskPattern
            .ask(backEnd,
                 replyTo -> new BackEnd.UploadCsvFileRequest(replyTo, info, file),
                 java.time.Duration.ofSeconds(11),
                 actorSystem.scheduler());
      return stage.thenApply(response -> response);
   }

   */
   /*
    *************************** PROXY ***************************
    *//*


   private CompletionStage<HttpResponse> proxyPostCalculateScores(final CalculateScoresRequest body) throws
   JsonProcessingException {
      final var request = HttpRequest
            .create("http://linker:50000/JeMPI/calculate-scores")
            .withMethod(HttpMethods.POST)
            .withEntity(ContentTypes.APPLICATION_JSON, AppUtils.OBJECT_MAPPER.writeValueAsBytes(body));
      final var stage = http.singleRequest(request);
      return stage.thenApply(response -> response);
   }

   */
   /*
    *************************** ROUTES ***************************
    *//*


   private Route mapError(final MpiGeneralError obj) {
      LOGGER.debug("{}", obj);
      return switch (obj) {
         case MpiServiceError.InteractionIdDoesNotExistError e -> complete(StatusCodes.BAD_REQUEST, e, Jackson.marshaller());
         case MpiServiceError.GoldenIdDoesNotExistError e -> complete(StatusCodes.BAD_REQUEST, e, Jackson.marshaller());
         case MpiServiceError.GoldenIdInteractionConflictError e -> complete(StatusCodes.BAD_REQUEST, e, Jackson.marshaller());
         case MpiServiceError.DeletePredicateError e -> complete(StatusCodes.BAD_REQUEST, e, Jackson.marshaller());
         default -> complete(StatusCodes.INTERNAL_SERVER_ERROR);
      };
   }

   private Route routeUpdateGoldenRecordFields(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final String goldenId) {
      return entity(Jackson.unmarshaller(GoldenRecordUpdateRequestPayload.class),
                    payload -> payload != null
                          ? onComplete(askUpdateGoldenRecordFields(actorSystem, backEnd, goldenId, payload),
                                       result -> {
                                          if (result.isSuccess()) {
                                             final var updatedFields = result.get().fields();
                                             if (updatedFields.size() == 0) {
                                                return complete(StatusCodes.BAD_REQUEST);
                                             } else {
                                                return complete(StatusCodes.OK, result.get(), Jackson.marshaller());
                                             }
                                          } else {
                                             return complete(StatusCodes.INTERNAL_SERVER_ERROR);
                                          }
                                       })
                          : complete(StatusCodes.NO_CONTENT));
   }

*/
/*
   private Route routeSessionUpdateGoldenRecordFields(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final String goldenId) {
      return requiredSession(refreshable, sessionTransport, session -> {
         if (session != null) {
            LOGGER.info("Current session: " + session.getEmail());
            return routeUpdateGoldenRecordFields(actorSystem, backEnd, goldenId);
         }
         LOGGER.info("No active session");
         return complete(StatusCodes.FORBIDDEN);
      });
   }
*//*


   private Route routeUpdateLinkToNewGoldenRecord(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      return parameter("goldenID",
                       currentGoldenId -> parameter("patientID",
                                                    patientId -> onComplete(askUpdateLinkToNewGoldenRecord(actorSystem,
                                                                                                           backEnd,
                                                                                                           currentGoldenId,
                                                                                                           patientId),
                                                                            result -> result.isSuccess()
                                                                                  ? result.get()
                                                                                          .linkInfo()
                                                                                          .mapLeft(this::mapError)
                                                                                          .fold(error -> error,
                                                                                                linkInfo -> complete
                                                                                                (StatusCodes.OK,
                                                                                                                     linkInfo,
                                                                                                                     Jackson
                                                                                                                     .marshaller()))
                                                                                  : complete(StatusCodes.IM_A_TEAPOT))));
   }

   private Route routeUpdateLinkToExistingGoldenRecord(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      return parameter("goldenID",
                       currentGoldenId ->
                             parameter("newGoldenID",
                                       newGoldenId ->
                                             parameter("patientID",
                                                       patientId ->
                                                             parameter("score",
                                                                       score -> onComplete(
                                                                             askUpdateLinkToExistingGoldenRecord(actorSystem,
                                                                                                                 backEnd,
                                                                                                                 currentGoldenId,
                                                                                                                 newGoldenId,
                                                                                                                 patientId,
                                                                                                                 Float.parseFloat(
                                                                                                                       score)),
                                                                             result -> result.isSuccess()
                                                                                   ? result.get()
                                                                                           .linkInfo()
                                                                                           .mapLeft(this::mapError)
                                                                                           .fold(error -> error,
                                                                                                 linkInfo -> complete(
                                                                                                       StatusCodes.OK,
                                                                                                       linkInfo,
                                                                                                       Jackson.marshaller()))
                                                                                   : complete(StatusCodes.IM_A_TEAPOT))))));
   }

   private Route routeGoldenRecordCount(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      return onComplete(askGetGoldenRecordCount(actorSystem, backEnd),
                        result -> result.isSuccess()
                              ? result.get()
                                      .count()
                                      .mapLeft(this::mapError)
                                      .fold(error -> error,
                                            count -> complete(StatusCodes.OK,
                                                              new ApiGoldenRecordCount(count),
                                                              Jackson.marshaller()))
                              : complete(StatusCodes.IM_A_TEAPOT));
   }

   private Route routeInteractionCount(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      return onComplete(askGetInteractionCount(actorSystem, backEnd),
                        result -> result.isSuccess()
                              ? result.get()
                                      .count()
                                      .mapLeft(this::mapError)
                                      .fold(error -> error,
                                            count -> complete(StatusCodes.OK,
                                                              new ApiPatientCount(count),
                                                              Jackson.marshaller()))
                              : complete(StatusCodes.IM_A_TEAPOT));
   }

   private Route routeNumberOfRecords(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      return onComplete(askGetNumberOfRecords(actorSystem, backEnd),
                        result -> result.isSuccess()
                              ? complete(StatusCodes.OK,
                                         new ApiNumberOfRecords(result.get().goldenRecords(), result.get().patientRecords()),
                                         Jackson.marshaller())
                              : complete(StatusCodes.IM_A_TEAPOT));
   }

   private Route routeGoldenIds(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      return onComplete(askGetGoldenIds(actorSystem, backEnd),
                        result -> result.isSuccess()
                              ? complete(StatusCodes.OK, result.get(), Jackson.marshaller())
                              : complete(StatusCodes.IM_A_TEAPOT));
   }

   private Route routeFindMatchesForReview(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      return onComplete(askFindMatchesForReview(actorSystem, backEnd),
                        result -> result.isSuccess()
                              ? complete(StatusCodes.OK, result.get(), Jackson.marshaller())
                              : complete(StatusCodes.IM_A_TEAPOT));
   }

   private Route routeGoldenRecord(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      return parameterList(params -> {
         final var goldenIds = params.stream().map(PARAM_STRING).toList();
         return onComplete(askFindExpandedGoldenRecords(actorSystem, backEnd, goldenIds),
                           result -> result.isSuccess()
                                 ? result.get()
                                         .expandedGoldenRecords()
                                         .mapLeft(this::mapError)
                                         .fold(error -> error,
                                               expandedGoldenRecords -> complete(StatusCodes.OK,
                                                                                 expandedGoldenRecords.stream()
                                                                                                      .map
                                                                                                      (ApiExpandedGoldenRecord::fromExpandedGoldenRecord)
                                                                                                      .toList(),
                                                                                 Jackson.marshaller()))
                                 : complete(StatusCodes.IM_A_TEAPOT));
      });
   }

   private Route routeExpandedGoldenRecords(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      return parameter("uidList", items -> {
         final var uidList = Stream.of(items.split(",")).map(String::trim).toList();
         return onComplete(
               askFindExpandedGoldenRecords(actorSystem, backEnd, uidList),
               result -> result.isSuccess()
                     ? result.get()
                             .expandedGoldenRecords()
                             .mapLeft(this::mapError)
                             .fold(error -> error,
                                   expandedGoldenRecords -> complete(StatusCodes.OK,
                                                                     expandedGoldenRecords.stream()
                                                                                          .map
                                                                                          (ApiExpandedGoldenRecord::fromExpandedGoldenRecord)
                                                                                          .toList(),
                                                                     Jackson.marshaller()))
                     : complete(StatusCodes.IM_A_TEAPOT));
      });
   }

   private Route routeExpandedPatientRecords(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      return parameter("uidList", items -> {
         final var uidList = Stream.of(items.split(",")).map(String::trim).toList();
         return onComplete(askFindExpandedPatientRecords(actorSystem, backEnd, uidList),
                           result -> result.isSuccess()
                                 ? result.get()
                                         .expandedPatientRecords()
                                         .mapLeft(this::mapError)
                                         .fold(error -> error,
                                               expandedPatientRecords -> complete(StatusCodes.OK,
                                                                                  expandedPatientRecords.stream()
                                                                                                        .map
                                                                                                        (ApiExpandedPatientRecord::fromExpandedPatientRecord)
                                                                                                        .toList(),
                                                                                  Jackson.marshaller()))
                                 : complete(StatusCodes.IM_A_TEAPOT));
      });
   }

   private Route routeFindExpandedGoldenRecord(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final String goldenId) {
      return onComplete(askFindExpandedGoldenRecord(actorSystem, backEnd, goldenId),
                        result -> result.isSuccess()
                              ? result.get()
                                      .goldenRecord()
                                      .mapLeft(this::mapError)
                                      .fold(error -> error,
                                            goldenRecord -> complete(StatusCodes.OK,
                                                                     ApiExpandedGoldenRecord.fromExpandedGoldenRecord
                                                                     (goldenRecord),
                                                                     Jackson.marshaller()))
                              : complete(StatusCodes.IM_A_TEAPOT));
   }

*/
/*
   private Route routeSessionFindExpandedGoldenRecord(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final String goldenId) {
      return requiredSession(refreshable,
                             sessionTransport,
                             session -> routeFindExpandedGoldenRecord(actorSystem, backEnd, goldenId));
   }
*//*


   private Route routeFindPatientRecord(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final String patientId) {
      return onComplete(askFindPatientRecord(actorSystem, backEnd, patientId),
                        result -> result.isSuccess()
                              ? result.get()
                                      .patient()
                                      .mapLeft(this::mapError)
                                      .fold(error -> error,
                                            patientRecord -> complete(StatusCodes.OK,
                                                                      ApiPatientRecord.fromPatientRecord(patientRecord),
                                                                      Jackson.marshaller()))
                              : complete(StatusCodes.IM_A_TEAPOT));
   }

*/
/*
   private Route routeSessionFindPatientRecord(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final String patientId) {
      return requiredSession(refreshable, sessionTransport, session -> routeFindPatientRecord(actorSystem, backEnd, patientId));
   }
*//*


    */
/*
   private Route routeGetPatientResource(
           final ActorSystem<Void> actorSystem,
           final ActorRef<BackEnd.Event> backEnd,
           final String patientResourceId) {
      return onComplete(askFindPatientResource(actorSystem, backEnd, patientResourceId),
              result -> result.isSuccess()
                      ? result.get()
                      .patientResource()
                      .mapLeft(this::mapError)
                      .fold(error -> error,
                              patientResource -> complete(StatusCodes.OK,
                                      patientResource
                              ))
                      : complete(StatusCodes.IM_A_TEAPOT));
   }
*//*


    */
/*
   private Route routeSessionGetPatientResource(
           final ActorSystem<Void> actorSystem,
           final ActorRef<BackEnd.Event> backEnd,
           final String patientResourceId) {
      return requiredSession(refreshable, sessionTransport, session -> routeGetPatientResource(actorSystem, backEnd,
      patientResourceId));
   }
*//*


   private Route routeFindCandidates(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      return parameter("uid",
                       patientId -> entity(Jackson.unmarshaller(CustomMU.class),
                                           mu -> onComplete(askFindCandidates(actorSystem, backEnd, patientId, mu),
                                                            result -> result.isSuccess()
                                                                  ? result.get()
                                                                          .candidates()
                                                                          .mapLeft(this::mapError)
                                                                          .fold(error -> error,
                                                                                candidateList -> complete(StatusCodes.OK,
                                                                                                          candidateList,
                                                                                                          Jackson.marshaller()))
                                                                  : complete(StatusCodes.IM_A_TEAPOT))));
   }

   private Route routeUpdateNotificationState(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      return entity(Jackson.unmarshaller(NotificationRequest.class),
                    obj -> onComplete(askUpdateNotificationState(actorSystem, backEnd, obj), response -> {
                       if (response.isSuccess()) {
                          final var updateResponse = response.get();
                          return complete(StatusCodes.OK, updateResponse, Jackson.marshaller());
                       } else {
                          return complete(StatusCodes.IM_A_TEAPOT);
                       }
                    }));
   }

*/
/*
   private Route routeLoginWithKeycloakRequest(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final CheckHeader<UserSession> checkHeader) {
      return entity(
            Jackson.unmarshaller(OAuthCodeRequestPayload.class),
            obj -> onComplete(askLoginWithKeycloak(actorSystem, backEnd, obj), response -> {
               if (response.isSuccess()) {
                  final var eventLoginWithKeycloakResponse = response.get();
                  final User user = eventLoginWithKeycloakResponse.user();
                  if (user != null) {
                     return setSession(refreshable,
                                       sessionTransport,
                                       new UserSession(user),
                                       () -> setNewCsrfToken(checkHeader,
                                                             () -> complete(StatusCodes.OK, user, Jackson.marshaller())));
                  } else {
                     return complete(StatusCodes.FORBIDDEN);
                  }
               } else {
                  return complete(StatusCodes.IM_A_TEAPOT);
               }
            }));
   }
*//*


    */
/*
   private Route routeCurrentUser() {
      return requiredSession(refreshable, sessionTransport, session -> {
         if (session != null) {
            LOGGER.info("Current session: " + session.getEmail());
            return complete(StatusCodes.OK, session, Jackson.marshaller());
         }
         LOGGER.info("No active session");
         return complete(StatusCodes.FORBIDDEN);
      });
   }
*//*


    */
/*
   private Route routeLogout() {
      return requiredSession(refreshable,
                             sessionTransport,
                             session -> invalidateSession(refreshable, sessionTransport, () -> extractRequestContext(ctx -> {
                                LOGGER.info("Logging out {}", session.getUsername());
                                return onSuccess(() -> ctx.completeWith(HttpResponse.create()),
                                                 routeResult -> complete("success"));
                             })));
   }
*//*


   private Route routeUploadCsvFile(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      return withSizeLimit(
            AppConfig.JEMPI_FILE_IMPORT_MAX_SIZE_BYTE,
            () -> storeUploadedFile("csv",
                                    (info) -> {
                                       try {
                                          LOGGER.debug(GlobalConstants.SEGMENT_UPLOAD);
                                          return File.createTempFile("import-", ".csv");
                                       } catch (Exception e) {
                                          LOGGER.error(e.getMessage(), e);
                                          return null;
                                       }
                                    },
                                    (info, file) -> onComplete(askUploadCsvFile(actorSystem, backEnd, info, file),
                                                               response -> response.isSuccess()
                                                                     ? complete(StatusCodes.OK)
                                                                     : complete(StatusCodes.IM_A_TEAPOT))));
   }

*/
/*
   private Route routeSessionUploadCsvFile(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd) {
      return withSizeLimit(AppConfig.JEMPI_FILE_IMPORT_MAX_SIZE_BYTE,
                           () -> requiredSession(refreshable, sessionTransport, session -> {
                              if (session != null) {
                                 LOGGER.info("Current session: " + session.getEmail());
                                 return storeUploadedFile("csv",
                                                          (info) -> {
                                                             try {
                                                                LOGGER.debug(GlobalConstants.SEGMENT_UPLOAD);
                                                                return File.createTempFile("import-", ".csv");
                                                             } catch (Exception e) {
                                                                LOGGER.error("error", e);
                                                                return null;
                                                             }
                                                          },
                                                          (info, file) -> onComplete(askUploadCsvFile(actorSystem, backEnd,
                                                                                                      info, file),
                                                                                     response -> response.isSuccess()
                                                                                           ? complete(StatusCodes.OK)
                                                                                           : complete(StatusCodes.IM_A_TEAPOT)));
                              }
                              LOGGER.info("No active session");
                              return complete(StatusCodes.FORBIDDEN);
                           }));
   }
*//*


   private Route routeSimpleSearch(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final RecordType recordType) {
      LOGGER.info("Simple search on {}", recordType);
      return entity(Jackson.unmarshaller(SimpleSearchRequestPayload.class),
                    searchParameters -> onComplete(
                          () -> {
                             if (recordType == RecordType.GoldenRecord) {
                                return askSimpleSearchGoldenRecords(actorSystem, backEnd, searchParameters);
                             } else {
                                return askSimpleSearchInteractions(actorSystem, backEnd, searchParameters);
                             }
                          },
                          response -> {
                             if (response.isSuccess()) {
                                final var eventSearchRsp = response.get();
                                return complete(StatusCodes.OK, eventSearchRsp, Jackson.marshaller());
                             } else {
                                return complete(StatusCodes.IM_A_TEAPOT);
                             }
                          }));
   }

*/

/*
   @Override
   public Route routeSimpleSearch(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final RecordType recordType) {
      return requiredSession(refreshable, sessionTransport, session -> super.routeSimpleSearch(actorSystem, backEnd, recordType));
   }
*/

/*
   private Route routeCustomSearch(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final RecordType recordType) {
      return entity(Jackson.unmarshaller(CustomSearchRequestPayload.class), searchParameters -> onComplete(() -> {
         if (recordType == RecordType.GoldenRecord) {
            return askCustomSearchGoldenRecords(actorSystem, backEnd, searchParameters);
         } else {
            return askCustomSearchInteractions(actorSystem, backEnd, searchParameters);
         }
      }, response -> {
         if (response.isSuccess()) {
            final var eventSearchRsp = response.get();
            return complete(StatusCodes.OK, eventSearchRsp, Jackson.marshaller());
         } else {
            return complete(StatusCodes.IM_A_TEAPOT);
         }
      }));
   }

   private Route routeCalculateScores() {
      return entity(Jackson.unmarshaller(CalculateScoresRequest.class),
                    obj -> {
                       try {
                          return onComplete(proxyPostCalculateScores(obj),
                                            response -> response.isSuccess()
                                                  ? complete(response.get())
                                                  : complete(StatusCodes.IM_A_TEAPOT));
                       } catch (JsonProcessingException e) {
                          LOGGER.error(e.getLocalizedMessage(), e);
                          return complete(StatusCodes.IM_A_TEAPOT);
                       }
                    });
   }

*/
/*
   private Route routeSessionCustomSearch(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final RecordType recordType) {
      return requiredSession(refreshable, sessionTransport, session -> {
         LOGGER.info("Custom search on {}", recordType);
         // Simple search for golden records
         return routeCustomSearch(actorSystem, backEnd, recordType);
      });
   }
*/

   private Route createJeMPIRoutes(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final JSONArray fields) {
      return concat(
            post(() -> concat(path(GlobalConstants.SEGMENT_UPDATE_NOTIFICATION,
                                   () -> routeUpdateNotificationState(actorSystem, backEnd)),
                              path(segment(GlobalConstants.SEGMENT_POST_SIMPLE_SEARCH).slash(segment(Pattern.compile(
                                         "^(golden|patient)$"))),
                                   (type) -> {
                                      final var t = type.equals("golden")
                                            ? RecordType.GoldenRecord
                                            : RecordType.Interaction;
                                      return routeSimpleSearch(actorSystem, backEnd, t);
                                   }),
                              path(segment(GlobalConstants.SEGMENT_POST_CUSTOM_SEARCH).slash(segment(Pattern.compile(
                                         "^(golden|patient)$"))),
                                   (type) -> {
                                      final var t = type.equals("golden")
                                            ? RecordType.GoldenRecord
                                            : RecordType.Interaction;
                                      return routeCustomSearch(actorSystem, backEnd, t);
                                   }),
                              path(GlobalConstants.SEGMENT_CALCULATE_SCORES, this::routeCalculateScores),
                              path(GlobalConstants.SEGMENT_UPLOAD, () -> routeUploadCsvFile(actorSystem, backEnd)))),
            patch(() -> concat(
                  path(segment(GlobalConstants.SEGMENT_UPDATE_GOLDEN_RECORD).slash(segment(Pattern.compile("^[A-z0-9]+$"))),
                       (goldenId) -> routeUpdateGoldenRecordFields(actorSystem, backEnd, goldenId)),
                  path(GlobalConstants.SEGMENT_CREATE_GOLDEN_RECORD,
                       () -> routeUpdateLinkToNewGoldenRecord(actorSystem, backEnd)),
                  path(GlobalConstants.SEGMENT_LINK_RECORD, () -> routeUpdateLinkToExistingGoldenRecord(actorSystem, backEnd)))),
            get(() -> concat(
/*
                  path(GlobalConstants.SEGMENT_CURRENT_USER, this::routeCurrentUser),
                  path(GlobalConstants.SEGMENT_LOGOUT, this::routeLogout),
*/
                      path(GlobalConstants.SEGMENT_COUNT_GOLDEN_RECORDS, () -> routeGoldenRecordCount(actorSystem, backEnd)),
                      path(GlobalConstants.SEGMENT_COUNT_PATIENT_RECORDS, () -> routeInteractionCount(actorSystem, backEnd)),
                      path(GlobalConstants.SEGMENT_COUNT_RECORDS, () -> routeNumberOfRecords(actorSystem, backEnd)),
                      path(GlobalConstants.SEGMENT_GOLDEN_IDS, () -> routeGoldenIds(actorSystem, backEnd)),
                      path(GlobalConstants.SEGMENT_GET_GOLDEN_ID_DOCUMENTS, () -> routeGoldenRecord(actorSystem, backEnd)),
                      path(GlobalConstants.SEGMENT_EXPANDED_GOLDEN_RECORDS, () -> routeExpandedGoldenRecords(actorSystem,
                                                                                                             backEnd)),
                      path(GlobalConstants.SEGMENT_EXPANDED_PATIENT_RECORDS, () -> routeExpandedPatientRecords(actorSystem,
                                                                                                               backEnd)),
                      path(GlobalConstants.SEGMENT_GET_NOTIFICATIONS, () -> routeFindMatchesForReview(actorSystem, backEnd)),
                      path(GlobalConstants.SEGMENT_CANDIDATE_GOLDEN_RECORDS, () -> routeFindCandidates(actorSystem, backEnd)),
                      path(segment(GlobalConstants.SEGMENT_PATIENT_RECORD_ROUTE).slash(segment(Pattern.compile("^[A-z0-9]+$"))),
                           (patientId) -> routeFindPatientRecord(actorSystem, backEnd, patientId)),
                      path(segment(GlobalConstants.SEGMENT_GOLDEN_RECORD_ROUTE).slash(segment(Pattern.compile("^[A-z0-9]+$"))),
                           (goldenId) -> routeFindExpandedGoldenRecord(actorSystem, backEnd, goldenId)),
                      path(GlobalConstants.SEGMENT_GET_FIELDS_CONFIG, () -> complete(StatusCodes.OK, fields.toJSONString())))
               ));
   }

   /*  private Route createRoutes(
           final ActorSystem<Void> actorSystem,
           final ActorRef<BackEnd.Event> backEnd) {
        return pathPrefix("JeMPI",
                          () -> pathPrefix("api",
                                           () -> createJeMPIRoutes(actorSystem, backEnd)));
     }
  */
   @Override
   protected Route createCorsRoutes(
         final ActorSystem<Void> actorSystem,
         final ActorRef<BackEnd.Event> backEnd,
         final JSONArray fields) {
      final var settings = CorsSettings.create(AppConfig.CONFIG);
      return cors(
            settings,
            () -> pathPrefix("JeMPI",
                             () -> createJeMPIRoutes(actorSystem, backEnd, fields)));
//                                   get(() -> path(GlobalConstants.SEGMENT_GET_FIELDS_CONFIG,
//                                                  () -> complete(StatusCodes.OK, fields.toJSONString()))))));
   }

}