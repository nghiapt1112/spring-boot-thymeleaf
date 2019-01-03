package com.lyna.web.domain.AI;

import com.lyna.web.domain.user.User;

import java.util.Collection;
import java.util.List;

public interface AIService {
    void calculateLogisticsWithAI(User currentUser, Collection<String> csvOrderIds);

    void updateDataToDB(User currentUser, List<UnknownData> resultDatas);
}
