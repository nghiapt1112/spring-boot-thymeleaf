package com.lyna.web.domain.AI;

import com.lyna.commons.infrustructure.object.AbstractObject;
import lombok.NoArgsConstructor;

import java.util.List;

public abstract class Data extends AbstractObject {
    public List<Integer> inputItemNums;
    public List<Integer> outputItemNums;
}

@NoArgsConstructor
class TrainingData extends Data {

}

@NoArgsConstructor
class UnknownData extends Data {
    private String key;

    public UnknownData(String key, List<Integer> el) {
        super();
        this.key = key;
        this.inputItemNums = el;
    }

    public String getOrderId() {
        return key;
    }
}