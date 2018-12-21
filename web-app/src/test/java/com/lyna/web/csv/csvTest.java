package com.lyna.web.csv;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class csvTest {
    private static final String CSV_PATH = "./src/test/resources/product-with-header.csv";

    @Test
    public void readByNameMapping() {
        new ParseByName().readByName(CSV_PATH);
    }

    @Test
    public void readRecordsOneByOne() throws IOException {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(CSV_PATH));
                CSVReader csvReader = new CSVReader(reader)
        ) {
            // Reading Records One by One in a String array
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                System.out.println("Name : " + nextRecord[0]);
                System.out.println("Email : " + nextRecord[1]);
                System.out.println("Phone : " + nextRecord[2]);
                System.out.println("Country : " + nextRecord[3]);
                System.out.println("==========================");
            }
        }
    }

    @Test
    public void convertMapToJsonString() {
        Map<String, String> relationshipStatus = new HashMap<>();
        relationshipStatus.put("bob", "married");
        relationshipStatus.put("foo", "single");
        relationshipStatus.put("bar", "divorced");
        relationshipStatus.put("baz", "single");

        JsonArray collect = relationshipStatus.entrySet().stream().map(r -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", r.getKey());
            jsonObject.addProperty("relationshipStatus", r.getValue());
            return jsonObject;
        }).reduce(new JsonArray(), (jsonArray, jsonObject) -> {
            jsonArray.add(jsonObject);
            return jsonArray;
        }, (jsonArray, otherJsonArray) -> {
            jsonArray.addAll(otherJsonArray);
            return jsonArray;
        });

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("collect = " + gson.toJson(collect));
    }

}
