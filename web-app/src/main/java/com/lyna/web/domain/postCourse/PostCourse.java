package com.lyna.web.domain.postCourse;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "m_post_course")
@Data
public class PostCourse extends AbstractEntity {

    @NotBlank(message = "'便'は必須です。")
    @Column(name = "post")
    public String post;
    @Id
    @Column(name = "post_course_id", nullable = false)
    private String postCourseId;
    @Column(name = "course")
    private String course;

    @Column(name = "store_id")
    private String storeId;


    public PostCourse() {
        this.postCourseId = UUID.randomUUID().toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostCourse that = (PostCourse) o;
        return Objects.equals(postCourseId, that.postCourseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postCourseId);
    }
}