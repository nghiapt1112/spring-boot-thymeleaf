package com.lyna.web.domain.trainningData;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "t_training_data")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
public class Training extends AbstractEntity {
    @Id
    @Column(name = "training_id", nullable = false)
    private String trainingId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "output_item")
    private String outputItemNums;

    @Column(name = "input_item")
    private String inputItemNums;

    public Training() {
        this.trainingId = UUID.randomUUID().toString();
    }
}
