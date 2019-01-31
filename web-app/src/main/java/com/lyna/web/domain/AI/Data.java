package com.lyna.web.domain.AI;

import com.lyna.commons.infrustructure.object.AbstractObject;

import java.math.BigDecimal;
import java.util.List;

@lombok.Data
public abstract class Data extends AbstractObject {
    protected List<Integer> inputItemNums;
    protected List<Integer> outputItemNums;
}

