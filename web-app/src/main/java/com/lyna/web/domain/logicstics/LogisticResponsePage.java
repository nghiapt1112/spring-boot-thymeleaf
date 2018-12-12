//package com.lyna.web.domain.logicstics;
//
//import com.lyna.commons.infrustructure.object.ResponsePage;
//import com.lyna.web.domain.view.LogisticAggregate;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class LogisticResponsePage extends ResponsePage<LogisticDTO, LogisticAggregate> {
//    private Collection<String> logisticIds;
//    private Collection<String> deliveryIds;
//
//    @Override
//    protected List<LogisticAggregate> parseResult(List<LogisticDTO> rawResults) {
//        List<LogisticAggregate> logisticAggregates = new ArrayList<>(rawResults.size());
//        logisticIds = new HashSet<>();
//        deliveryIds = new HashSet<>();
//        rawResults.stream()
//                .parallel()
//                .forEach(el -> {
//                    logisticAggregates.add(LogisticAggregate.fromLogisticDTO(el));
//                    logisticIds.add(el.getLogisticId());
//                    deliveryIds.add(el.getDeliveryId());
//                });
//
//        return logisticAggregates;
//    }
//
//    public Collection<String> getLogisticIds() {
//        return logisticIds;
//    }
//
//    public Collection<String> getDeliveryIds() {
//        return deliveryIds;
//    }
//}
