package org.jembi.jempi.shared.models;


public record LinkingAuditEventData(
        String message, //todo: Alway need to be there
        String interaction_id,
        String goldenID,
        float score,
        LinkingRule linkingRule
) {

}