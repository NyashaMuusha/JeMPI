package org.jembi.jempi.linker.linker_processor.processors.TPTNProcessor;

import org.jembi.jempi.AppConfig;
import org.jembi.jempi.libmpi.LibMPI;
import org.jembi.jempi.linker.linker_processor.lib.CategorisedCandidates;
import org.jembi.jempi.linker.linker_processor.lib.range_type.RangeTypeName;
import org.jembi.jempi.linker.linker_processor.processors.*;
import org.jembi.jempi.shared.libs.tptn.TPTNAccessor;
import org.jembi.jempi.shared.libs.tptn.TPTNKGlobalStoreInstance;
import org.jembi.jempi.shared.libs.tptn.TPTNMatrix;
import org.jembi.jempi.shared.models.Interaction;
import org.jembi.jempi.shared.models.NotificationResolutionProcessorData;
import org.jembi.jempi.shared.models.dashboard.TPTNFScore;
import org.jembi.jempi.shared.models.dashboard.TPTNStats;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public final class TPTNProcessor extends SubProcessor implements IThresholdRangeSubProcessor, IOnNotificationResolutionProcessor, IDashboardDataProducer<TPTNStats> {

    private float getFScoreType(final String type, final TPTNMatrix tptnMatrix) {
        float beta;

        if (Objects.equals(type, "recall")) {
            beta = 2.0F;
        } else if (Objects.equals(type, "recall_precision")) {
            beta = 1.0F;
        } else if (Objects.equals(type, "precision")) {
            beta = 0.5F;
        } else {
            throw new RuntimeException("Unknown f-score type");
        }

        return (1 + beta) * tptnMatrix.getTruePositive() / (((1 + beta) * tptnMatrix.getTruePositive()) + (beta * tptnMatrix.getFalseNegative()) + tptnMatrix.getFalsePositive());
    }
    public TPTNStats getDashboardData(final LibMPI libMPI) throws Exception {
        TPTNKGlobalStoreInstance store = getStore();
        TPTNMatrix currentMatrix = store.getValue();
        return new TPTNStats(currentMatrix, new TPTNFScore(getFScoreType("recall", currentMatrix),
                                                           getFScoreType("recall_precision", currentMatrix),
                                                           getFScoreType("precision", currentMatrix)));
    }

    @Override
    public String getDashboardDataName() {
        return "tptn";
    }

    @Override
    public boolean processOnNotificationResolution(final NotificationResolutionProcessorData notificationResolutionProcessorData, final LibMPI libMPI) {
        return false;
    }

    private TPTNKGlobalStoreInstance getStore() throws ExecutionException, InterruptedException {
        return TPTNAccessor.getKafkaTPTNUpdater(this.linkerId, AppConfig.KAFKA_BOOTSTRAP_SERVERS);
    }
    @Override
    public Boolean processCandidates(final List<CategorisedCandidates> candidates) throws ExecutionException, InterruptedException {
        TPTNMatrix tptnMatrix = new TPTNMatrix();
        TPTNKGlobalStoreInstance store = getStore();

        List<CategorisedCandidates> candidateOutsideNotiWindow = candidates.stream()
                .filter(candidate -> !(candidate.isRangeApplicable(RangeTypeName.NOTIFICATION_RANGE_BELOW_THRESHOLD)
                        || candidate.isRangeApplicable(RangeTypeName.NOTIFICATION_RANGE_ABOVE_THRESHOLD))).toList();

        tptnMatrix.updateTruePositive(candidateOutsideNotiWindow.stream().filter(c -> c.isRangeApplicable(RangeTypeName.ABOVE_THRESHOLD)).count());
        tptnMatrix.updateTrueNegative(candidateOutsideNotiWindow.stream().filter(c -> !c.isRangeApplicable(RangeTypeName.ABOVE_THRESHOLD)).count());

        store.updateValue(tptnMatrix);
        return true;
    }

    @Override
    public IThresholdRangeSubProcessor setOriginalInteraction(final Interaction originalInteractionIn) {
        return null;
    }
}
