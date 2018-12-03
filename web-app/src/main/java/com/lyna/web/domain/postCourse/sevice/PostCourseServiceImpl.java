package com.lyna.web.domain.postCourse.sevice;

import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.postCourse.repository.PostCourseRepository;
import com.lyna.web.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class PostCourseServiceImpl implements PostCourseService {
    @Autowired
    private PostCourseRepository postCourseRepository;


    @Override
    @Transactional
    public void updatePostCourse(List<PostCourse> postCourses, UsernamePasswordAuthenticationToken principal, String storeId) {
        User currentUser = (User) principal.getPrincipal();
        int tenantId = currentUser.getTenantId();
        String userId = currentUser.getId();
        Date date = new Date();
        for (PostCourse postCourse : postCourses) {
            if (!Objects.isNull(postCourse.getStoreId()) && !postCourse.getStoreId().isEmpty()) {
                postCourse.setTenantId(tenantId);
                postCourse.setUpdateDate(date);
                postCourse.setUpdateUser(userId);
                postCourseRepository.updatePostCourse(postCourse);

            } else {
                postCourse.setTenantId(tenantId);
                postCourse.setStoreId(storeId);
                postCourse.setCreateDate(date);
                postCourse.setCreateUser(userId);
                postCourseRepository.save(postCourse);
             }

        }
    }

    @Override
    public List<PostCourse> findAllByStoreId(String storeId) {
        return postCourseRepository.findAllByStoreId(storeId);
    }
}
