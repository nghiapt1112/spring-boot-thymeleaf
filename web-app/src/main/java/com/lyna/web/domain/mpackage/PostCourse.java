package com.lyna.web.domain.mpackage;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "m_post_course")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
        @NamedQuery(name = "PostCourse.countAll", query = "SELECT COUNT(x) FROM PostCourse x")
})
public class PostCourse implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "post_course_id", nullable = false, length = 36)
    public String postCourseId;

    @Basic
    @Column(name = "post", nullable = true, length = 255)
    public String post;


    @Basic
    @Column(name = "course", nullable = true, length = 255)
    public String course;


    @Basic
    @Column(name = "create_date", nullable = true)
    public Timestamp createDate;


    @Id
    @Column(name = "create_user", nullable = false, length = 36)
    public String createUser;


    @Basic
    @Column(name = "update_date", nullable = true)
    public Timestamp updateDate;


    @Basic
    @Column(name = "update_user", nullable = true, length = 36)
    public String updateUser;

    public String getPostCourseId() {
        return postCourseId;
    }

    public void setPostCourseId(String postCourseId) {
        this.postCourseId = postCourseId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
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
    public String toString() {
        return "PostCourse{" +
                "postCourseId='" + postCourseId + '\'' +
                ", post='" + post + '\'' +
                ", course='" + course + '\'' +
                ", createDate=" + createDate +
                ", createUser='" + createUser + '\'' +
                ", updateDate=" + updateDate +
                ", updateUser='" + updateUser + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostCourse)) return false;
        PostCourse that = (PostCourse) o;
        return Objects.equals(postCourseId, that.postCourseId) &&
                Objects.equals(post, that.post) &&
                Objects.equals(course, that.course) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(createUser, that.createUser) &&
                Objects.equals(updateDate, that.updateDate) &&
                Objects.equals(updateUser, that.updateUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postCourseId, post, course, createDate, createUser, updateDate, updateUser);
    }
}
