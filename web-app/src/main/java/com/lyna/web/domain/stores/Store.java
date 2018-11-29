package com.lyna.web.domain.stores;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.web.domain.postCourse.PostCourse;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "m_store")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
        @NamedQuery(name = "Store.getAll", query = "SELECT c FROM Store c WHERE c.tenantId = :tenantId ORDER BY c.name")
})
@Data
public class Store extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column
    private String address;

    @Column(name = "person_in_charge")
    private String personCharge;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Valid
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id")
    private List<PostCourse> postCourses;

    public Store() {
        this.storeId = UUID.randomUUID().toString();
    }
}

