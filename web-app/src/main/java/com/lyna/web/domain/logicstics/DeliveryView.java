package com.lyna.web.domain.logicstics;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "v_delivery")
@Data
@NoArgsConstructor
public class DeliveryView extends MainMenuView {
    @Id
    @Column
    private String delivery_detail_id;

    public boolean isPackageNameNonNull() {
        return Objects.nonNull(packageName);
    }
}
