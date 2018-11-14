package com.lyna.web.domain.mpackage;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "m_post_course")
@NamedQueries({
        @NamedQuery(name = "PostCourse.countAll", query = "SELECT COUNT(x) FROM PostCourse x")
})
@Data
@NoArgsConstructor
public class PostCourse extends AbstractEntity {
    @Id
    @Column(name = "post_course_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String postCourseId;

    @Column
    public String post;

    @Column
    public String course;

}