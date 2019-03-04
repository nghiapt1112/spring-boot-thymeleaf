package com.lyna.web.domain.storagefile.service.serviceImp;

import com.lyna.commons.utils.Constants;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.postCourse.repository.PostCourseRepository;
import com.lyna.web.domain.reader.service.impl.BaseReaderService;
import com.lyna.web.domain.storagefile.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class BaseStorageService extends BaseReaderService {
    protected Set<PostCourse> postCoursesIterable;
    protected Map<Object, String> mapCsvPostCourseId;
    protected Map<String, String> setStoreCodePost;

    @Autowired
    private PostCourseRepository postCourseRepository;

    public BaseStorageService(StorageProperties properties) {
        super(properties);
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
        mapCsvPostCourseId.put(csvData, postCourseId);
    }

    private String getPostCourseId(int tenantId, String storeId, String post, String userId) {
        PostCourse postCourse = new PostCourse(tenantId, storeId, post, userId);
        postCoursesIterable.add(postCourse);
        return postCourse.getPostCourseId();
    }

    public boolean checkStoreCodeContainKey(String keyStoreCodePost) {
        return setStoreCodePost.containsKey(keyStoreCodePost);
    }

    public String getStoreCodeWithPostByKey(String keyStoreCodePost) {
        return setStoreCodePost.get(keyStoreCodePost);
    }

    public boolean checkExistsMapError() {
        return mapError == null || mapError.size() == 0;
    }

    protected int getExtensionFile(MultipartFile file) {
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (extension.equals(Constants.FILE_EXTENSION.EXCEL_XLSX) || extension.equals(Constants.FILE_EXTENSION.EXCEL_XLS))
            return Constants.FILE_EXTENSION.EXCEL;
        else
            return Constants.FILE_EXTENSION.CSV;
    }

}
