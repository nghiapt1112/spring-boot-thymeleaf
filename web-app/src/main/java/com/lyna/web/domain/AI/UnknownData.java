package com.lyna.web.domain.AI;

import com.lyna.web.domain.mpackage.Package;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class UnknownData extends Data {
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
     */
    public Map<String, Integer> toMap(List<Package> pkgsInTenant) {
        Map<String, Integer> amountByPgkId = new HashMap<>();
        for (int i = 0; i < this.outputItemNums.size(); i++) {
            String k = pkgsInTenant.get(i).getPackageId();
            Integer v = this.outputItemNums.get(i);
            amountByPgkId.put(k, v);
        }
        return amountByPgkId;
    }

}