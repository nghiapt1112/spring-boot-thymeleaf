package com.lyna.web.domain.AI;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lyna.commons.infrustructure.object.AbstractObject;

import java.util.List;
import java.util.Map;

@lombok.Data
@lombok.NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AIDataAggregate extends AbstractObject {

    private List<TrainingData> trainingDatas;
    private List<UnknownData> unknownDatas;
    private List<UnknownData> resultDatas;

    // parse request
    public AIDataAggregate(Map<String, List<Integer>> productAmountsByOrderId) {
        productAmountsByOrderId.forEach((orderId, v) ->
            this.unknownDatas.add(new UnknownData(orderId, v))
        );
    }

}