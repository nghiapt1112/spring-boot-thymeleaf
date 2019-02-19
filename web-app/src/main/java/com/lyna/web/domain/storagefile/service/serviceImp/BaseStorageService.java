package com.lyna.web.domain.storagefile.service.serviceImp;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.postCourse.repository.PostCourseRepository;
import com.lyna.web.domain.view.CsvOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class BaseStorageService extends BaseService {

    private Set<PostCourse> postCoursesIterable;
    private Map<Integer, String> mapError;
    private Map<Object, String> mapCsvPostCourseId;
    private Map<String, String> setStoreCodePost;

    @Autowired
    private PostCourseRepository postCourseRepository;

    public Map<Object, String> getMapCsvPostCourseId() {
        return mapCsvPostCourseId;
    }

    public String getMapCsvPostCourse(CsvOrder csvOrder) {
        return mapCsvPostCourseId.get(csvOrder);
    }

    public void setMapCsvPostCourse(Object csvData, String postCourseId) {
        mapCsvPostCourseId.put(csvData, postCourseId);
    }

    public Set<PostCourse> getPostCoursesIterable() {
        return postCoursesIterable;
    }

    public void putPostCoursesIterable(PostCourse postCourse) {
        postCoursesIterable.add(postCourse);
    }

    public boolean checkExistsPostCoursesIterable() {
        return postCoursesIterable.isEmpty();
    }

    public boolean checkExistsMapError() {
        return mapError == null || mapError.size() == 0;
    }

    public int getSizeMapError() {
        return mapError.size();
    }

    public Map<Integer, String> getMapError() {
        return mapError;
    }

    public void setMapError(Integer errorCode, String strCode) {
        mapError.put(errorCode, strCode);
    }

    public void innitDataGeneral() {
        this.postCoursesIterable = new HashSet<>();
        this.mapCsvPostCourseId = new HashMap<>();
        this.mapError = new HashMap<>();
        setStoreCodePost = new HashMap<>();
    }

    public void setMapStorePostCourse(int tenantId, Object csvData, String post, String skey, String storeId, String userId) {
        String postCourseId = postCourseRepository.findByStoreIdAndPost(storeId, post);
        if (postCourseId == null)
            postCourseId = getPostCourseId(tenantId, storeId, post, userId);
        setStoreCodePost.put(skey, postCourseId);
        setMapCsvPostCourse(csvData, postCourseId);
    }

    private String getPostCourseId(int tenantId, String storeId, String post, String userId) {
        PostCourse postCourse = new PostCourse(tenantId, storeId, post, userId);
        putPostCoursesIterable(postCourse);
        return postCourse.getPostCourseId();
    }

    public boolean checkStoreCodeContainKey(String keyStoreCodePost) {
        return setStoreCodePost.containsKey(keyStoreCodePost);
    }

    public String getStoreCodeWithPostByKey(String keyStoreCodePost) {
        return setStoreCodePost.get(keyStoreCodePost);
    }
}
