package com.lyna.web.domain.stores;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.tenant.Tenant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "m_store")
@Data
public class Store extends AbstractEntity {
    @Id
    @Column(name = "store_id", nullable = false)
    private String storeId;

    @NotBlank(message = "code not empty server")
    @Column
    private String code;

    @NotBlank(message = "name not empty server")
    @Column
    private String name;

    @Column(name = "major_area")
    private String majorArea;

    @Column
    private String area;

    @Valid
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "store_id")
    private List<PostCourse> postCourses;

    public Store() {
        this.storeId = UUID.randomUUID().toString();
    }


}


