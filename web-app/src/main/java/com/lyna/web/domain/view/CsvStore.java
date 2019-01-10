package com.lyna.web.domain.view;

import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.user.User;
import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class CsvStore {
    @CsvBindByName(column = "店舗コード")
    private String storeCode;

    @CsvBindByName(column = "店舗名")
    private String storeName;

    @CsvBindByName(column = "住所")
    private String address;

    @CsvBindByName(column = "電話番号")
    private String phoneNumber;

    @CsvBindByName(column = "担当者")
    private String personInCharge;

    @CsvBindByName(column = "大エリア")
    private String majorArea;

    @CsvBindByName(column = "エリア")
    private String area;

    @CsvBindByName(column = "便")
    private String post;

    @CsvBindByName(column = "コース")
    private String course;

    public Store createStore(User user) {
        Store store = new Store();
        store.setCode(this.getStoreCode());
        store.setName(this.getStoreName());
        store.setAddress(this.getAddress());
        store.setArea(this.getArea());
        store.setMajorArea(this.getMajorArea());
        store.setPersonCharge(this.getPersonInCharge());
        store.setPhoneNumber(this.getPhoneNumber());
        store.setPostCourses(store.getPostCourses());
        store.setTenantId(user.getTenantId());
        store.setCreateUser(user.getId());
        store.setCreateDate(new Date());
        return store;
    }
}
