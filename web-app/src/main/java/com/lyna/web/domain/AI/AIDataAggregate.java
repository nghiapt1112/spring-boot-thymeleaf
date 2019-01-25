package com.lyna.web.domain.AI;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lyna.commons.infrustructure.object.AbstractObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@lombok.Data
@lombok.NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AIDataAggregate extends AbstractObject {

    private List<TrainingData> trainingDatas;
    private List<UnknownData> unknownDatas;
    private List<UnknownData> resultDatas;

    private int code;
    private String message;


    public AIDataAggregate(Map<String, List<Integer>> productAmountsByOrderId, List<TrainingData> trainingDatas) {
        if (Objects.isNull(this.unknownDatas)) {
            this.unknownDatas = new ArrayList<>();
        }
        productAmountsByOrderId.forEach((orderId, v) ->
                this.unknownDatas.add(new UnknownData(orderId, v))
        );

        if (Objects.isNull(this.trainingDatas)) {
            this.trainingDatas = new ArrayList<>();
        }
        this.trainingDatas.addAll(trainingDatas);
    }

}