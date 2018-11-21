package com.lyna.web.domain.view;

import com.lyna.commons.infrustructure.object.AbstractObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class UserList extends AbstractObject {
    Map<String, Integer> mapStore;
    private String userId;
    private String email;
    private String name;
    private short role;
}
