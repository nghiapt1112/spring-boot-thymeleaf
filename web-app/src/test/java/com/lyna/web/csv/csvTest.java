package com.lyna.web.csv;

import com.opencsv.CSVReader;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class csvTest {
        private static final String CSV_PATH = "./src/test/resources/product-with-header.csv";
    @Test
    public void readByNameMapping(){
        new ParseByName().readByName(CSV_PATH);
    }

    @Test
    public void readRecordsOneByOne() throws IOException {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(CSV_PATH));
                CSVReader csvReader = new CSVReader(reader);
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

}
