package org.jembi.jempi.shared.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LinkEntitySyncBody(@JsonProperty("stan") String stan,
                                 @JsonProperty("externalLinkRange") ExternalLinkRange externalLinkRange,
                                 @JsonProperty("matchThreshold") Float matchThreshold,
                                 @JsonProperty("entity") CustomEntity entity) {}