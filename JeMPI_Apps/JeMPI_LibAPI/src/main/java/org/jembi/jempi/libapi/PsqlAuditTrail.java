package org.jembi.jempi.libapi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jembi.jempi.shared.models.ApiModels;
import org.jembi.jempi.shared.models.AuditEvent;
import org.jembi.jempi.shared.models.GlobalConstants;
import org.jembi.jempi.shared.models.LinkingAuditEventData;
import org.jembi.jempi.shared.utils.AuditTrailUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import static org.jembi.jempi.shared.models.GlobalConstants.PSQL_TABLE_AUDIT_TRAIL;

final class PsqlAuditTrail {
   private static final Logger LOGGER = LogManager.getLogger(PsqlAuditTrail.class);
   private final PsqlClient psqlClient;

   PsqlAuditTrail(
         final String pgServer,
         final int pgPort,
         final String pgDatabase,
         final String pgUser,
         final String pgPassword) {
      psqlClient = new PsqlClient(pgServer, pgPort, pgDatabase, pgUser, pgPassword);
   }

   List<ApiModels.ApiAuditTrail.LinkingAuditEntry> goldenRecordAuditTrail(final String uid) {
      psqlClient.connect();
      final var list = new ArrayList<ApiModels.ApiAuditTrail.LinkingAuditEntry>();
      //todo: improve query later
      try (PreparedStatement preparedStatement = psqlClient.prepareStatement(String.format(Locale.ROOT,
                                                                                           """
                                                                                           SELECT * FROM %s where event = %s
                                                                                           AND where eventData like '%%%s%%';
                                                                                           """,
                                                                                           PSQL_TABLE_AUDIT_TRAIL,
                                                                                           GlobalConstants.AuditEventType.LINKING_EVENT,
                                                                                          uid
                                                                                           )
                                                                                   .stripIndent())) {
         preparedStatement.setString(1, uid);
         ResultSet rs = preparedStatement.executeQuery();
         while (rs.next()) {
            final var insertTime = rs.getString(2);
            final var createdTime = rs.getString(3);
            final var eventData = rs.getString(4);
            //todo:Validate
            LinkingAuditEventData deserializeEventData = AuditTrailUtil.getDeserializeEventData(eventData, LinkingAuditEventData.class);
            if (!Objects.equals(deserializeEventData.goldenID(), uid)) {
               throw new Exception("Something went wrong");
            }
            list.add(new ApiModels.ApiAuditTrail.LinkingAuditEntry(
                    insertTime,
                    createdTime,
                    deserializeEventData.interaction_id(),
                    deserializeEventData.goldenID(),
                    deserializeEventData.message(),
                    deserializeEventData.score(),
                    deserializeEventData.linkingRule().name()
            ));
         }
      } catch (Exception e) {
         LOGGER.error(e);
      }
      return list;
   }

   List<AuditEvent> interactionRecordAuditTrail(final String uid) {
      psqlClient.connect();
      final var list = new ArrayList<AuditEvent>();
      try (PreparedStatement preparedStatement = psqlClient.prepareStatement(String.format(Locale.ROOT,
                                                                                           """
                                                                                           SELECT * FROM %s where interactionID = ?;
                                                                                           """,
                                                                                           PSQL_TABLE_AUDIT_TRAIL)
                                                                                   .stripIndent())) {
         preparedStatement.setString(1, uid);
         ResultSet rs = preparedStatement.executeQuery();
         while (rs.next()) {
            final var insertedAt = rs.getTimestamp(2);
            final var createdAt = rs.getTimestamp(3);
            final var interactionID = rs.getString(4);
            final var goldenID = rs.getString(5);
            final var event = rs.getString(6);
            final var eventData = rs.getString(7);
            final var eventType = Optional.ofNullable(rs.getString(8));

            final var auditEvent = new AuditEventOld(createdAt, insertedAt, interactionID, goldenID, event);
            final var auditEventType = eventType.map(AuditEventType::valueOf).orElse(AuditEventType.UNKNOWN_EVENT);
            list.add(new AuditEvent(auditEvent, auditEventType, eventData));
         }
      } catch (Exception e) {
         LOGGER.error(e);
      }
      return list;
   }

}
