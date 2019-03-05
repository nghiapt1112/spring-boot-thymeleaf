package com.lyna.web.domain.trainningData.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.stores.exception.StoreException;
import com.lyna.web.domain.trainningData.Training;
import com.lyna.web.domain.trainningData.repository.TrainingRepository;
import com.lyna.web.domain.trainningData.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TrainingServiceImpl extends BaseService implements TrainingService {
    @Autowired
    TrainingRepository trainingRepository;

    @Override
    @Transactional
    public void saveMap(Map<String, Map<String, BigDecimal>> mapOrderIdProductIdAmount, Map<String, Map<String, BigDecimal>> mapOrderPackageIdAmount, int tenantId, String userId) {

        List<String> orderIds = mapOrderIdProductIdAmount.keySet().stream().collect(Collectors.toList());
        List<Training> trainings = trainingRepository.findByOrderIdTenantId(orderIds, tenantId);
        Map<String, Training> mapTraining = trainings.stream().collect(Collectors.toMap(p -> p.getOrderId(), p -> p));

        Set<Training> setTraining = new HashSet<>();
        mapOrderIdProductIdAmount.forEach((orderId, mapProduct) -> {
            Training trainingData = mapTraining.get(orderId);
            ObjectMapper mapper = new ObjectMapper();
            String jsonInputItems;
            String jsonOutputItems;
            try {
                jsonInputItems = mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(mapProduct);

                jsonOutputItems = mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(mapOrderPackageIdAmount.get(orderId));
            } catch (JsonProcessingException e) {
                throw new StoreException(503, "データトレーニング文字列からJsonまで変換するときにエラが発生されました。");
            }

            if (trainingData != null) {
                trainingData.setInputItemNums(jsonInputItems);
                trainingData.setOutputItemNums(jsonOutputItems);
                trainingData.setUpdateDate(new Date());
                trainingData.setUpdateUser(userId);
                setTraining.add(trainingData);
            } else {
                Training training = new Training();
                training.setOrderId(orderId);
                training.setInputItemNums(jsonInputItems);
                training.setOutputItemNums(jsonOutputItems);
                training.setTenantId(tenantId);
                training.setCreateDate(new Date());
                training.setCreateUser(userId);
                setTraining.add(training);
            }
        });

        trainingRepository.saveAll(setTraining);
    }

    @Override
    public List<Training> findAllByTenantId(int tenantId) {
        return trainingRepository.findByTenantId(tenantId);
    }
}
