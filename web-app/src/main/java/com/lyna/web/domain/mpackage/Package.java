package com.lyna.web.domain.mpackage;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.commons.utils.NumberUtils;
import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.logicstics.LogiticsDetail;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "m_package")
@NamedQueries({
        @NamedQuery(name = "Package.getAll", query = "SELECT x FROM Package x")
})
@Data
public class Package extends AbstractEntity {

    @Id
    @Column(name = "package_id", nullable = false)
    public String packageId;

    @NotBlank(message = "'荷姿名'は必須です。")
    @Column
    private String name;

    @Column
    private String unit;

    @Column(name = "empty_weight")
    private BigDecimal emptyWeight;

    @Column(name = "full_load_weight")
    private BigDecimal fullLoadWeight;

    @Column(name = "empty_capacity")
    private BigDecimal emptyCapacity;

    @Column(name = "full_load_capacity")
    private BigDecimal fullLoadCapacity;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "package_id")
    private Set<DeliveryDetail> deliveryDetails;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "package_id")
    private Set<LogiticsDetail> logiticsDetails;


    public Package() {
        this.packageId = UUID.randomUUID().toString();
    }
	
	public BigDecimal getEmptyWeight() {
        return NumberUtils.removeTrailingZero(emptyWeight);
    }

    public void setEmptyWeight(BigDecimal emptyWeight) {
        this.emptyWeight = NumberUtils.removeTrailingZero(emptyWeight);;
    }

    public BigDecimal getFullLoadWeight() {
        return NumberUtils.removeTrailingZero(fullLoadWeight);
    }

    public void setFullLoadWeight(BigDecimal fullLoadWeight) {
        this.fullLoadWeight = NumberUtils.removeTrailingZero(fullLoadWeight);
    }

    public BigDecimal getEmptyCapacity() {
        return NumberUtils.removeTrailingZero(emptyCapacity);
    }

    public void setEmptyCapacity(BigDecimal emptyCapacity) {
        this.emptyCapacity = NumberUtils.removeTrailingZero(emptyCapacity);
    }

    public BigDecimal getFullLoadCapacity() {
        return NumberUtils.removeTrailingZero(fullLoadCapacity);
    }

    public void setFullLoadCapacity(BigDecimal fullLoadCapacity) {
        this.fullLoadCapacity = NumberUtils.removeTrailingZero(fullLoadCapacity);
    }
}