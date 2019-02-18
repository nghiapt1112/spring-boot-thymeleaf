package com.lyna.web.domain.storagefile.service.serviceImp;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.view.CsvOrder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class BaseStorageService extends BaseService {

    private Set<PostCourse> postCoursesIterable;
    private Map<Integer, String> mapError;
    private Map<Object, String> mapCsvPostCourseId;

    public Map<Object, String> getMapCsvPostCourseId() {
        return mapCsvPostCourseId;
    }

    public Set<PostCourse> getPostCoursesIterable() {
        return postCoursesIterable;
    }

    public void putPostCoursesIterable(PostCourse postCourse) {
        postCoursesIterable.add(postCourse);
    }

    public boolean checkExistsPostCoursesIterable(){
        return postCoursesIterable.isEmpty();
    }

    public int getSizeMapError() {
        return mapError.size();
    }

    public String getMapCsvPostCourse(CsvOrder csvOrder) {
        return mapCsvPostCourseId.get(csvOrder);
    }

    public void putMapCsvPostCourse(Object csvData, String postCourseId) {
        mapCsvPostCourseId.put(csvData, postCourseId);
    }

    public Map<Integer, String> getMapError() {
        return mapError;
    }


    public void setMapError(Integer errorCode, String strCode) {
        mapError.put(toInteger("err.csv.saveFileFailed.code"), toStr("err.csv.saveFileFailed.msg"));
    }

    public void innitDataGeneral() {
        this.postCoursesIterable = new HashSet<>();
        this.mapCsvPostCourseId = new HashMap<>();
        this.mapError = new HashMap<>();
    }
}
