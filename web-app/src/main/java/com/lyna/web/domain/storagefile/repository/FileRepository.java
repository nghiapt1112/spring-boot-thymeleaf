package com.lyna.web.domain.storagefile.repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface FileRepository {

    Map<Integer, String> readFileHeader(InputStream stream) throws IOException;
}
