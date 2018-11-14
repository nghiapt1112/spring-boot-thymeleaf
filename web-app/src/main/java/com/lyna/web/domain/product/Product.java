package com.lyna.web.domain.product;

import com.lyna.web.infrastructure.entity.AbstractEntity;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "m_product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
        @NamedQuery(name = "Product.countAll", query = "SELECT COUNT(x) FROM Product x")
})
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Product_SEQ")
    @SequenceGenerator(sequenceName = "Product_SEQ", schema = "PACKAGE", initialValue = 1, allocationSize = 1, name = "Product_SEQ")
    @Column(name = "productId", nullable = false, length = 36)
    private String productId;
    @Column(name = "code", nullable = true, length = 50)

    private String code;
    @Column(name = "name", nullable = true, length = 255)

    private String name;
    @Column(name = "unit　", nullable = true, length = 50)

    private String unit;
    @Column(name = "price", nullable = true, precision = 2)

    private BigDecimal price;
    @Column(name = "category1", nullable = true, length = 255)

    private String category1;
    @Column(name = "category2", nullable = true, length = 255)

    private String category2;
    @Column(name = "category3", nullable = true, length = 255)

    private String category3;
    @Column(name = "temperature", nullable = true, length = 255)

    private String temperature;
    @Column(name = "create_date", nullable = true)

    private Timestamp createDate;
    @Column(name = "create_user", nullable = true, length = 36)

    private String createUser;
    @Column(name = "update_date", nullable = true)

    private Timestamp updateDate;
    @Column(name = "update_user", nullable = true, length = 36)

    private String updateUser;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getCategory3() {
        return category3;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId) &&
                Objects.equals(code, product.code) &&
                Objects.equals(name, product.name) &&
                Objects.equals(unit, product.unit) &&
                Objects.equals(price, product.price) &&
                Objects.equals(category1, product.category1) &&
                Objects.equals(category2, product.category2) &&
                Objects.equals(category3, product.category3) &&
                Objects.equals(temperature, product.temperature) &&
                Objects.equals(createDate, product.createDate) &&
                Objects.equals(createUser, product.createUser) &&
                Objects.equals(updateDate, product.updateDate) &&
                Objects.equals(updateUser, product.updateUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, code, name, unit, price, category1, category2, category3, temperature, createDate, createUser, updateDate, updateUser);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", price=" + price +
                ", category1='" + category1 + '\'' +
                ", category2='" + category2 + '\'' +
                ", category3='" + category3 + '\'' +
                ", temperature='" + temperature + '\'' +
                ", createDate=" + createDate +
                ", createUser='" + createUser + '\'' +
                ", updateDate=" + updateDate +
                ", updateUser='" + updateUser + '\'' +
                '}';
    }
}
