package com.lyna.web.infrastructure.services;

import com.lyna.commons.infrustructure.object.AbstractObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@lombok.Data
public class AIData extends AbstractObject {

    private List<TrainingData> trainingDatas;
    private List<UnknownData> unknownDatas;

    public void parse(List<List<Integer>> productAmount) {
        if (Objects.isNull(this.unknownDatas)) {
            this.unknownDatas = new ArrayList<>(productAmount.size());
        }
        UnknownData data = new UnknownData();
//        data.inputItemNums = productAmount;
    }

    private abstract class Data extends AbstractObject {
        public List<Integer> inputItemNums;
        public List<Integer> outputItemNums;
    }

    private class TrainingData extends Data {

    }

    private class UnknownData extends Data {

    }

}

