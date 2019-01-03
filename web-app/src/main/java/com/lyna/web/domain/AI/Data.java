package com.lyna.web.domain.AI;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.web.domain.mpackage.Package;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@lombok.Data
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

    /**
     * create a map: packageId <-> amount
     *
     */
    public Map<String, Integer> toMap(List<Package> pkgsInTenant) {
        Map<String, Integer> amountByPgkId = new HashMap<>();
        for (int i = 0; i < this.outputItemNums.size(); i++) {
            String k = pkgsInTenant.get(i).getPackageId();
            Integer v = this.outputItemNums.get(i); // nhung cai bang 0 thi khong can cho vao nhi?
            amountByPgkId.put(k, v);
        }
        return amountByPgkId;
    }

}