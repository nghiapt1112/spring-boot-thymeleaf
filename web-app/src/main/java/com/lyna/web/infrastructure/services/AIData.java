package com.lyna.web.infrastructure.services;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.commons.utils.JsonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@lombok.Data
@lombok.NoArgsConstructor
public class AIData extends AbstractObject {

    private List<TrainingData> trainingDatas;
    private List<UnknownData> unknownDatas;

    public void parse(Map<String, List<Integer>> productAmountsByOrderId) {
        productAmountsByOrderId.forEach((orderId, v)-> {
            this.unknownDatas.add(new UnknownData(orderId, v));
        });
    }

    @Deprecated
    public void parse(List<List<Integer>> productAmount) {
        if (Objects.isNull(this.unknownDatas)) {
            this.unknownDatas = new ArrayList<>(productAmount.size());
        }
        for (List<Integer> el : productAmount) {
            this.unknownDatas.add(new UnknownData("keyu", el));
        }
    }

    private abstract class Data extends AbstractObject {
        public List<Integer> inputItemNums;
        public List<Integer> outputItemNums;
    }

    private class TrainingData extends Data {

    }

    private class UnknownData extends Data {
        private String key;

        public UnknownData(String keyu, List<Integer> el) {
            super();
            this.key = keyu;
            this.inputItemNums = el;
        }
    }

}

