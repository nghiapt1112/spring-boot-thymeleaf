package com.lyna.web.infrastructure.object;

import com.lyna.commons.infrustructure.object.AbstractObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public abstract class RequestPage extends AbstractObject {
    private int noOfRowInPage;
    private int currentPage;
    private Map<String, String> sortFields;
    private Map<String, String> searchFields;

}
