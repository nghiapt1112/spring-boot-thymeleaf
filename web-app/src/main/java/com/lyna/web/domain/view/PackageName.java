package com.lyna.web.domain.view;

import com.lyna.commons.infrustructure.object.AbstractObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageName extends AbstractObject {
    private String name;
    private BigDecimal amount;
}
