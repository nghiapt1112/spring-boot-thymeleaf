package com.lyna.web.domain.postCourse;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.tenant.Tenant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "m_post_course")
@Data
public class PostCourse extends AbstractEntity {

    @Id
    @Column(name="post_course_id", nullable = false)
    private String postCourseId;

    @NotBlank(message = "'便'は必須です。")
    @Column(name="post")
    public String post;

    @Column(name="course")
    private String course;

    @Column(name = "store_id")
    private String storeId;


    public PostCourse(){
        this.postCourseId = UUID.randomUUID().toString();
    }
}