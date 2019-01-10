package com.lyna.web.domain.storagefile.repository.impl;

import com.lyna.web.domain.storagefile.repository.FileRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class FileRepositoryImpl implements FileRepository {

    private static final String COMMA_DELIMITER = ",";

    @Override
    public Map<Integer, String> readFileHeader(InputStream stream) throws IOException {
        Map<Integer, String> map = new HashMap<>();
        int index = 0;
        List<String> headers = new ArrayList<>();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                headers = Arrays.asList(values);
            }

            for (String header : headers) {
                map.put(index, header);
                index++;
            }
        }
        return map;
    }
}
